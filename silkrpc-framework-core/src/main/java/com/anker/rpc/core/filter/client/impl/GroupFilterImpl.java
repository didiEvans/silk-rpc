package com.anker.rpc.core.filter.client.impl;

import cn.hutool.core.collection.CollUtil;
import com.anker.rpc.core.filter.client.ClientFilter;
import com.anker.rpc.core.common.RpcInvocation;
import com.anker.rpc.core.wrapper.ChannelFutureWrapper;

import java.util.List;

/**
 * 分组过滤器
 *
 * @author Anker
 */
public class GroupFilterImpl implements ClientFilter {

    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        String group = String.valueOf(rpcInvocation.getAttachments().get("group"));
        src.removeIf(channelFutureWrapper -> !channelFutureWrapper.getGroup().equals(group));
        if (CollUtil.isEmpty(src)) {
            throw new RuntimeException("no provider match for group " + group);
        }
    }

}
