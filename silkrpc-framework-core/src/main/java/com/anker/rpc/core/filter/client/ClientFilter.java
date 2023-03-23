package com.anker.rpc.core.filter.client;

import com.anker.rpc.core.filter.IFilter;
import com.anker.rpc.core.common.RpcInvocation;
import com.anker.rpc.core.wrapper.ChannelFutureWrapper;

import java.util.List;

/**
 * 客户端过滤器
 *
 * @author Anker
 */
public interface ClientFilter extends IFilter {

    /**
     * 执行过滤链
     *
     * @param src   调用channel源
     * @param rpcInvocation RPC调用信息
     */
    void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation);

}
