package com.onlineshop.hishop.feign;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.pojo.TbUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Component
@FeignClient(value = "hishop-auth")
public interface AuthFeignService {

    @RequestMapping(value = "/admin/user/login",method = RequestMethod.POST)
    public Result<TbUser> login(@RequestParam String username, @RequestParam String password,
                                @RequestParam String challenge, @RequestParam String validate,
                                @RequestParam String seccode);

    @RequestMapping(value = "/admin/user/logout",method = RequestMethod.GET)
    public Result<Object> logout();

    @RequestMapping(value = "/admin/user/userInfo",method = RequestMethod.GET)
    public Result<TbUser> getUserInfo();

}
