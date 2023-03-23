package com.anker.rpc.core.cache;

import com.anker.rpc.core.config.ServerConfig;
import com.anker.rpc.core.dispatcher.ServerChannelDispatcher;
import com.anker.rpc.core.filter.server.impl.ServerAfterFilterChain;
import com.anker.rpc.core.filter.server.impl.ServerBeforeFilterChain;
import com.anker.rpc.core.registry.URL;
import com.anker.rpc.core.registry.AbstractRegister;
import com.anker.rpc.core.serialize.SerializeFactory;
import com.anker.rpc.core.wrapper.ServerServiceSemaphoreWrapper;
import com.anker.rpc.core.wrapper.ServiceWrapper;
import io.netty.util.internal.ConcurrentSet;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 公共的服务缓存
 *
 * @author Anker
 */
public class CommonServerCache {

    /**
     * 服务提供者
     */
    public static final Map<String,Object> PROVIDER_CLASS_MAP = new ConcurrentHashMap<>();
    /**
     * 服务提供者URL集合
     */
    public static final Set<URL> PROVIDER_URL_SET = new ConcurrentSet<>();
    /**
     * 注册服务
     */
    public static AbstractRegister REGISTRY_SERVICE;
    /**
     * 服务端序列化工厂类
     */
    public static SerializeFactory SERVER_SERIALIZE_FACTORY;
    /**
     * 服务端配置
     */
    public static ServerConfig SERVER_CONFIG;
    /**
     * 前置过滤器
     */
    public static ServerBeforeFilterChain SERVER_BEFORE_FILTER_CHAIN;
    /**
     * 后置过滤器
     */
    public static ServerAfterFilterChain SERVER_AFTER_FILTER_CHAIN;
    /**
     * 服务提供者包装类集合
     */
    public static final Map<String, ServiceWrapper> PROVIDER_SERVICE_WRAPPER_MAP = new ConcurrentHashMap<>();
    public static Boolean IS_STARTED = false;
    /**
     * 服务端请求转发器
     */
    public static ServerChannelDispatcher SERVER_CHANNEL_DISPATCHER = new ServerChannelDispatcher();
    /**
     * 服务端信标
     */
    public static final Map<String, ServerServiceSemaphoreWrapper> SERVER_SERVICE_SEMAPHORE_MAP = new ConcurrentHashMap<>(64);
}
