package com.anker.rpc.core.server;

import com.anker.rpc.core.event.impl.RpcDestroyEvent;
import com.anker.rpc.core.listener.SilkRpcListenerLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端关停勾连函数
 *
 * @author Anker
 */
public class ServerShutdownHook {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerShutdownHook.class);

    /**
     * 注册一个shutdownHook的钩子，当jvm进程关闭的时候触发
     */
    public static void registryShutdownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                SilkRpcListenerLoader.sendSyncEvent(new RpcDestroyEvent("destroy"));
                LOGGER.info("server destruction");
            }
        }));
    }

}
