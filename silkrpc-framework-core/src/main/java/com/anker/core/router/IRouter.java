package com.anker.core.router;

import com.anker.core.registy.URL;
import com.anker.core.wrapper.ChannelFutureWrapper;

/**
 * 路由接口
 *
 * @author YBin
 */
public interface IRouter {



    /**
     * 刷新路由数组
     *
     * @param selector  选择器
     */
    void refreshRouterArr(Selector selector);

    /**
     * 获取到请求到连接通道
     *
     * @return channel选择器
     */
    ChannelFutureWrapper select(Selector selector);

    /**
     * 更新权重信息
     *
     * @param url 地址
     */
    void updateWeight(URL url);


}
