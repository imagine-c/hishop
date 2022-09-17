package com.onlineshop.hishop.config;

import com.onlineshop.hishop.shiro.SessionManager;
import com.onlineshop.hishop.shiro.UserRealm;
import com.onlineshop.hishop.utils.AuthUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@Log4j2
public class ShiroConfig {

    @Bean
    public RedisConfig myRedisConfig(){
        return new RedisConfig();
    }

    //配置自定义Realm
    @Bean
    public UserRealm realm() {
        return new UserRealm();
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter() {
        log.warn("shiroFilter");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());

        return shiroFilterFactoryBean;
    }

    //权限管理，配置主要是Realm的管理认证
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm());
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
//        redisCacheManager.setKeyPrefix("SPRINGBOOT_CACHE:");   //设置前缀
        return redisCacheManager;
    }

    @Bean
    public SimpleCookie cookie(){
        SimpleCookie simpleCookie = new SimpleCookie(AuthUtils.token);
        simpleCookie.setPath("/");
        simpleCookie.setHttpOnly(false);
        return simpleCookie;
    }


    /**
     * Session User
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public SessionManager sessionManager() {

        SessionManager sessionManager = new SessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
//        sessionManager.setSessionIdCookieEnabled(false);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionIdCookie(cookie());
        sessionManager.setGlobalSessionTimeout(myRedisConfig().getTimeout());//单位毫秒
        return sessionManager;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(myRedisConfig().getHost()+":"+myRedisConfig().getPort());
       // redisManager.setJedisPool(redisPoolFactory);
       // redisManager.setPassword(password);
        redisManager.setTimeout(myRedisConfig ().getTimeout());
        return redisManager;
    }

    @Bean
    public JavaUuidSessionIdGenerator sessionIdGenerator(){
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setSessionIdGenerator(sessionIdGenerator());
//        redisSessionDAO.setKeyPrefix("SPRINGBOOT_SESSION:");
        //redisSessionDAO.setExpire(3600);//设置过期时间，单位s
        return redisSessionDAO;
    }

    /***
     * 授权所用配置
     *
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        //defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }
    /**
     * Shiro生命周期处理器
     * 此方法需要用static作为修饰词，否则无法通过@Value()注解的方式获取配置文件的值
     *
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


//    @Bean
//    public FilterRegistrationBean replaceTokenFilter(){
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setDispatcherTypes(DispatcherType.REQUEST);
//        registration.setFilter(new CorsFilter());
//        registration.addUrlPatterns("/*");
//        registration.setName("CrosFilter");
//        registration.setOrder(1);
//        return registration;
//    }

}
