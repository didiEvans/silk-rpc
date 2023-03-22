package com.anker.rpc.core.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.anker.rpc.core.serialize.SerializeFactory;

/**
 * fastjson序列化工厂
 *
 * @author Anker
 */
public class FastJsonSerializeFactory implements SerializeFactory {

    @Override
    public <T> byte[] serialize(T t) {
        String jsonStr = JSON.toJSONString(t);
        return jsonStr.getBytes();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(new String(data),clazz);
    }
}
