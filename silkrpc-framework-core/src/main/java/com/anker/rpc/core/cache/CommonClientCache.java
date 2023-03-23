package com.anker.rpc.core.cache;

import com.anker.rpc.core.filter.client.impl.ClientFilterChain;
import com.anker.rpc.core.router.Router;
import com.anker.rpc.core.spi.ExtensionLoader;
import com.anker.rpc.core.common.ChannelFuturePollingRef;
import com.anker.rpc.core.common.RpcInvocation;
import com.anker.rpc.core.config.ClientConfig;
import com.anker.rpc.core.registry.AbstractRegister;
import com.anker.rpc.core.serialize.SerializeFactory;
import com.anker.rpc.core.wrapper.ChannelFutureWrapper;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 公共客户端缓存
 *
 * @author Anker
 */
public class CommonClientCache {
    /**
     * 调用请求发送队列
     */
    public static BlockingQueue<RpcInvocation> SEND_QUEUE = new ArrayBlockingQueue<>(100);
    /**
     * 响应Map
     */
    public static Map<String, Object> RESP_MAP = new ConcurrentHashMap<>();
    /**
     * 客户端订阅的服务
     */
    public static List<String> SUBSCRIBE_SERVICE_LIST = new ArrayList<>();
    /**
     * URL_MAP
     */
    public static Map<String, Map<String, String>> URL_MAP = new ConcurrentHashMap<>();
    /**
     * 服务端地址
     */
    public static Set<String> SERVER_ADDRESS = new HashSet<>();
    /**
     * 远程调用map -> key:需要调用的serviceName value:与多个服务提供者建立的连接
     * 当客户端需要调用指定serviceName时，可以根据不同策略从多个服务提供者中选择一个
     */
    public static Map<String, List<ChannelFutureWrapper>> CONNECT_MAP = new ConcurrentHashMap<>();
    /**
     * 随机请求的map
     */
    public static Map<String, ChannelFutureWrapper[]> SERVICE_ROUTER_MAP = new ConcurrentHashMap<>();
    /**
     * channel轮询引用
     */
    public static ChannelFuturePollingRef CHANNEL_FUTURE_POLLING_REF = new ChannelFuturePollingRef();
    /**
     * 客户端路由
     */
    public static Router ROUTER;
    /**
     * 客户端指定的序列化工厂
     */
    public static SerializeFactory CLIENT_SERIALIZE_FACTORY;
    /**
     * 客户端配置
     */
    public static ClientConfig CLIENT_CONFIG;
    /**
     * 客户端过滤器链
     */
    public static ClientFilterChain CLIENT_FILTER_CHAIN;
    /**
     * 客户端注册器
     */
    public static AbstractRegister ABSTRACT_REGISTER;
    /**
     * 客户端spi拓展加载器
     */
    public static ExtensionLoader EXTENSION_LOADER = new ExtensionLoader();

}
