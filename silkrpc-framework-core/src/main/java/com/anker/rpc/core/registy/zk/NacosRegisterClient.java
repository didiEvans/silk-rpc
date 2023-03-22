package com.anker.rpc.core.registy.zk;

import com.anker.rpc.core.registy.URL;

import java.util.List;

public class NacosRegisterClient extends AbstractRegister {

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
}
