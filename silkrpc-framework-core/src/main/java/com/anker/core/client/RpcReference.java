package com.anker.core.client;


import com.anker.core.proxy.ProxyFactory;

public class RpcReference {

    public ProxyFactory proxyFactory;

    public RpcReference(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    public <T> T get(RpcReferenceWrapper<T> rpcReferenceWrapper) throws Throwable {
        initGlobalRpcReferenceWrapperConfig(rpcReferenceWrapper);
        return proxyFactory.getProxy(rpcReferenceWrapper);
    }

    private void initGlobalRpcReferenceWrapperConfig(RpcReferenceWrapper rpcReferenceWrapper) {
        if (rpcReferenceWrapper.getTimeOUt() == null) {
            rpcReferenceWrapper.setTimeOut(CLIENT_CONFIG.getTimeOut());
        }
    }
}
