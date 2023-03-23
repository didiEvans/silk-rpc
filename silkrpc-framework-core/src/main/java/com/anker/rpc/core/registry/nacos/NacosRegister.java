package com.anker.rpc.core.registry.nacos;

import com.anker.rpc.core.registry.URL;
import com.anker.rpc.core.registry.AbstractRegister;

import java.util.List;
import java.util.Map;

/**
 * nacos注册器
 *
 * @author Anker
 */
public class NacosRegister extends AbstractRegister {

    @Override
    public void unRegister(URL url) {

    }

    @Override
    public void doAfterSubscribe(URL url) {

    }

    @Override
    public void doBeforeSubscribe(URL url) {

    }

    @Override
    public List<String> getProviderIps(String serviceName) {
        return null;
    }

    @Override
    public Map<String, String> getServiceWeightMap(String serviceName) {
        return null;
    }

    @Override
    public void register(URL url) {
        super.register(url);
    }

    @Override
    public void offline(URL url) {
        super.offline(url);
    }

    @Override
    public void subscribe(URL url) {
        super.subscribe(url);
    }

    @Override
    public void doUnSubscribe(URL url) {
        super.doUnSubscribe(url);
    }
}
