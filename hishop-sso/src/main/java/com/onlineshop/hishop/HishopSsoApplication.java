package com.onlineshop.hishop;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableDubbo
@MapperScan("com.onlineshop.hishop.mapper")
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds= 1800) //session过期时间30分钟
@SpringBootApplication
public class HishopSsoApplication {

	public static void main(String[] args) {
		SpringApplication.run(HishopSsoApplication.class, args);
	}

}
