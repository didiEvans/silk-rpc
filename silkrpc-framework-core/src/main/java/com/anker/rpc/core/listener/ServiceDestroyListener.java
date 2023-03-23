package com.anker.rpc.core.listener;


import com.anker.rpc.core.event.impl.RpcDestroyEvent;
import com.anker.rpc.core.registy.URL;

import static com.anker.rpc.core.cache.CommonServerCache.PROVIDER_URL_SET;
import static com.anker.rpc.core.cache.CommonServerCache.REGISTRY_SERVICE;

/**
 * 服务注销 监听器
 *
 * @author Anker
 */
public class ServiceDestroyListener implements SilkRpcListener<RpcDestroyEvent> {

    @Override
    public void callBack(Object t) {
        for (URL url : PROVIDER_URL_SET) {
            REGISTRY_SERVICE.unRegister(url);
        }
    }
}
