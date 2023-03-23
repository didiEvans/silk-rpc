package com.anker.rpc.core.server;

import cn.hutool.core.text.CharSequenceUtil;
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
import com.anker.rpc.core.registry.AbstractRegister;
import com.anker.rpc.core.registry.RegistryService;
import com.anker.rpc.core.registry.URL;
import com.anker.rpc.core.serialize.SerializeFactory;
import com.anker.rpc.core.server.handler.MaxConnectionLimitHandler;
import com.anker.rpc.core.server.handler.ServerHandler;
import com.anker.rpc.core.wrapper.ServerServiceSemaphoreWrapper;
import com.anker.rpc.core.wrapper.ServiceWrapper;
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
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.anker.rpc.core.cache.CommonClientCache.EXTENSION_LOADER;
import static com.anker.rpc.core.cache.CommonServerCache.*;
import static com.anker.rpc.core.config.DefaultRpcConfigProperties.DEFAULT_DECODE_CHAR;
import static com.anker.rpc.core.spi.ExtensionLoader.EXTENSION_LOADER_CLASS_CACHE;

/**
 * 服务
 *
 * @author Anker
 */
public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    /**
     * 初始化线程池
     */
    private static final ThreadPoolExecutor EVENT_THREAD_POOL;
    static {
        Integer corePollSize = Runtime.getRuntime().availableProcessors();
        EVENT_THREAD_POOL = new ThreadPoolExecutor(corePollSize, Integer.MAX_VALUE,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(512), new DefaultThreadFactory("silk-rpc-exportServerWorker"));
    }

    /**
     * 服务端配置
     */
    private ServerConfig serverConfig;
    /**
     * 服务注册器
     */
    private RegistryService registryService;

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void startApplication() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 核心线程数
        int core = Runtime.getRuntime().availableProcessors() + 1;
        EventLoopGroup workerGroup = new NioEventLoopGroup(Math.min(core, 32), new DefaultThreadFactory("silk-rpc-NettyServerWorker", true));
        bootstrap.group(bossGroup, workerGroup);
        // NIO-SOCKET
        bootstrap.channel(NioServerSocketChannel.class);
        //有数据立即发送
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        //保持连接数
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.option(ChannelOption.SO_SNDBUF, 16 * 1024)
                .option(ChannelOption.SO_RCVBUF, 16 * 1024)
                //长链接
                .option(ChannelOption.SO_KEEPALIVE, true);
        /*
        服务端采用单一长连接的模式，这里所支持的最大连接数应该和机器本身的性能有关
        连接防护的handler应该绑定在Main-Reactor上
         */
        bootstrap.handler(new MaxConnectionLimitHandler(serverConfig.getMaxConnections()));
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                LOGGER.info("初始化provider过程");
                ByteBuf delimiter = Unpooled.copiedBuffer(DEFAULT_DECODE_CHAR.getBytes());
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(serverConfig.getMaxServerRequestData(), delimiter));
                ch.pipeline().addLast(new Encoder());
                ch.pipeline().addLast(new Decoder());
                //这里面需要注意出现堵塞的情况发生，建议将核心业务内容分配给业务线程池处理
                ch.pipeline().addLast(new ServerHandler());
            }
        });
        this.batchExportUrl();
        //开始准备接收请求的任务
        SERVER_CHANNEL_DISPATCHER.startDataConsume();
        bootstrap.bind(serverConfig.getServerPort()).sync();
        IS_STARTED = true;
        LOGGER.info("[startApplication] server is started!");
    }

    public void initServerConfig() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        ServerConfig serverConfig = PropertiesBootstrap.loadServerConfigFromLocal();
        this.setServerConfig(serverConfig);
        SERVER_CONFIG = serverConfig;
        //初始化线程池和队列的配置
        SERVER_CHANNEL_DISPATCHER.init(SERVER_CONFIG.getServerQueueSize(), SERVER_CONFIG.getServerBizThreadNums());
        //序列化技术初始化
        String serverSerialize = serverConfig.getServerSerialize();
        EXTENSION_LOADER.loadExtension(SerializeFactory.class);
        LinkedHashMap<String, Class<?>> serializeFactoryClassMap = EXTENSION_LOADER_CLASS_CACHE.get(SerializeFactory.class.getName());
        Class<?> serializeFactoryClass = serializeFactoryClassMap.get(serverSerialize);
        if (serializeFactoryClass == null) {
            throw new RuntimeException("no match serialize type for " + serverSerialize);
        }
        SERVER_SERIALIZE_FACTORY = (SerializeFactory) serializeFactoryClass.newInstance();
        //过滤链技术初始化
        EXTENSION_LOADER.loadExtension(ServerFilter.class);
        LinkedHashMap<String, Class<?>> iServerFilterClassMap = EXTENSION_LOADER_CLASS_CACHE.get(ServerFilter.class.getName());
        ServerBeforeFilterChain serverBeforeFilterChain = new ServerBeforeFilterChain();
        ServerAfterFilterChain serverAfterFilterChain = new ServerAfterFilterChain();
        //过滤器初始化环节新增 前置过滤器和后置过滤器
        for (String iServerFilterKey : iServerFilterClassMap.keySet()) {
            Class<?> iServerFilterClass = iServerFilterClassMap.get(iServerFilterKey);
            if (iServerFilterClass == null) {
                throw new RuntimeException("no match iServerFilter type for " + iServerFilterKey);
            }
            SPI spi = (SPI) iServerFilterClass.getDeclaredAnnotation(SPI.class);
            if (spi != null && "before".equals(spi.value())) {
                serverBeforeFilterChain.addServerFilter((ServerFilter) iServerFilterClass.newInstance());
            } else if (spi != null && "after".equals(spi.value())) {
                serverAfterFilterChain.addServerFilter((ServerFilter) iServerFilterClass.newInstance());
            }
        }
        SERVER_AFTER_FILTER_CHAIN = serverAfterFilterChain;
        SERVER_BEFORE_FILTER_CHAIN = serverBeforeFilterChain;
    }

    /**
     * 暴露服务信息
     *
     * @param serviceWrapper 服务提供者包装类
     */
    public void exportService(ServiceWrapper serviceWrapper) {
        Object serviceBean = serviceWrapper.getServiceObj();
        if (serviceBean.getClass().getInterfaces().length == 0) {
            throw new RuntimeException("service must had interfaces!");
        }
        Class<?>[] classes = serviceBean.getClass().getInterfaces();
        if (classes.length > 1) {
            throw new RuntimeException("service must only had one interfaces!");
        }
        if (REGISTRY_SERVICE == null) {
            try {
                EXTENSION_LOADER.loadExtension(RegistryService.class);
                Map<String, Class<?>> registryClassMap = EXTENSION_LOADER_CLASS_CACHE.get(RegistryService.class.getName());
                Class<?> registryClass = registryClassMap.get(serverConfig.getRegisterType());
                REGISTRY_SERVICE = (AbstractRegister) registryClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("registryServiceType unKnow,error is ", e);
            }
        }
        //默认选择该对象的第一个实现接口
        Class<?> interfaceClass = classes[0];
        PROVIDER_CLASS_MAP.put(interfaceClass.getName(), serviceBean);
        URL url = new URL();
        url.setServiceName(interfaceClass.getName());
        url.setApplicationName(serverConfig.getApplicationName());
        url.addParameter("host", IpUtil.getIpAddress());
        url.addParameter("port", String.valueOf(serverConfig.getServerPort()));
        url.addParameter("group", String.valueOf(serviceWrapper.getGroup()));
        url.addParameter("limit", String.valueOf(serviceWrapper.getLimit()));
        //设置服务端的限流器
        SERVER_SERVICE_SEMAPHORE_MAP.put(interfaceClass.getName(), new ServerServiceSemaphoreWrapper(serviceWrapper.getLimit()));
        PROVIDER_URL_SET.add(url);
        if (CharSequenceUtil.isNotBlank(serviceWrapper.getServiceToken())) {
            PROVIDER_SERVICE_WRAPPER_MAP.put(interfaceClass.getName(), serviceWrapper);
        }
    }

    public void batchExportUrl() {
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
