package com.anker.core.config;

/**
 * 客户端配置
 *
 * @author Anker
 */
public class ClientConfig {
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 注册地址
     */
    private String registerAddr;
    /**
     * 代理类型
     */
    private String proxyType;
    /**
     * 负载均衡策略
     */
    private String routerStrategy;
    /**
     * 客戶端序列化策略
     */
    private String clientSerialize;
    /**
     * 注册中心类型
     */
    private String registerType;
    /**
     * 超时时间
     */
    private Integer timeOut;
    /**
     * 客户端最大响应数据体积
     */
    private Integer maxServerRespDataSize;

    public Integer getMaxServerRespDataSize() {
        return maxServerRespDataSize;
    }

    public void setMaxServerRespDataSize(Integer maxServerRespDataSize) {
        this.maxServerRespDataSize = maxServerRespDataSize;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public String getRegisterType() {
        return registerType;
    }

    public void setRegisterType(String registerType) {
        this.registerType = registerType;
    }

    public String getRouterStrategy() {
        return routerStrategy;
    }

    public String getClientSerialize() {
        return clientSerialize;
    }

    public void setClientSerialize(String clientSerialize) {
        this.clientSerialize = clientSerialize;
    }

    public void setRouterStrategy(String routerStrategy) {
        this.routerStrategy = routerStrategy;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getRegisterAddr() {
        return registerAddr;
    }

    public void setRegisterAddr(String registerAddr) {
        this.registerAddr = registerAddr;
    }

    public String getProxyType() {
        return proxyType;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }
}
