package com.anker.rpc.spring.starter.configuration;

import com.anker.rpc.annotation.provider.SilkRpcService;
import com.anker.rpc.core.listener.SilkRpcListenerLoader;
import com.anker.rpc.core.server.Server;
import com.anker.rpc.core.server.ServerShutdownHook;
import com.anker.rpc.core.wrapper.ServiceWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * rpc 服务端自动配置
 *
 * @author Anker
 */
public class SilkRpcServerAutoConfiguration implements InitializingBean, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SilkRpcServerAutoConfiguration.class);

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(SilkRpcService.class);
        if (beanMap.size() == 0) {
            //说明当前应用内部不需要对外暴露服务，无需执行下边多余的逻辑
            return;
        }
        //输出banner图案
        printBanner();
        long begin = System.currentTimeMillis();
        Server server = new Server();
        server.initServerConfig();
        SilkRpcListenerLoader iRpcListenerLoader = new SilkRpcListenerLoader();
        iRpcListenerLoader.init();
        for (String beanName : beanMap.keySet()) {
            Object bean = beanMap.get(beanName);
            SilkRpcService iRpcService = bean.getClass().getAnnotation(SilkRpcService.class);
            ServiceWrapper dataServiceServiceWrapper = new ServiceWrapper(bean, iRpcService.group());
            dataServiceServiceWrapper.setServiceToken(iRpcService.serviceToken());
            dataServiceServiceWrapper.setLimit(iRpcService.limit());
            server.exportService(dataServiceServiceWrapper);
            LOGGER.info(">>>>>>>>>>>>>>> [silk_rpc] {} export success! >>>>>>>>>>>>>>> ",beanName);
        }
        long end = System.currentTimeMillis();
        ServerShutdownHook.registryShutdownHook();
        server.startApplication();
        LOGGER.info(" ================== [{}] started success in {}s ================== ",server.getServerConfig().getApplicationName(),((double)end-(double)begin)/1000);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void printBanner(){
        System.out.println();
        System.out.println("==============================================");
        System.out.println("|||---------- Silk-Rpc Starting Now! ----------|||");
        System.out.println("==============================================");
        System.out.println("version: 1.0.0");
        System.out.println();
    }
}