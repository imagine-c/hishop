package com.onlineshop.hishop.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * 自动注入Bean根据
 * 在Spring-GateWay中自动注入不稳定,所以需要手动注入获取Bean
 */

public class AutowiredBean{

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (AutowiredBean.applicationContext == null) {
            AutowiredBean.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}

