package com.anker.rpc.core.event.impl;

import com.anker.rpc.core.event.RocEvent;

/**
 *
 */
public class RpcDataUpdateEvent implements RocEvent {

    private Object data;

    public RpcDataUpdateEvent(Object data) {
        this.data = data;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public RocEvent setData(Object data) {
        this.data = data;
        return this;
    }
}
