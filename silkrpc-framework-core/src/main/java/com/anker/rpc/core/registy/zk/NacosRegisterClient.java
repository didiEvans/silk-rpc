package com.anker.rpc.core.registy.zk;

import com.anker.rpc.core.registy.AbstractRegister;
import com.anker.rpc.core.registy.URL;

import java.util.List;
import java.util.Map;

/**
 * nacos 注册器
 * @author Anker
 */
public class NacosRegisterClient extends AbstractRegister {

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
    public Map<String, String> getServiceWeightMap(String serviceName) {
        return null;
    }

    @Override
    public List<String> getProviderIps(String serviceName) {
        return null;
    }
}
