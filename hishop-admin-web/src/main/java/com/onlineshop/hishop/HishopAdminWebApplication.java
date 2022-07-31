package com.onlineshop.hishop;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@EnableDubbo
@EnableRedisHttpSession(maxInactiveIntervalInSeconds= 1800) //session过期时间30分钟
@SpringBootApplication
public class HishopAdminWebApplication {

	public static void main(String[] args) {
		System.setProperty("es.set.netty.runtime.available.processors","false");
		SpringApplication.run(HishopAdminWebApplication.class, args);
	}

}
