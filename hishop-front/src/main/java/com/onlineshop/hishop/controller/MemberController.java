package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.front.*;
import com.onlineshop.hishop.feign.MemberFeignService;
import com.onlineshop.hishop.pojo.TbMember;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(description = "会员注册登录")
@Log4j2
public class MemberController {

    @Autowired
    private MemberFeignService memberFeignService;

    @RequestMapping(value = "/member/geetestInit", method = RequestMethod.GET)
    @ApiOperation(value = "极验初始化")
    public String geetestInit() {
        return memberFeignService.geetestInit();
    }

    @RequestMapping(value = "/member/login", method = RequestMethod.POST)
    @ApiOperation(value = "用户登录")
    public Result<Member> login(@RequestBody MemberLoginRegist memberLoginRegist) {

        return memberFeignService.login(memberLoginRegist);
    }

    @RequestMapping(value = "/member/checkLogin", method = RequestMethod.GET)
    @ApiOperation(value = "判断用户是否登录")
    public Result<Member> checkLogin(@RequestParam(defaultValue = "") String token) {
        return memberFeignService.checkLogin(token);
    }

    @RequestMapping(value = "/member/loginOut", method = RequestMethod.GET)
    @ApiOperation(value = "退出登录")
    public Result<Object> logout(@RequestParam(defaultValue = "") String token) {
        return memberFeignService.logout(token);
    }

    @RequestMapping(value = "/member/register", method = RequestMethod.POST)
    @ApiOperation(value = "发送注册邮箱")
    public Result<Object> register(@RequestBody MemberLoginRegist memberLoginRegist) {
        return memberFeignService.register(memberLoginRegist);
    }

    @RequestMapping(value = "/member/active", method = RequestMethod.GET)
    @ApiOperation(value = "邮箱激活")
    public Result<TbMember> active(@RequestParam String name) {
        return memberFeignService.active(name);
    }

    @RequestMapping(value = "/member/code", method = RequestMethod.GET)
    @ApiOperation(value = "获取验证码")
    public Result<Object> code(@RequestParam String username, @RequestParam String email) {
        return memberFeignService.code(username, email);
    }

    @RequestMapping(value = "/member/forget", method = RequestMethod.POST)
    @ApiOperation(value = "修改密码")
    public Result<Object> forget(@RequestBody MemberForget memberForget) {
        return memberFeignService.forget(memberForget);

    }

    @RequestMapping(value = "/member/updateEmail", method = RequestMethod.POST)
    @ApiOperation(value = "修改邮箱")
    public Result<Object> updateEmail(@RequestBody EmailDto emailDto) {
        return memberFeignService.updateEmail(emailDto);
    }


//    @RequestMapping(value = "/member/imageUpload", method = RequestMethod.POST)
//    @ApiOperation(value = "用户头像上传")
//    public Result<Object> imageUpload(@RequestBody CommonDto common, HttpServletRequest request) {
//        return memberFeignService.imageUpload(common, request);
//    }
}
