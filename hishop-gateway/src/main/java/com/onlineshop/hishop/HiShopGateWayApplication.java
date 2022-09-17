package com.onlineshop.hishop;


import com.onlineshop.hishop.utils.AutowiredBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.stream.Collectors;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.onlineshop.hishop.mapper")
public class HiShopGateWayApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(HiShopGateWayApplication.class, args);
        AutowiredBean.setApplicationContext(run);
    }

    /**
     * Spring-GateWay使用的WebFlux架构。
     * HttpMessage转换器，用于对Feign的请求转换。
     * @param converters
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

}
