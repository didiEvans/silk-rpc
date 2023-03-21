package com.anker.core.router.impl;

import com.anker.core.cache.CommonClientCache;
import com.anker.core.registy.URL;
import com.anker.core.router.IRouter;
import com.anker.core.router.Selector;
import com.anker.core.wrapper.ChannelFutureWrapper;

import java.util.List;

/**
 * 轮询路由
 *
 * @author Anker
 */
public class RotateRouterImpl implements IRouter {

    @Override
    public void refreshRouterArr(Selector selector) {
        List<ChannelFutureWrapper> channelFutureWrappers = CommonClientCache.CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureWrapper[] arr = new ChannelFutureWrapper[channelFutureWrappers.size()];
        for (int i=0;i<channelFutureWrappers.size();i++) {
            arr[i]=channelFutureWrappers.get(i);
        }
        CommonClientCache.SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(),arr);
    }

    @Override
    public ChannelFutureWrapper select(Selector selector) {
        return CommonClientCache.CHANNEL_FUTURE_POLLING_REF.getChannelFutureWrapper(selector);
    }

    @Override
    public void updateWeight(URL url) {

    }
}
