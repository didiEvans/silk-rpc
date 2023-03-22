package com.anker.rpc.annotation.spi;

import java.lang.annotation.*;

/**
 * @author Anker
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SPI {

    String value() default "";
}