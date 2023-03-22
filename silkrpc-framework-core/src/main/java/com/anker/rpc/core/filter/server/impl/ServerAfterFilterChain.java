package com.anker.rpc.core.filter.server.impl;

import com.anker.rpc.core.filter.server.ServerFilter;
import com.anker.rpc.core.common.RpcInvocation;

import java.util.ArrayList;
import java.util.List;


public class ServerAfterFilterChain implements ServerFilter {


    private static final List<ServerFilter> SERVER_FILTERS = new ArrayList<>();

    public void addServerFilter(ServerFilter iServerFilter) {
        SERVER_FILTERS.add(iServerFilter);
    }

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        for (ServerFilter serverFilter : SERVER_FILTERS) {
            serverFilter.doFilter(rpcInvocation);
        }
    }
}
