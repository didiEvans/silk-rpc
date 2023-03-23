package com.anker.rpc.core.filter.server;

import com.anker.rpc.core.filter.IFilter;
import com.anker.rpc.core.common.RpcInvocation;

/**
 * 服务端过滤器
 *
 * @author Anker
 */
public interface ServerFilter extends IFilter {

    /**
     * 执行核心过滤逻辑
     *
     * @param rpcInvocation
     */
    void doFilter(RpcInvocation rpcInvocation);

}
