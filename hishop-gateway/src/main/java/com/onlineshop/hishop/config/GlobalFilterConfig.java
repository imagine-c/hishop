package com.onlineshop.hishop.config;

import com.onlineshop.hishop.feign.AuthorizeClient;
import com.onlineshop.hishop.utils.AuthUtils;
import com.onlineshop.hishop.utils.AutowiredBean;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * @description: 全局过滤器配置
 * 主要用于授权、日志记录等功能
 * @author: imagine
 * @time: 2022/1/20 14:01
 */
@Configuration
@Log4j2
public class GlobalFilterConfig {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public final static String ATTRIBUTE_AUTH_GLOBAL_FILTER = "@AuthFilter";
    public final static String ATTRIBUTE_PERM_GLOBAL_FILTER = "@PermFilter";

    /**
     * 授权拦截
     */
    @Bean
    @Order(-1)
    public GlobalFilter auth() {
        return (exchange, chain) -> {
            // 获取请求微服务的请求路径
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().toString();
            // 获取请求头部的Token
            String token = request.getHeaders().getFirst(AuthUtils.token);
            log.warn(token);
            long expire = -2L;
            // 过期时间
            // 不存在返回-2，未设置过期时间返回-1，返回过期时间
            if (token != null){
                expire = redisTemplate.getExpire(token, TimeUnit.SECONDS);
            }
            /* 授权放行检查 */
            if (exchange.getAttribute(ATTRIBUTE_AUTH_GLOBAL_FILTER) == null && exchange.getAttribute(ATTRIBUTE_PERM_GLOBAL_FILTER) == null) {
                log.warn("无需认证授权");
                if (!"/login".equals(path)){
                    return chain.filter(exchange);
                }
                else {
                    if (expire > 0){
                        ServerHttpResponse response = exchange.getResponse();
                        response.setStatusCode(HttpStatus.FOUND);
                        response.getHeaders().set("Location", "/");
                        return exchange.getResponse().setComplete();
                    }else {
                        return chain.filter(exchange);
                    }
                }
            } else {
                log.info("进行授权认证操作...");
                // 需要认证
                if (exchange.getAttribute(ATTRIBUTE_AUTH_GLOBAL_FILTER) != null){
                    log.warn(path);
                    if (expire > 0) {
                        return chain.filter(exchange);
                    }
                    log.warn("没有认证");
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.FOUND);
                    response.getHeaders().set("Location", "/login");
                    return exchange.getResponse().setComplete();
                }
                if (expire == -2) {
                    throw new ServiceException("Token 不存在");
                }else if (expire == 0){
                    throw new ServiceException("Token 过期");
                }
                AuthorizeClient authorizeClient = AutowiredBean.getBean(AuthorizeClient.class);
                /** 进行授权验证操作 */
                boolean authResult = authorizeClient.isPermitted(path, token);
                if (!authResult) {
                    throw new ServiceException("未授权，无法访问！");
                }
                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    log.info("授权认证通过");
                }));
            }
        };
    }
}
