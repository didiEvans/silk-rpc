package com.anker.rpc.core.config;

import java.io.IOException;

/**
 * 配置属性引导类
 *
 * @author Anker
 */
public class PropertiesBootstrap {

    private volatile boolean configIsReady;

    public static final String SERVER_PORT = "silk-rpc.serverPort";
    public static final String REGISTER_ADDRESS = "silk-rpc.registerAddr";
    public static final String REGISTER_TYPE = "silk-rpc.registerType";
    public static final String APPLICATION_NAME = "silk-rpc.applicationName";
    public static final String PROXY_TYPE = "silk-rpc.proxyType";
    public static final String ROUTER_TYPE = "silk-rpc.router";
    public static final String SERVER_SERIALIZE_TYPE = "silk-rpc.serverSerialize";
    public static final String CLIENT_SERIALIZE_TYPE = "silk-rpc.clientSerialize";
    public static final String CLIENT_DEFAULT_TIME_OUT = "silk-rpc.client.default.timeout";
    public static final String SERVER_BIZ_THREAD_NUMS = "silk-rpc.server.biz.thread.nums";
    public static final String SERVER_QUEUE_SIZE = "silk-rpc.server.queue.size";
    public static final String SERVER_MAX_CONNECTION = "silk-rpc.server.max.connection";
    public static final String SERVER_MAX_DATA_SIZE = "silk-rpc.server.max.data.size";
    public static final String CLIENT_MAX_DATA_SIZE = "silk-rpc.client.max.data.size";

    public static ServerConfig loadServerConfigFromLocal() {
        try {
            PropertiesLoader.loadConfiguration();
        } catch (IOException e) {
            throw new RuntimeException("loadServerConfigFromLocal fail,e is {}", e);
        }
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setServerPort(PropertiesLoader.getPropertiesIntegerDefault(SERVER_PORT, DefaultRpcConfigProperties.DEFAULT_SERVER_PORT));
        serverConfig.setApplicationName(PropertiesLoader.getPropertiesStrDefault(APPLICATION_NAME, DefaultRpcConfigProperties.DEFAULT_PROVIDER_APPLICATION_NAME));
        serverConfig.setRegisterAddr(PropertiesLoader.getPropertiesStrDefault(REGISTER_ADDRESS, DefaultRpcConfigProperties.DEFAULT_REGISTER_ADDR));
        serverConfig.setRegisterType(PropertiesLoader.getPropertiesStrDefault(REGISTER_TYPE, DefaultRpcConfigProperties.DEFAULT_REGISTER_TYPE));
        serverConfig.setServerSerialize(PropertiesLoader.getPropertiesStrDefault(SERVER_SERIALIZE_TYPE, DefaultRpcConfigProperties.JDK_SERIALIZE_TYPE));
        serverConfig.setServerBizThreadNums(PropertiesLoader.getPropertiesIntegerDefault(SERVER_BIZ_THREAD_NUMS, DefaultRpcConfigProperties.DEFAULT_THREAD_NUMS));
        serverConfig.setServerQueueSize(PropertiesLoader.getPropertiesIntegerDefault(SERVER_QUEUE_SIZE, DefaultRpcConfigProperties.DEFAULT_QUEUE_SIZE));
        serverConfig.setMaxConnections(PropertiesLoader.getPropertiesIntegerDefault(SERVER_MAX_CONNECTION, DefaultRpcConfigProperties.DEFAULT_MAX_CONNECTION_NUMS));
        serverConfig.setMaxServerRequestData(PropertiesLoader.getPropertiesIntegerDefault(SERVER_MAX_DATA_SIZE, DefaultRpcConfigProperties.SERVER_DEFAULT_MSG_LENGTH));
        return serverConfig;
    }

    public static ClientConfig loadClientConfigFromLocal() {
        try {
            PropertiesLoader.loadConfiguration();
        } catch (IOException e) {
            throw new RuntimeException("loadClientConfigFromLocal fail,e is {}", e);
        }
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setApplicationName(PropertiesLoader.getPropertiesStrDefault(APPLICATION_NAME, DefaultRpcConfigProperties.DEFAULT_CONSUMER_APPLICATION_NAME));
        clientConfig.setRegisterAddr(PropertiesLoader.getPropertiesStrDefault(REGISTER_ADDRESS, DefaultRpcConfigProperties.DEFAULT_REGISTER_ADDR));
        clientConfig.setRegisterType(PropertiesLoader.getPropertiesStrDefault(REGISTER_TYPE, DefaultRpcConfigProperties.DEFAULT_REGISTER_TYPE));
        clientConfig.setProxyType(PropertiesLoader.getPropertiesStrDefault(PROXY_TYPE, DefaultRpcConfigProperties.JDK_PROXY_TYPE));
        clientConfig.setRouterStrategy(PropertiesLoader.getPropertiesStrDefault(ROUTER_TYPE, DefaultRpcConfigProperties.RANDOM_ROUTER_TYPE));
        clientConfig.setClientSerialize(PropertiesLoader.getPropertiesStrDefault(CLIENT_SERIALIZE_TYPE, DefaultRpcConfigProperties.JDK_SERIALIZE_TYPE));
        clientConfig.setTimeOut(PropertiesLoader.getPropertiesIntegerDefault(CLIENT_DEFAULT_TIME_OUT, DefaultRpcConfigProperties.DEFAULT_TIMEOUT));
        clientConfig.setMaxServerRespDataSize(PropertiesLoader.getPropertiesIntegerDefault(CLIENT_MAX_DATA_SIZE, DefaultRpcConfigProperties.CLIENT_DEFAULT_MSG_LENGTH));
        return clientConfig;
    }


}
