package com.onlineshop.hishop.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * @description: 忽略授权认证过滤器工程类，主要用于对不需要认证的开发接口跳过全局认证
 * @author: Zhaotianyi
 * @time: 2022/1/21 10:08
 */

@Component
public class AuthFilterFactor extends AbstractGatewayFilterFactory<AuthFilterFactor.Config> {

    public final static String ATTRIBUTE_AUTH_GLOBAL_FILTER = "@AuthFilter";

    public AuthFilterFactor() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return this::filter;
    }

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put(ATTRIBUTE_AUTH_GLOBAL_FILTER, true);
        return chain.filter(exchange);
    }

    public static class Config {

    }

    @Override
    public String name() {
        return "AuthFilter";
    }

}
