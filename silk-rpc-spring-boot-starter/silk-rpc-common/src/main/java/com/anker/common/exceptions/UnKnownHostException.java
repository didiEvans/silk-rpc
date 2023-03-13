package com.anker.common.exceptions;

/**
 * 未知主机异常
 *
 * @author Anker
 */
public class UnKnownHostException extends RuntimeException {

    public UnKnownHostException() {
    }

    public UnKnownHostException(String message) {
        super(message);
    }

    public UnKnownHostException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnKnownHostException(Throwable cause) {
        super(cause);
    }

    public UnKnownHostException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
