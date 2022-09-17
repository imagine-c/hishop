package com.onlineshop.hishop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@MapperScan("com.onlineshop.hishop.mapper")
@EnableDiscoveryClient
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds= 1800) //session过期时间30分钟
public class HiShopSsoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HiShopSsoApplication.class, args);
    }

}
