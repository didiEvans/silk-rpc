package com.anker.core.cache;

import com.anker.core.config.ServerConfig;
import com.anker.core.dispatcher.ServerChannelDispatcher;
import com.anker.core.serialize.SerializeFactory;
import io.netty.util.internal.ConcurrentSet;

import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 公共的服务缓存
 *
 * @author Anker
 */
public class CommonServerCache {


    public static final Map<String,Object> PROVIDER_CLASS_MAP = new ConcurrentHashMap<>();
    public static final Set<URL> PROVIDER_URL_SET = new ConcurrentSet<>();
    public static AbstractRegister REGISTRY_SERVICE;
    public static SerializeFactory SERVER_SERIALIZE_FACTORY;
    public static ServerConfig SERVER_CONFIG;
    public static ServerBeforeFilterChain SERVER_BEFORE_FILTER_CHAIN;
    public static ServerAfterFilterChain SERVER_AFTER_FILTER_CHAIN;
    public static final Map<String, ServiceWrapper> PROVIDER_SERVICE_WRAPPER_MAP = new ConcurrentHashMap<>();
    public static Boolean IS_STARTED = false;
    public static ServerChannelDispatcher SERVER_CHANNEL_DISPATCHER = new ServerChannelDispatcher();
    public static final Map<String, ServerServiceSemaphoreWrapper> SERVER_SERVICE_SEMAPHORE_MAP = new ConcurrentHashMap<>(64);
}
