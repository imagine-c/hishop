package com.onlineshop.hishop.config;

import com.onlineshop.hishop.pojo.TbShiroFilter;
import com.onlineshop.hishop.service.SystemService;
import com.onlineshop.hishop.service.impl.SystemServiceImpl;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GateWayConfig {

    @Bean
    public SystemService service(){
        SystemServiceImpl service = new SystemServiceImpl();
        return service;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder,
                                           AuthFilterFactor authFilterFactor,
                                           PermFilterFactor permFilterFactor,
                                           SystemService service) {

        List<TbShiroFilter> list = service.getShiroFilter();
        System.out.println(list.size());
        RouteLocatorBuilder.Builder routes = builder.routes();
        AuthFilterFactor.Config authFilterConfig = new AuthFilterFactor.Config();
        PermFilterFactor.Config permFilterConfig = new PermFilterFactor.Config();

        for(TbShiroFilter filter : list){
            if (filter.getPerms().contains("anon")){
                routes.route(r ->r.path(filter.getName())
                        .uri("lb://hishop-admin-web/"));
            }
            if (filter.getPerms().contains("authc")){
                routes.route(r -> r.path(filter.getName())
                        .filters(f -> f.filter(authFilterFactor.apply(authFilterConfig)))
                        .uri("lb://hishop-admin-web/")
                );
            }
            if (filter.getPerms().contains("perm")){
                routes.route(r -> r.path(filter.getName())
                        .filters(f -> f.filter(permFilterFactor.apply(permFilterConfig)))
                        .uri("lb://hishop-admin-web/")
                );
            }
        }





        return routes.build();
    }

}
