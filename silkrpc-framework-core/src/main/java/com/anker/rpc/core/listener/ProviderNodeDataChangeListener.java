package com.anker.rpc.core.listener;

import com.anker.rpc.core.cache.CommonClientCache;
import com.anker.rpc.core.event.impl.RpcNodeChangeEvent;
import com.anker.rpc.core.registy.URL;
import com.anker.rpc.core.registy.zk.ProviderNodeInfo;
import com.anker.rpc.core.wrapper.ChannelFutureWrapper;

import java.util.List;

public class ProviderNodeDataChangeListener implements SilkRpcListener<RpcNodeChangeEvent> {

    @Override
    public void callBack(Object t) {
        ProviderNodeInfo providerNodeInfo = ((ProviderNodeInfo) t);
        List<ChannelFutureWrapper> channelFutureWrappers = CommonClientCache.CONNECT_MAP.get(providerNodeInfo.getServiceName());
        for (ChannelFutureWrapper channelFutureWrapper : channelFutureWrappers) {
            String address = channelFutureWrapper.getHost()+":"+channelFutureWrapper.getPort();
            if(address.equals(providerNodeInfo.getAddress())){
                //修改权重
                channelFutureWrapper.setWeight(providerNodeInfo.getWeight());
                URL url = new URL();
                url.setServiceName(providerNodeInfo.getServiceName());
                //更新权重 这里对应了文章顶部的RandomRouterImpl类
                IROUTER.updateWeight(url);
                break;
            }
        }
    }
}
