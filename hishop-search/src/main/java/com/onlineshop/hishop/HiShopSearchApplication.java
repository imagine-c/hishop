package com.onlineshop.hishop;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@MapperScan("com.onlineshop.hishop.mapper")
@EnableDiscoveryClient
public class HiShopSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(HiShopSearchApplication.class, args);
    }
}
