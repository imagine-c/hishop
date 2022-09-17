package com.onlineshop.hishop;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@SpringBootApplication(exclude= DataSourceAutoConfiguration.class)
@EnableRedisHttpSession(maxInactiveIntervalInSeconds= 1800) //session过期时间30分钟
@EnableFeignClients
@EnableDiscoveryClient
public class HiShopAdminWebApplication {

	public static void main(String[] args) {
//		System.setProperty("es.set.netty.runtime.available.processors","false");
		SpringApplication.run(HiShopAdminWebApplication.class, args);
	}

}
