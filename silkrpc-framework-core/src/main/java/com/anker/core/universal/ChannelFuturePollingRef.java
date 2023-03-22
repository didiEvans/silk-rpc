package com.anker.core.universal;

import com.anker.core.router.Selector;
import com.anker.core.wrapper.ChannelFutureWrapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * channel轮询引用
 * @author Anker
 */
public class ChannelFuturePollingRef {

    private final Map<String, AtomicLong> referenceMap = new ConcurrentHashMap<>();

    public ChannelFutureWrapper getChannelFutureWrapper(Selector selector) {
        AtomicLong referCount = referenceMap.get(selector.getProviderServiceName());
        if (referCount == null) {
            referCount = new AtomicLong(0);
            referenceMap.put(selector.getProviderServiceName(), referCount);
        }
        ChannelFutureWrapper[] arr = selector.getChannelFutureWrappers();
        long i = referCount.getAndIncrement();
        int index = (int) (i % arr.length);
        return arr[index];
    }

}
