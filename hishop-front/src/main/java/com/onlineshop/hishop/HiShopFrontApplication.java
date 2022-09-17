package com.onlineshop.hishop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(exclude= DataSourceAutoConfiguration.class)
@EnableFeignClients
@EnableDiscoveryClient
public class HiShopFrontApplication {

    public static void main(String[] args) {
//        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(HiShopFrontApplication.class, args);
    }

}
