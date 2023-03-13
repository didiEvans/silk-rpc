package com.anker.common.exceptions;

/**
 * Rpc调用失败异常
 *
 * @author Anker
 */
public class RpcInvokeFailedException extends RuntimeException {

    public RpcInvokeFailedException() {
        super();
    }

    public RpcInvokeFailedException(String message) {
        super(message);
    }

    public RpcInvokeFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcInvokeFailedException(Throwable cause) {
        super(cause);
    }

    public RpcInvokeFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
