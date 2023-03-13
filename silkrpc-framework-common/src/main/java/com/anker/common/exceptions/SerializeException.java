package com.anker.common.exceptions;

/**
 * 序列化异常
 *
 * @author Anker
 */
public class SerializeException extends RuntimeException {

    public SerializeException() {
    }

    public SerializeException(String msg) {
        super(msg);
    }

    public SerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializeException(Throwable cause) {
        super(cause);
    }

    public SerializeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
