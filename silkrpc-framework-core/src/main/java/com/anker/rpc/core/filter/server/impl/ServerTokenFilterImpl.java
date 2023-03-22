package com.anker.rpc.core.filter.server.impl;

import cn.hutool.core.text.CharSequenceUtil;
import com.anker.rpc.core.cache.CommonServerCache;
import com.anker.rpc.core.filter.server.ServerFilter;
import com.anker.rpc.core.common.RpcInvocation;
import com.anker.rpc.core.wrapper.ServiceWrapper;

/**
 * 服务端token认证过滤器
 *
 * @author YBin
 */
public class ServerTokenFilterImpl implements ServerFilter {

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        String token = String.valueOf(rpcInvocation.getAttachments().get("serviceToken"));
        ServiceWrapper serviceWrapper = CommonServerCache.PROVIDER_SERVICE_WRAPPER_MAP.get(rpcInvocation.getTargetServiceName());
        String matchToken = String.valueOf(serviceWrapper.getServiceToken());
        if (CharSequenceUtil.isBlank(matchToken)) {
            return;
        }
        if (!CharSequenceUtil.isBlank(token) && token.equals(matchToken)) {
            return;
        }
        throw new RuntimeException("token is " + token + " , verify result is false!");
    }
}
