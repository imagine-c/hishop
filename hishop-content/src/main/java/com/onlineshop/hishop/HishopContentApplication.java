package com.onlineshop.hishop;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@MapperScan("com.onlineshop.hishop.mapper")
@SpringBootApplication
public class HishopContentApplication {

	public static void main(String[] args) {
		SpringApplication.run(HishopContentApplication.class, args);
	}

}
