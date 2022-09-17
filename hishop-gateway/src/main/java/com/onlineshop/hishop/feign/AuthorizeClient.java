package com.onlineshop.hishop.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description: 调用授权微服务，验证连接是否授权
 * @author: imagine
 * @time: 2022/1/20 11:17
 */
@Component
@FeignClient(value = "hishop-auth")
public interface AuthorizeClient {
    @GetMapping("/admin/user/isPermitted")
    boolean isPermitted(@RequestParam String requestURI, @RequestParam String Authorization);
}
