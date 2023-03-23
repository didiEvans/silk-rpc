package com.anker.rpc.core.listener;

import cn.hutool.core.collection.CollUtil;
import com.anker.rpc.core.event.RocEvent;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 时间监听器加载器
 *
 * @author Anker
 */
public class SilkRpcListenerLoader {


    private static final List<SilkRpcListener<?>> RPC_LISTENERS = new ArrayList<>();

    private static final ThreadPoolExecutor EVENT_THREAD_POOL;

    static {
        Integer corePollSize = Runtime.getRuntime().availableProcessors();
        EVENT_THREAD_POOL = new ThreadPoolExecutor(corePollSize, Integer.MAX_VALUE,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(512), r -> new Thread("dispatcher-thread:"));
    }

    public static void registerListener(SilkRpcListener<?> iRpcListener) {
        RPC_LISTENERS.add(iRpcListener);
    }

    public void init() {
        registerListener(new ServiceUpdateListener());
        registerListener(new ServiceDestroyListener());
        registerListener(new ProviderNodeDataChangeListener());
    }

    /**
     * 获取接口上的泛型T
     *
     * @param o 接口
     */
    public static Class<?> getInterfaceT(Object o) {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type type = parameterizedType.getActualTypeArguments()[0];
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }

    /**
     * 同步事件处理，可能会堵塞
     */
    public static void sendSyncEvent(RocEvent iRpcEvent) {
        if (CollUtil.isEmpty(RPC_LISTENERS)) {
            return;
        }
        for (SilkRpcListener<?> rpcListener : RPC_LISTENERS) {
            Class<?> type = getInterfaceT(rpcListener);
            if (type != null && type.equals(iRpcEvent.getClass())) {
                try {
                    rpcListener.callBack(iRpcEvent.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendEvent(RocEvent rpcEvent) {
        if (CollUtil.isEmpty(RPC_LISTENERS)){
            return;
        }
        for (SilkRpcListener<?> iRpcListener : RPC_LISTENERS) {
            Class<?> type = getInterfaceT(iRpcListener);
            if (rpcEvent.getClass().equals(type)) {
                EVENT_THREAD_POOL.execute(() -> {
                    try {
                        iRpcListener.callBack(rpcEvent.getData());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
