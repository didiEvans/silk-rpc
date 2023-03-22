package com.anker.rpc.core.filter.server.impl;

import com.anker.rpc.core.filter.server.ServerFilter;
import com.anker.rpc.core.common.RpcInvocation;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务端过滤器链
 *
 * @author Anker
 */
public class ServerFilterChain implements ServerFilter {
    
    private static final List<ServerFilter> I_SERVER_FILTERS = new ArrayList<>();

    public void addServerFilter(ServerFilter serverFilter) {
        I_SERVER_FILTERS.add(serverFilter);
    }

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        for (ServerFilter serverFilter : I_SERVER_FILTERS) {
            serverFilter.doFilter(rpcInvocation);
        }
    }
}
