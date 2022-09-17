package com.onlineshop.hishop.config;

import com.onlineshop.hishop.utils.AuthUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: Feign配置, 现阶段主要解决Feign远程调用丢失Request的Header问题
 * @author: imagine
 * @time: 2022/2/21 15:46
 */
@Configuration
@Log4j2
public class FeignConfig {

    /**
     * 解决fein远程调用丢失请求头
     * @return
     */
    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 1、RequestContextHolder 拿到当前的请求
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    // 原始请求 页面发起的老请求
                    HttpServletRequest request = attributes.getRequest();
                    if (request != null) {
                        // 获取原始请求的头数据 cookie
                        String token = request.getHeader(AuthUtils.token);
                        // 给feign重新生成的新请求设置请求头cookie
                        log.warn("token：" +token);
                        template.header(AuthUtils.token, token);
                    }
                }
            }
        };
    }
}
