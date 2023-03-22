package com.anker.rpc.core.listener;

/**
 * rpc监听器
 *
 * @author Anker
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
