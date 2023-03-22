package com.anker.rpc.annotation.provider;

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
@Component
public @interface SilkRpcService {

    int limit() default 0;

    String group() default "default";

    String serviceToken() default "";

}
