package com.anker.core.event.impl;

import com.anker.core.event.SilkRpcEvent;

/**
 *
 */
public class RpcDataUpdateEvent implements SilkRpcEvent {

    private Object data;

    public RpcDataUpdateEvent(Object data) {
        this.data = data;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public SilkRpcEvent setData(Object data) {
        this.data = data;
        return this;
    }
}
