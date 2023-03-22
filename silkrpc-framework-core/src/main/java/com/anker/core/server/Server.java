package com.anker.core.server;

import cn.hutool.core.net.NetUtil;
import com.anker.common.utils.IpUtil;
import com.anker.core.cache.CommonServerCache;
import com.anker.core.codec.Decoder;
import com.anker.core.codec.Encoder;
import com.anker.core.config.PropertiesBootstrap;
import com.anker.core.config.ServerConfig;
import com.anker.core.registy.RegistryService;
import com.anker.core.registy.URL;
import com.anker.core.registy.zk.ZookeeperRegister;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

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

    public void startApplication() throws InterruptedException {
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

        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                System.out.println("初始化provider过程");
                ch.pipeline().addLast(new Encoder());
                ch.pipeline().addLast(new Decoder());
                ch.pipeline().addLast(new ServerHandler());
            }
        });
        this.batchExportUrl();
        bootstrap.bind(serverConfig.getServerPort()).sync();
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
        url.addParameter("port", String.valueOf(serverConfig.getServerPort()));
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
