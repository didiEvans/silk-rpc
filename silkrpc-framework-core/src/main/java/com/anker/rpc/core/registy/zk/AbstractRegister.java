package com.anker.rpc.core.registy.zk;

import com.anker.rpc.core.cache.CommonClientCache;
import com.anker.rpc.core.cache.CommonServerCache;
import com.anker.rpc.core.registy.RegistryService;
import com.anker.rpc.core.registy.URL;

import java.util.List;

public abstract class AbstractRegister implements RegistryService {


    @Override
    public void register(URL url) {
        CommonServerCache.PROVIDER_URL_SET.add(url);
    }

    @Override
    public void offline(URL url) {
        CommonServerCache.PROVIDER_URL_SET.remove(url);
    }

    @Override
    public void subscribe(URL url) {
        CommonClientCache.SUBSCRIBE_SERVICE_LIST.add(url.getServiceName());
    }

    @Override
    public void doUnSubscribe(URL url) {
        CommonClientCache.SUBSCRIBE_SERVICE_LIST.remove(url.getServiceName());
    }

    /**
     * 留给子类扩展
     *
     * @param url
     */
    public abstract void doAfterSubscribe(URL url);

    /**
     * 留给子类扩展
     *
     * @param url
     */
    public abstract void doBeforeSubscribe(URL url);

    /**
     * 留给子类扩展
     *
     * @param serviceName
     * @return
     */
    public abstract List<String> getProviderIps(String serviceName);

    /**
     * 服务注销
     *
     * @param url 服务地址
     */
    public abstract void unRegister(URL url);
}
