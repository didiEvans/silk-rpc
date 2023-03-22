package com.anker.rpc.core.filter.client.impl;

import com.anker.rpc.core.filter.client.ClientFilter;
import com.anker.rpc.core.common.RpcInvocation;
import com.anker.rpc.core.wrapper.ChannelFutureWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户端过滤器链
 *
 * @author Anker
 */
public class ClientFilterChain implements ClientFilter {

    private static final List<ClientFilter> I_CLIENT_FILTER_LIST = new ArrayList<>();

    public void addClientFilter(ClientFilter ClientFilter) {
        I_CLIENT_FILTER_LIST.add(ClientFilter);
    }

    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        for (ClientFilter clientFilter : I_CLIENT_FILTER_LIST) {
            clientFilter.doFilter(src, rpcInvocation);
        }
    }
}
