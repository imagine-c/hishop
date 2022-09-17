package com.onlineshop.hishop.controller;


import com.onlineshop.hishop.annotation.SystemControllerLog;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.exception.UserLockException;
import com.onlineshop.hishop.pojo.TbUser;
import com.onlineshop.hishop.service.UserService;
import com.onlineshop.hishop.utils.GeetestLib;
import com.onlineshop.hishop.utils.ResultUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Log4j2
@RequestMapping("/admin")
@PropertySource("classpath:conf/redis.yml")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${redis.timeout}")
    private int timeout; // 过期时间

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @SystemControllerLog(description="登录系统")
    public Result<TbUser> login(@RequestParam String username, @RequestParam String password,
                                @RequestParam String challenge, @RequestParam String validate,
                                @RequestParam String seccode){

        //极验验证
        GeetestLib gtSdk = new GeetestLib(GeetestLib.id, GeetestLib.key,GeetestLib.newfailback);

        //从session中获取gt-server状态
//        int gt_server_status_code = (Integer) request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);
        int gt_server_status_code = Integer.parseInt(stringRedisTemplate.opsForValue().get(gtSdk.gtServerStatusSessionKey));
        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();

        int gtResult = 0;

        if (gt_server_status_code == 1) {
            //gt-server正常，向gt-server进行二次验证
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
            log.info(gtResult);
        } else {
            // gt-server非正常情况下，进行failback模式验证
            System.out.println("failback:use your own server captcha validate");
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
            log.info(gtResult);
            System.out.println(gtResult);
        }

        if (gtResult == 1) {
            // 验证成功
            Subject subject = SecurityUtils.getSubject() ;
            //MD5加密
            String md5Pass = DigestUtils.md5DigestAsHex(password.getBytes());
            UsernamePasswordToken Token = new UsernamePasswordToken(username,md5Pass);
            try {
                subject.login(Token);
                TbUser user = (TbUser) subject.getPrincipal();
                String token = subject.getSession().getId().toString();
                user.setToken(token);
                stringRedisTemplate.opsForValue().set(token, user.getUsername(), timeout, TimeUnit.SECONDS);
                return new ResultUtil<TbUser>().setData(user);
            }catch (UnknownAccountException e){
                log.warn("无用户");
                return new ResultUtil<TbUser>().setErrorMsg("用户名错误！");
            }catch (IncorrectCredentialsException e){
                log.warn("密码错误");
                return new ResultUtil<TbUser>().setErrorMsg(null, "密码错误！");
            }catch (UserLockException e){
                log.warn("已锁定");
                return new ResultUtil<TbUser>().setErrorMsg(null, "已锁定");
            }

        }
        else {
            // 验证失败
            return new ResultUtil<TbUser>().setErrorMsg("验证失败");
        }
    }

    @RequestMapping(value = "/user/logout",method = RequestMethod.GET)
    public Result<Object> logout(){

        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/user/userInfo",method = RequestMethod.GET)
    public Result<TbUser> getUserInfo(){

        TbUser user = (TbUser) SecurityUtils.getSubject().getPrincipal();
        TbUser tbUser=userService.getUserByUsername(user.getUsername());
        tbUser.setPassword(null);
        return new ResultUtil<TbUser>().setData(tbUser);
    }


    @GetMapping("/user/isPermitted")
    public boolean isPermitted(@RequestParam String requestURI, @RequestParam String Authorization){

        boolean permitted = SecurityUtils.getSubject().isPermitted(requestURI);
        return permitted;
    }

}
