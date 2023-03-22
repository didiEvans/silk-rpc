package com.anker.rpc.core.event;

/**
 * zk节点变更时间
 *
 * @author Anker
 */
public interface RocEvent {
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
     * @return {@link RocEvent}
     */
    RocEvent setData(Object data);
}
