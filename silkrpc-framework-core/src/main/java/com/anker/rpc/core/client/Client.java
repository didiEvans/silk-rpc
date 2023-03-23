package com.anker.rpc.core.client;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.anker.rpc.core.cache.CommonClientCache;
import com.anker.rpc.core.codec.Decoder;
import com.anker.rpc.core.codec.Encoder;
import com.anker.rpc.core.common.RpcInvocation;
import com.anker.rpc.core.common.utils.IpUtil;
import com.anker.rpc.core.config.ClientConfig;
import com.anker.rpc.core.config.PropertiesBootstrap;
import com.anker.rpc.core.connection.ConnectionHandler;
import com.anker.rpc.core.filter.client.ClientFilter;
import com.anker.rpc.core.filter.client.impl.ClientFilterChain;
import com.anker.rpc.core.listener.SilkRpcListenerLoader;
import com.anker.rpc.core.protocol.RpcProtocol;
import com.anker.rpc.core.proxy.ProxyFactory;
import com.anker.rpc.core.registy.RegistryService;
import com.anker.rpc.core.registy.URL;
import com.anker.rpc.core.registy.AbstractRegister;
import com.anker.rpc.core.router.Router;
import com.anker.rpc.core.serialize.SerializeFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.anker.rpc.core.cache.CommonClientCache.*;
import static com.anker.rpc.core.config.DefaultRpcConfigProperties.DEFAULT_DECODE_CHAR;
import static com.anker.rpc.core.spi.ExtensionLoader.EXTENSION_LOADER_CLASS_CACHE;

/**
 * 客户端
 *
 * @author Anker
 */
public class Client {


    private final Logger log = LoggerFactory.getLogger(Client.class);

    private ClientConfig clientConfig;

    private AbstractRegister abstractRegister;

    private final Bootstrap bootstrap = new Bootstrap();

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public ClientConfig getClientConfig() {
        return clientConfig;
    }


    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public RpcReference initClientApplication() throws IllegalAccessException, InstantiationException, IOException, ClassNotFoundException {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ByteBuf delimiter = Unpooled.copiedBuffer(DEFAULT_DECODE_CHAR.getBytes());
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(clientConfig.getMaxServerRespDataSize(), delimiter));
                ch.pipeline().addLast(new Encoder());
                ch.pipeline().addLast(new Decoder());
                ch.pipeline().addLast(new ClientHandler());
            }
        });
        //初始化连接器
        ConnectionHandler.setBootstrap(bootstrap);
        //初始化监听器
        SilkRpcListenerLoader rpcListenerLoader = new SilkRpcListenerLoader();
        rpcListenerLoader.init();
        //初始化路由策略
        this.initRouter();
        //初始化序列化器
        this.initSerializer();
        //初始化过滤链
        this.initFilterChain();
        //初始化代理工厂
        return this.initRpcReference();
    }

    private RpcReference initRpcReference() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String proxyType = CommonClientCache.CLIENT_CONFIG.getProxyType();
        EXTENSION_LOADER.loadExtension(ProxyFactory.class);
        LinkedHashMap<String, Class<?>> proxyTypeMap = EXTENSION_LOADER_CLASS_CACHE.get(ProxyFactory.class.getName());
        Class<?> proxyTypeClass = proxyTypeMap.get(proxyType);
        if (proxyTypeClass == null) {
            throw new RuntimeException("no match proxyTypeClass for " + proxyType);
        }
        return new RpcReference((ProxyFactory) proxyTypeClass.newInstance());
    }

    private void initFilterChain() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        ClientFilterChain clientFilterChain = new ClientFilterChain();
        EXTENSION_LOADER.loadExtension(ClientFilter.class);
        LinkedHashMap<String, Class<?>> filterChainMap = EXTENSION_LOADER_CLASS_CACHE.get(ClientFilter.class.getName());
        for (Map.Entry<String, Class<?>> filterChainEntry : filterChainMap.entrySet()) {
            String filterChainKey = filterChainEntry.getKey();
            Class<?> filterChainImpl = filterChainEntry.getValue();
            if (filterChainImpl == null) {
                throw new RuntimeException("no match filterChainImpl for " + filterChainKey);
            }
            clientFilterChain.addClientFilter((ClientFilter) filterChainImpl.newInstance());
        }
        CommonClientCache.CLIENT_FILTER_CHAIN = clientFilterChain;
    }

    private void initSerializer() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String clientSerialize = CommonClientCache.CLIENT_CONFIG.getClientSerialize();
        EXTENSION_LOADER.loadExtension(SerializeFactory.class);
        LinkedHashMap<String, Class<?>> serializeMap = EXTENSION_LOADER_CLASS_CACHE.get(SerializeFactory.class.getName());
        Class<?> serializeClass = serializeMap.get(clientSerialize);
        if (serializeClass == null) {
            throw new RuntimeException("no match serializeClass for " + clientSerialize);
        }
        CommonClientCache.CLIENT_SERIALIZE_FACTORY = (SerializeFactory) serializeClass.newInstance();
    }

    private void initRouter() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String routerStrategy = CommonClientCache.CLIENT_CONFIG.getRouterStrategy();
        EXTENSION_LOADER.loadExtension(Router.class);
        LinkedHashMap<String, Class<?>> routerMap = EXTENSION_LOADER_CLASS_CACHE.get(Router.class.getName());
        Class<?> routerClass = routerMap.get(routerStrategy);
        if (routerClass == null) {
            throw new RuntimeException("no match routerStrategyClass for " + routerStrategy);
        }
        CommonClientCache.ROUTER = (Router) routerClass.newInstance();
    }

    public void initClientConfig() {
        CommonClientCache.CLIENT_CONFIG = PropertiesBootstrap.loadClientConfigFromLocal();
    }

    /**
     * 启动服务之前需要预先订阅对应的dubbo服务
     *
     * @param serviceBean
     */
    public void doSubscribeService(Class<?> serviceBean) {
        if (ObjectUtil.isNull(ABSTRACT_REGISTER)) {
            try {
                EXTENSION_LOADER.loadExtension(RegistryService.class);
                Map<String, Class<?>> registerMap = EXTENSION_LOADER_CLASS_CACHE.get(RegistryService.class.getName());
                Class<?> registerClass = registerMap.get(clientConfig.getRegisterType());
                ABSTRACT_REGISTER = (AbstractRegister) registerClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("registryServiceType unKnow,error is ", e);
            }
        }
        URL url = new URL();
        url.setApplicationName(clientConfig.getApplicationName());
        url.setServiceName(serviceBean.getName());
        url.addParameter("host", IpUtil.getIpAddress());
        Map<String, String> result = ABSTRACT_REGISTER.getServiceWeightMap(serviceBean.getName());
        URL_MAP.put(serviceBean.getName(), result);
        ABSTRACT_REGISTER.subscribe(url);
    }

    /**
     * 开始和各个provider建立连接
     */
    public void doConnectServer() {
        for (String providerServiceName : CommonClientCache.SUBSCRIBE_SERVICE_LIST) {
            List<String> providerIps = abstractRegister.getProviderIps(providerServiceName);
            for (String providerIp : providerIps) {
                try {
                    ConnectionHandler.connect(providerServiceName, providerIp);
                } catch (InterruptedException e) {
                    log.error("[doConnectServer] connect fail ", e);
                }
            }
            URL url = new URL();
            url.setServiceName(providerServiceName);
            //客户端在此新增一个订阅的功能
            abstractRegister.doAfterSubscribe(url);
        }
    }


    /**
     * 开启发送线程
     *
     * @param
     */
    public void startClient() {
        Thread asyncSendJob = new Thread(new AsyncSendJob());
        asyncSendJob.start();
    }

    class AsyncSendJob implements Runnable {

        public AsyncSendJob() {
        }

        @Override
        public void run() {
            while (true) {
                try {
                    //阻塞模式
                    RpcInvocation data = CommonClientCache.SEND_QUEUE.take();
                    String json = JSON.toJSONString(data);
                    RpcProtocol rpcProtocol = new RpcProtocol(json.getBytes());
                    ChannelFuture channelFuture = ConnectionHandler.getChannelFuture(data.getTargetServiceName());
                    channelFuture.channel().writeAndFlush(rpcProtocol);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
