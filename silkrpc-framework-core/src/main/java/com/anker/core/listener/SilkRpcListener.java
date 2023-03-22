package com.anker.core.listener;

/**
 * rpc监听器
 *
 * @param <T>
 */
public interface SilkRpcListener<T> {

    /**
     * 事件回调
     *
     * @param t
     */
    void callBack(Object t);


}
