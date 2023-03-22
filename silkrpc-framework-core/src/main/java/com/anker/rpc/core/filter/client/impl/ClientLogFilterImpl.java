package com.anker.rpc.core.filter.client.impl;

import com.anker.rpc.core.cache.CommonClientCache;
import com.anker.rpc.core.filter.client.ClientFilter;
import com.anker.rpc.core.common.RpcInvocation;
import com.anker.rpc.core.wrapper.ChannelFutureWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 客户端日志过滤器实现
 *
 * @author Anker
 */
public class ClientLogFilterImpl implements ClientFilter {

    private static final Logger logger = LoggerFactory.getLogger(ClientLogFilterImpl.class);

    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        rpcInvocation.getAttachments().put("c_app_name", CommonClientCache.CLIENT_CONFIG.getApplicationName());
        logger.info(rpcInvocation.getAttachments().get("c_app_name") + " do invoke -----> " + rpcInvocation.getTargetServiceName());
    }
}
