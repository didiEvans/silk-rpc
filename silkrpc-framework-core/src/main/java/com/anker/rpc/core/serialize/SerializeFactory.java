package com.anker.rpc.core.serialize;


/**
 * 序列化工厂
 *
 * @author Anker
 */
public interface SerializeFactory {
    /**
     * 序列化
     * @param t
     * @param <T>
     * @return
     */
    <T> byte[] serialize(T t);

    /**
     * 反序列化
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] data, Class<T> clazz);
}
