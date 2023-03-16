package com.anker.core.event;

/**
 * zk节点变更时间
 *
 * @author Anker
 */
public interface SilkRpcEvent {
    /**
     * 获取数据
     *
     * @return {@link Object} 数据结果
     */
    Object getData();

    /**
     * 设置数据
     *
     * @param data  数据
     * @return {@link SilkRpcEvent}
     */
    SilkRpcEvent setData(Object data);
}
