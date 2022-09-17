package com.onlineshop.hishop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds= 1800) //session过期时间30分钟
@MapperScan("com.onlineshop.hishop.mapper")
@EnableDiscoveryClient
public class HiShopContentApplication {
    public static void main(String[] args) {
        SpringApplication.run(HiShopContentApplication.class, args);
    }
}
