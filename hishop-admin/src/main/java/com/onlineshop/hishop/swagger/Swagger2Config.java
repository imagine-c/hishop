//package com.onlineshop.hishop.swagger;
//
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.com.onlineshop.hishop.service.ApiInfo;
//import springfox.documentation.com.onlineshop.hishop.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
///**
// */
//@Configuration  //让Spring来加载该类配置
//@Log4j2
//@PropertySource("classpath:conf/admin.yml")
//public class Swagger2Config {
//    @Value("${host}")
//    private String host;
//    @Value("${port}")
//    private int port;
//    @Value("${version}")
//    private String version;
//
//    @Bean
//    public Docket createRestApi() {
//        log.info("开始加载Swagger2...");
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo()).select()
//                //扫描指定包中的swagger注解
//                .apis(RequestHandlerSelectors.basePackage("com.onlineshop.hishop.controller"))
//                //扫描所有有注解的api，用这种方式更灵活
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("HiShop Api Documentation")
//                .description("HiShop商城管理后台API接口文档")
//                .termsOfServiceUrl("#")
//                .contact(new Contact("HiShop", "http://"+host+":"+port+"/swagger-ui.html#/", "imagine_j@163.com"))
//                .version(version)
//                .build();
//    }
//}
