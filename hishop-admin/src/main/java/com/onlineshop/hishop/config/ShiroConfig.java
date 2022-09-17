//package com.onlineshop.hishop.config;
//
//import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
//import com.onlineshop.hishop.pojo.TbShiroFilter;
//import com.onlineshop.hishop.com.onlineshop.hishop.service.SystemService;
//import com.onlineshop.hishop.shiro.MyPermissionFilter;
//import com.onlineshop.hishop.shiro.MyRealm;
//import lombok.extern.log4j.Log4j2;
//import org.apache.shiro.config.Ini;
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.util.CollectionUtils;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.web.filter.DelegatingFilterProxy;
//
//import javax.servlet.Filter;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//@Configuration
//@Log4j2
//public class ShiroConfig {
//
//    @Autowired
//    private SystemService systemService;
//
//    //配置自定义Realm
//    @Bean
//    @DependsOn("lifecycleBeanPostProcessor")
//    public MyRealm myRealm(){
//        return new MyRealm();
//    }
//
//    //安全管理器
//    @Bean
//    public DefaultWebSecurityManager securityManager(){
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(myRealm());
//        return securityManager;
//    }
//
//    //权限过滤
//    @Bean
//    public MyPermissionFilter perms(){
//        return new MyPermissionFilter();
//    }
//
//
//    @Bean(name = "shiroFilter")
//    public ShiroFilterFactoryBean shiroFilter(){
//        log.info("shiroFilter");
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new  ShiroFilterFactoryBean();
//        shiroFilterFactoryBean.setSecurityManager(securityManager());
//        shiroFilterFactoryBean.setLoginUrl("/login");
//        shiroFilterFactoryBean.setSuccessUrl("/");
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
//        log.info("权限过滤");
//        Map<String,Filter> map = new HashMap<>();
//        map.put("perms",new MyPermissionFilter());
//        map.put("roles",new MyPermissionFilter());
//        shiroFilterFactoryBean.setFilters(map);
//        //数据库动态权限
//        List<TbShiroFilter> list = systemService.getShiroFilter();
//        /**
//         * 配置中的过滤链
//         */
//        String definitions = "";
//        for(TbShiroFilter tbShiroFilter : list){
//            //字符串拼接权限
//            definitions = definitions + tbShiroFilter.getName() + " = "+tbShiroFilter.getPerms()+"\n";
//        }
//        log.info(definitions);
//        //从配置文件加载权限配置
//        Ini ini = new Ini();
//        ini.load(definitions);
//        Ini.Section section = ini.getSection("urls");
//        if (CollectionUtils.isEmpty(section)) {
//            section = ini.getSection("");
//        }
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(section);
//        LinkedHashMap<String, String> filterChainDefinitionMap=new LinkedHashMap<>();
//        filterChainDefinitionMap.put("/css/**", "anon"); //匿名访问静态资源
//        filterChainDefinitionMap.put("/js/**", "anon");
//        filterChainDefinitionMap.put("/fonts/**", "anon"); //匿名访问静态资源
////        filterChainDefinitionMap.put("/lib/**", "anon");
//        filterChainDefinitionMap.put("/icon/**", "anon"); //匿名访问静态资源
//        filterChainDefinitionMap.put("/lib/**", "anon");
//        filterChainDefinitionMap.put("/static/**", "anon");
//        filterChainDefinitionMap.put("/temp/**", "anon");
//        filterChainDefinitionMap.put("/upload/**", "anon");
//        return shiroFilterFactoryBean;
//    }
//
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
//        return authorizationAttributeSourceAdvisor;
//    }
//
//    @Bean
//    public ShiroDialect shiroDialect() {
//        return new ShiroDialect();
//    }
//
//    @Bean
//    public FilterRegistrationBean delegatingFilterProxy(){
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
//        proxy.setTargetFilterLifecycle(true);
//        proxy.setTargetBeanName("shiroFilter");
//        filterRegistrationBean.setFilter(proxy);
//        return filterRegistrationBean;
//    }
//}
