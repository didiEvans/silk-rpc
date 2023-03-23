package com.anker.rpc.core.config;

/**
 *  server config for rpc framework
 *
 * @author Anker
 */
public class ServerConfig {

    private Integer serverPort;

    private String registerAddr;

    private String registerType;

    private String applicationName;
    /**
     * 服務端序列化配置
     */
    private String serverSerialize;

    private Integer serverBizThreadNums;

    private Integer serverQueueSize;
    /**
     * 限制服务器最大能接受的数据包体积
     */
    private Integer maxServerRequestData;

    private Integer maxConnections;


    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getRegisterAddr() {
        return registerAddr;
    }

    public void setRegisterAddr(String registerAddr) {
        this.registerAddr = registerAddr;
    }

    public String getRegisterType() {
        return registerType;
    }

    public void setRegisterType(String registerType) {
        this.registerType = registerType;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getServerSerialize() {
        return serverSerialize;
    }

    public void setServerSerialize(String serverSerialize) {
        this.serverSerialize = serverSerialize;
    }

    public Integer getServerBizThreadNums() {
        return serverBizThreadNums;
    }

    public void setServerBizThreadNums(Integer serverBizThreadNums) {
        this.serverBizThreadNums = serverBizThreadNums;
    }

    public Integer getServerQueueSize() {
        return serverQueueSize;
    }

    public void setServerQueueSize(Integer serverQueueSize) {
        this.serverQueueSize = serverQueueSize;
    }

    public Integer getMaxServerRequestData() {
        return maxServerRequestData;
    }

    public void setMaxServerRequestData(Integer maxServerRequestData) {
        this.maxServerRequestData = maxServerRequestData;
    }

    public Integer getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(Integer maxConnections) {
        this.maxConnections = maxConnections;
    }
}
