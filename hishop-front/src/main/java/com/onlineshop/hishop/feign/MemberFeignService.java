package com.onlineshop.hishop.feign;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.front.*;
import com.onlineshop.hishop.pojo.TbMember;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Component
@FeignClient("hishop-sso")
public interface MemberFeignService {

    @RequestMapping(value = "/front/member/geetestInit", method = RequestMethod.GET)
    public String geetestInit();

    @RequestMapping(value = "/front/member/login", method = RequestMethod.POST)
    public Result<Member> login(@RequestBody MemberLoginRegist memberLoginRegist);
    
    @RequestMapping(value = "/front/member/checkLogin", method = RequestMethod.GET)
    public Result<Member> checkLogin(@RequestParam(defaultValue = "") String token);

    @RequestMapping(value = "/front/member/loginOut", method = RequestMethod.GET)
    public Result<Object> logout(@RequestParam(defaultValue = "") String token);

    @RequestMapping(value = "/front/member/register", method = RequestMethod.POST)
    public Result<Object> register(@RequestBody MemberLoginRegist memberLoginRegist);

    @RequestMapping(value = "/front/member/active", method = RequestMethod.GET)
    public Result<TbMember> active(@RequestParam String name) ;

    @RequestMapping(value = "/front/member/code", method = RequestMethod.GET)
    public Result<Object> code(@RequestParam String username, @RequestParam String email);

    @RequestMapping(value = "/front/member/forget", method = RequestMethod.POST)
    public Result<Object> forget(@RequestBody MemberForget memberForget);

    @RequestMapping(value = "/front/member/updateEmail", method = RequestMethod.POST)
    public Result<Object> updateEmail(@RequestBody EmailDto emailDto);

//    @RequestMapping(value = "/front/member/imageUpload", method = RequestMethod.POST)
//    public Result<Object> imageUpload(@RequestBody CommonDto common, HttpServletRequest request);
}
