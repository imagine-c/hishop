package com.onlineshop.hishop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:conf/redis.yml")
public class RedisConfig {

    /**
     * 注入配置文件属性
     */
    @Value("${redis.host}")
    private String host; // 地址
    @Value("${redis.port}")
    private int port; // 端口号
    @Value("${redis.timeout}")
    private int timeout; // 过期时间
//    @Value("${redis.password}")
//    private String password; // 密码

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

}
