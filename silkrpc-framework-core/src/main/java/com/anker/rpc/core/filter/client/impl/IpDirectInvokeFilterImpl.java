package com.anker.rpc.core.filter.client.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.anker.rpc.core.filter.client.ClientFilter;
import com.anker.rpc.core.common.RpcInvocation;
import com.anker.rpc.core.wrapper.ChannelFutureWrapper;

import java.util.List;

/**
 * ip直连器
 *
 * @author Anker ip直连过滤器
 */
public class IpDirectInvokeFilterImpl implements ClientFilter {

    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        String url = (String) rpcInvocation.getAttachments().get("url");
        if(CharSequenceUtil.isEmpty(url)){
            return;
        }
        src.removeIf(channelFutureWrapper -> !(channelFutureWrapper.getHost() + ":" + channelFutureWrapper.getPort()).equals(url));
        if(CollUtil.isEmpty(src)){
            throw new RuntimeException("no match provider url for "+ url);
        }
    }
}
