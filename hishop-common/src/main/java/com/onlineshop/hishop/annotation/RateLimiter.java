package com.onlineshop.hishop.annotation;

import java.lang.annotation.*;

/**
 * 限流注解
 * @author imagine
 */
@Target(ElementType.METHOD)//作用于方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    int limit() default 5;
    int timeout() default 1000;
}
