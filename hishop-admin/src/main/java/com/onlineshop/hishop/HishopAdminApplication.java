package com.onlineshop.hishop;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDubbo
@EnableScheduling
@MapperScan("com.onlineshop.hishop.mapper")
@EnableDubboConfig
@SpringBootApplication
public class HishopAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(HishopAdminApplication.class, args);
	}

}
