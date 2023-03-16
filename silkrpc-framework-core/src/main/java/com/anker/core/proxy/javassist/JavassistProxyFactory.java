package com.anker.core.proxy.javassist;

import com.anker.core.wrapper.RpcReferenceWrapper;
import com.anker.core.proxy.ProxyFactory;

public class JavassistProxyFactory  implements ProxyFactory {

    public JavassistProxyFactory() {
    }

    @Override
    public <T> T getProxy(RpcReferenceWrapper rpcReferenceWrapper) throws Throwable {
        return (T) ProxyGenerator.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                rpcReferenceWrapper.getAimClass(), new JavassistInvocationHandler(rpcReferenceWrapper));
    }
}