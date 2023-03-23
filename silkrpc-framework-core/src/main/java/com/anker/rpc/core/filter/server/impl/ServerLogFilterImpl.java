package com.anker.rpc.core.filter.server.impl;

import com.anker.rpc.core.filter.server.ServerFilter;
import com.anker.rpc.core.common.RpcInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志过滤器
 *
 * @author Anker
 */
public class ServerLogFilterImpl implements ServerFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(ServerLogFilterImpl.class);

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        logger.info(rpcInvocation.getAttachments().get("c_app_name") + " do invoke -----> " + rpcInvocation.getTargetServiceName() + "#" + rpcInvocation.getTargetMethod());
    }
}
