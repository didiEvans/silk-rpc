package com.anker.core.proxy;

import com.anker.core.client.RpcReferenceWrapper;

/**
 * 代理工厂
 * @author Anker
 */
public interface ProxyFactory {

    <T> T getProxy(RpcReferenceWrapper rpcReferenceWrapper) throws Throwable;
}
