package com.anker.rpc.annotation.consumer;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * rpc 服务提供方注解
 *
 * @author Anker
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SilkRpcReference {

    //服务分组
    String group() default "default";
    //服务的令牌校验
    String serviceToken() default "";
    //服务调用的超时时间
    int timeOut() default 3000;
    //重试次数
    int retry() default 1;
    //是否异步调用
    boolean async() default false;

}
