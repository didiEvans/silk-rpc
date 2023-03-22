package com.anker.rpc.core.server;

import com.anker.rpc.annotation.spi.SPI;
import com.anker.rpc.core.cache.CommonServerCache;
import com.anker.rpc.core.codec.Decoder;
import com.anker.rpc.core.codec.Encoder;
import com.anker.rpc.core.common.utils.IpUtil;
import com.anker.rpc.core.config.PropertiesBootstrap;
import com.anker.rpc.core.config.ServerConfig;
import com.anker.rpc.core.filter.server.ServerFilter;
import com.anker.rpc.core.filter.server.impl.ServerAfterFilterChain;
import com.anker.rpc.core.filter.server.impl.ServerBeforeFilterChain;
import com.anker.rpc.core.listener.SilkRpcListenerLoader;
import com.anker.rpc.core.registy.RegistryService;
import com.anker.rpc.core.registy.URL;
import com.anker.rpc.core.registy.zk.ZookeeperRegister;
import com.anker.rpc.core.serialize.SerializeFactory;
import com.anker.rpc.core.server.handler.MaxConnectionLimitHandler;
import com.anker.rpc.core.server.handler.ServerHandler;
import com.anker.rpc.core.cache.CommonClientCache;
import com.anker.rpc.core.config.DefaultRpcConfigProperties;
import com.anker.rpc.core.spi.ExtensionLoader;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 服务
 *
 * @author Anker
 */
public class Server {

    private static final ThreadPoolExecutor EVENT_THREAD_POOL;

    static {
        Integer corePollSize = Runtime.getRuntime().availableProcessors();
        EVENT_THREAD_POOL = new ThreadPoolExecutor(corePollSize, Integer.MAX_VALUE,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(512), r -> new Thread("dispatcher-thread:"));
    }

    /**
     * 服务端配置
     */
    private ServerConfig serverConfig;
    /**
     * 注册服务
     */
    private RegistryService registryService;

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void startApplication() throws InterruptedException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.option(ChannelOption.SO_SNDBUF, 16 * 1024)
                .option(ChannelOption.SO_RCVBUF, 16 * 1024)
                .option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new MaxConnectionLimitHandler(serverConfig.getMaxConnections()));
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                System.out.println("初始化provider过程");
                ByteBuf delimiter = Unpooled.copiedBuffer(DefaultRpcConfigProperties.DEFAULT_DECODE_CHAR.getBytes());
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(CommonServerCache.SERVER_CONFIG.getMaxServerRequestData(), delimiter));
                ch.pipeline().addLast(new Encoder());
                ch.pipeline().addLast(new Decoder());
                ch.pipeline().addLast(new ServerHandler());
            }
        });

        //初始化监听器
        SilkRpcListenerLoader rpcListenerLoader = new SilkRpcListenerLoader();
        rpcListenerLoader.init();

        //初始化序列化器
        String serverSerialize = CommonServerCache.SERVER_CONFIG.getServerSerialize();
        CommonClientCache.EXTENSION_LOADER.loadExtension(SerializeFactory.class);
        LinkedHashMap<String, Class<?>> serializeMap = ExtensionLoader.EXTENSION_LOADER_CLASS_CACHE.get(SerializeFactory.class.getName());
        Class<?> serializeClass = serializeMap.get(serverSerialize);
        if (serializeClass == null) {
            throw new RuntimeException("no match serializeClass for " + serverSerialize);
        }
        CommonServerCache.SERVER_SERIALIZE_FACTORY = (SerializeFactory) serializeClass.newInstance();


        //初始化过滤链
        ServerBeforeFilterChain serverBeforeFilterChain = new ServerBeforeFilterChain();
        ServerAfterFilterChain serverAfterFilterChain = new ServerAfterFilterChain();
        CommonClientCache.EXTENSION_LOADER.loadExtension(ServerFilter.class);
        LinkedHashMap<String, Class<?>> filterChainMap = ExtensionLoader.EXTENSION_LOADER_CLASS_CACHE.get(ServerFilter.class.getName());
        for (Map.Entry<String, Class<?>> filterChainEntry : filterChainMap.entrySet()) {
            String filterChainKey = filterChainEntry.getKey();
            Class<?> filterChainImpl = filterChainEntry.getValue();
            if (filterChainImpl == null) {
                throw new RuntimeException("no match filterChainImpl for " + filterChainKey);
            }
            SPI spi = filterChainImpl.getDeclaredAnnotation(SPI.class);
            if (spi != null && "before".equalsIgnoreCase(spi.value())) {
                serverBeforeFilterChain.addServerFilter((ServerFilter) filterChainImpl.newInstance());
            } else if (spi != null && "after".equalsIgnoreCase(spi.value())) {
                serverAfterFilterChain.addServerFilter((ServerFilter) filterChainImpl.newInstance());
            }
        }
        CommonServerCache.SERVER_BEFORE_FILTER_CHAIN = serverBeforeFilterChain;
        CommonServerCache.SERVER_AFTER_FILTER_CHAIN = serverAfterFilterChain;

        //初始化请求分发器
        CommonServerCache.SERVER_CHANNEL_DISPATCHER.init(CommonServerCache.SERVER_CONFIG.getServerQueueSize(), CommonServerCache.SERVER_CONFIG.getServerBizThreadNums());
        CommonServerCache.SERVER_CHANNEL_DISPATCHER.startDataConsume();

        //暴露服务端url
        this.batchExportUrl();
        bootstrap.bind(CommonServerCache.SERVER_CONFIG.getPort()).sync();
    }

    public void initServerConfig() {
        ServerConfig serverConfig = PropertiesBootstrap.loadServerConfigFromLocal();
        this.setServerConfig(serverConfig);
    }

    /**
     * 暴露服务信息
     *
     * @param serviceBean 服务提供者bean
     */
    public void exportService(Object serviceBean) {
        if (serviceBean.getClass().getInterfaces().length == 0) {
            throw new RuntimeException("service must had interfaces!");
        }
        Class<?>[] classes = serviceBean.getClass().getInterfaces();
        if (classes.length > 1) {
            throw new RuntimeException("service must only had one interfaces!");
        }
        if (registryService == null) {
            registryService = new ZookeeperRegister(serverConfig.getRegisterAddr());
        }
        //默认选择该对象的第一个实现接口
        Class<?> interfaceClass = classes[0];
        CommonServerCache.PROVIDER_CLASS_MAP.put(interfaceClass.getName(), serviceBean);
        URL url = new URL();
        url.setServiceName(interfaceClass.getName());
        url.setApplicationName(serverConfig.getApplicationName());
        url.addParameter("host", IpUtil.getIpAddr());
        url.addParameter("port", String.valueOf(serverConfig.getPort()));
        CommonServerCache.PROVIDER_URL_SET.add(url);
    }

    public void batchExportUrl(){
        EVENT_THREAD_POOL.execute(() -> {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (URL url : CommonServerCache.PROVIDER_URL_SET) {
                registryService.register(url);
            }
        });
    }
}
