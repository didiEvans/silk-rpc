package com.anker.rpc.core.common.exceptions;

import com.anker.rpc.core.common.RpcInvocation;

public class MaxServiceLimitRequestException extends RpcException{

    public MaxServiceLimitRequestException(RpcInvocation rpcInvocation) {
        super(rpcInvocation);
    }
}
