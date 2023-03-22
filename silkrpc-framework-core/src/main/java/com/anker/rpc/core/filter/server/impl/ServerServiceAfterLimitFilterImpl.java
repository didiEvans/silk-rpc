package com.anker.rpc.core.filter.server.impl;

import com.anker.rpc.annotation.spi.SPI;
import com.anker.rpc.core.filter.server.ServerFilter;
import com.anker.rpc.core.common.RpcInvocation;
import com.anker.rpc.core.wrapper.ServerServiceSemaphoreWrapper;
import com.anker.rpc.core.cache.CommonServerCache;

/**
 *
 *
 * @author Anker
 */
@SPI("after")
public class ServerServiceAfterLimitFilterImpl implements ServerFilter {

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        String serviceName = rpcInvocation.getTargetServiceName();
        ServerServiceSemaphoreWrapper serverServiceSemaphoreWrapper = CommonServerCache.SERVER_SERVICE_SEMAPHORE_MAP.get(serviceName);
        serverServiceSemaphoreWrapper.getSemaphore().release();
    }
}
