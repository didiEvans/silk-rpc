package com.anker.rpc.core.proxy.javassist;

import com.anker.rpc.core.wrapper.RpcReferenceWrapper;
import com.anker.rpc.core.proxy.ProxyFactory;

public class JavassistProxyFactory  implements ProxyFactory {

    public JavassistProxyFactory() {
    }

    @Override
    public <T> T getProxy(RpcReferenceWrapper rpcReferenceWrapper) throws Throwable {
        return (T) ProxyGenerator.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                rpcReferenceWrapper.getAimClass(), new JavassistInvocationHandler(rpcReferenceWrapper));
    }
}