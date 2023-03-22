package com.anker.rpc.core.config;

/**
 * 默认rpc配置属性
 *
 * @author Anker
 */
public interface DefaultRpcConfigProperties {

    String DEFAULT_PROPERTIES_FILE = "META-INF/silk-rpc.properties";

    String JDK_PROXY_TYPE = "jdk";

    String JAVASSIST_PROXY_TYPE = "javassist";

    String RANDOM_ROUTER_TYPE = "random";

    String ROTATE_ROUTER_TYPE = "rotate";

    String JDK_SERIALIZE_TYPE = "jdk";

    String FAST_JSON_SERIALIZE_TYPE = "fastJson";

    String HESSIAN2_SERIALIZE_TYPE = "hessian2";

    String KRYO_SERIALIZE_TYPE = "kryo";

    String DEFAULT_PROVIDER_APPLICATION_NAME = "irpc_provider";

    String DEFAULT_CONSUMER_APPLICATION_NAME = "ircp_consumer";

    String DEFAULT_REGISTER_ADDR = "localhost:2181";

    String DEFAULT_REGISTER_TYPE = "zookeeper";

    Integer DEFAULT_SERVER_PORT = 9093;

    Integer DEFAULT_TIMEOUT = 3000;

    Integer DEFAULT_THREAD_NUMS = 256;

    Integer DEFAULT_QUEUE_SIZE = 512;

    Integer DEFAULT_MAX_CONNECTION_NUMS = DEFAULT_THREAD_NUMS + DEFAULT_QUEUE_SIZE;

    String DEFAULT_DECODE_CHAR = "$_i0#Xsop1_$";

    int SERVER_DEFAULT_MSG_LENGTH = 1024 * 10;

    int CLIENT_DEFAULT_MSG_LENGTH = 1024 * 10;

}
