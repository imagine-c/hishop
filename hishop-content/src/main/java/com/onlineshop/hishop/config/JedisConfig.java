package com.onlineshop.hishop.config;

import com.onlineshop.hishop.jedis.JedisClientPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;

@Primary
@Configuration
@PropertySource("classpath:conf/content.properties")
public class JedisConfig {

    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private int port;

    @Bean
    public JedisPool jedisPool(){
        return new JedisPool(host,port);
    }

    @Bean
    public JedisClientPool jedisClientPool(){
        JedisClientPool jedisClientPool = new JedisClientPool();
        jedisClientPool.setJedisPool(jedisPool());
        return jedisClientPool;
    }
}
