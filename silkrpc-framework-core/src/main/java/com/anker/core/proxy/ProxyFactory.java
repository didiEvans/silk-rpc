package com.anker.core.proxy;

import com.anker.core.wrapper.RpcReferenceWrapper;

/**
 * 代理工厂
 * @author Anker
 */
public interface ProxyFactory {
    /**
     * 获取代理对象
     *
     * @param rpcReferenceWrapper
     * @param <T>
     * @return
     * @throws Throwable
     */
    <T> T getProxy(RpcReferenceWrapper rpcReferenceWrapper) throws Throwable;
}
