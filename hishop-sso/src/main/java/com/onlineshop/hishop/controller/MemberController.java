package com.onlineshop.hishop.controller;

import com.google.gson.Gson;
import com.onlineshop.hishop.domain.GeetInit;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.front.*;

import com.onlineshop.hishop.pojo.TbMember;
import com.onlineshop.hishop.service.LoginService;
import com.onlineshop.hishop.service.RegisterService;
import com.onlineshop.hishop.service.MemberService;
import com.onlineshop.hishop.utils.AddDateMinute;
import com.onlineshop.hishop.utils.GeetestLib;
import com.onlineshop.hishop.utils.ResultUtil;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@Log4j2
@PropertySource("classpath:conf/sso.yml")
@RequestMapping("/front")
public class  MemberController {


    @Autowired
    private LoginService loginService;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Value("${CODE_DATE}")
    private String CODE_DATE;
    @Value("${CODE_PRX}")
    private String CODE_PRX;

    @RequestMapping(value = "/member/geetestInit", method = RequestMethod.GET)
    public String geetestInit() {

        GeetestLib gtSdk = new GeetestLib(GeetestLib.id, GeetestLib.key, GeetestLib.newfailback);

        String resStr = "{}";

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();

        //进行验证预处理
        int gtServerStatus = gtSdk.preProcess(param);

        //将服务器状态设置到redis中
        //request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);
        String key = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(key, gtServerStatus + "",360, TimeUnit.SECONDS);

        resStr = gtSdk.getResponseStr();
        GeetInit geetInit = new Gson().fromJson(resStr, GeetInit.class);
        geetInit.setStatusKey(key);
        return new Gson().toJson(geetInit);
    }

    @RequestMapping(value = "/member/login", method = RequestMethod.POST)
    public Result<Member> login(@RequestBody MemberLoginRegist memberLoginRegist) {

        //极验验证
        GeetestLib gtSdk = new GeetestLib(GeetestLib.id, GeetestLib.key, GeetestLib.newfailback);

        String challenge = memberLoginRegist.getChallenge();
        String validate = memberLoginRegist.getValidate();
        String seccode = memberLoginRegist.getSeccode();

        //从redis中获取gt-server状态
        //int gt_server_status_code = (Integer) request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);
        int gt_server_status_code = Integer.parseInt(redisTemplate.opsForValue().get(memberLoginRegist.getStatusKey()));

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();

        int gtResult = 0;

        if (gt_server_status_code == 1) {
            //gt-server正常，向gt-server进行二次验证
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
            System.out.println(gtResult);
        } else {
            // gt-server非正常情况下，进行failback模式验证
            System.out.println("failback:use your own server captcha validate");
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
            System.out.println(gtResult);
        }

        Member member = new Member();
        if (gtResult == 1) {
            // 验证成功
            log.error("" + gtResult);
            member = loginService.userLogin(memberLoginRegist.getUserName(), memberLoginRegist.getUserPwd());
            log.error(member.toString());
        } else {
            // 验证失败
            member.setState(0);
            member.setMessage("验证失败");
        }
        return new ResultUtil<Member>().setData(member);
    }

    @RequestMapping(value = "/member/checkLogin", method = RequestMethod.GET)
    public Result<Member> checkLogin(@RequestParam(defaultValue = "") String token) {
        Member member = loginService.getUserByToken(token);
        return new ResultUtil<Member>().setData(member);
    }

    @RequestMapping(value = "/member/loginOut", method = RequestMethod.GET)
    public Result<Object> logout(@RequestParam(defaultValue = "") String token) {

        loginService.logout(token);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/member/register", method = RequestMethod.POST)
    public Result<Object> register(@RequestBody MemberLoginRegist memberLoginRegist) {

        //极验验证
        GeetestLib gtSdk = new GeetestLib(GeetestLib.id, GeetestLib.key, GeetestLib.newfailback);

        String challenge = memberLoginRegist.getChallenge();
        String validate = memberLoginRegist.getValidate();
        String seccode = memberLoginRegist.getSeccode();

        //从redis中获取gt-server状态
        //int gt_server_status_code = (Integer) request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);
        int gt_server_status_code = Integer.parseInt(redisTemplate.opsForValue().get(memberLoginRegist.getStatusKey()));

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();

        int gtResult = 0;

        if (gt_server_status_code == 1) {
            //gt-server正常，向gt-server进行二次验证
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
            System.out.println(gtResult);
        } else {
            // gt-server非正常情况下，进行failback模式验证
            System.out.println("failback:use your own server captcha validate");
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
            System.out.println(gtResult);
        }

        if (gtResult == 1) {
            // 验证成功
            int result = registerService.RegisterEmail(memberLoginRegist.getUserName(), memberLoginRegist.getUserPwd(), memberLoginRegist.getEmail());
            if (result == 0) {
                return new ResultUtil<Object>().setErrorMsg("该用户已被注册");
            } else if (result == 1) {
                return new ResultUtil<Object>().setErrorMsg("该邮箱已被占用");
            } else if (result == 2) {
                return new ResultUtil<Object>().setErrorMsg("邮箱发送失败");
            } else if (result == -1) {
                return new ResultUtil<Object>().setErrorMsg("用户名密码不能为空");
            }
            return new ResultUtil<Object>().setData(result);
        } else {
            // 验证失败
            return new ResultUtil<Object>().setErrorMsg("验证失败");
        }
    }

    @RequestMapping(value = "/member/active", method = RequestMethod.GET)
    public Result<TbMember> active(@RequestParam String name) {
        TbMember member = registerService.EmailActive(name);
        return new ResultUtil<TbMember>().setData(member);
    }

    @RequestMapping(value = "/member/code", method = RequestMethod.GET)
    public Result<Object> code(@RequestParam String username, @RequestParam String email) {
        TbMember member = memberService.getByUserName(username);
        if (member == null)
            return new ResultUtil<Object>().setErrorMsg("账号不存在");
        if (!member.getEmail().equals(email))
            return new ResultUtil<Object>().setErrorMsg("账号邮箱不匹配");
        if (member.getState() == 0)
            return new ResultUtil<Object>().setErrorMsg("账号未激活，无法修改密码");
        if (redisTemplate.hasKey(CODE_DATE)) {
            Long diff = Long.parseLong(redisTemplate.opsForValue().get(CODE_DATE));
            if (System.currentTimeMillis() - diff < 0)
                return new ResultUtil<Object>().setErrorMsg("您的操作太频繁，请稍后再试！");
            else {
                redisTemplate.delete(CODE_DATE);
            }
        }
        if (registerService.codeEmail(member.getId(), email)) {
            redisTemplate.opsForValue().set(CODE_DATE, String.valueOf(AddDateMinute.addMinute(new Date(), 1).getTime()));
            return new ResultUtil<Object>().setData(null);
        } else {
            return new ResultUtil<Object>().setErrorMsg("验证码发送失败！");
        }
    }

    @RequestMapping(value = "/member/forget", method = RequestMethod.POST)
    public Result<Object> forget(@RequestBody MemberForget memberForget) {
        TbMember member = memberService.getByUserName(memberForget.getUserName());
        if (member == null)
            return new ResultUtil<Object>().setErrorMsg("账号不存在");
        if (!member.getEmail().equals(memberForget.getEmail()))
            return new ResultUtil<Object>().setErrorMsg("账号邮箱不匹配");
        if (member.getState() == 0)
            return new ResultUtil<Object>().setErrorMsg("账号未激活，无法修改密码");
        String md5Pass = DigestUtils.md5DigestAsHex(memberForget.getUserPwd().getBytes());
        member.setPassword(md5Pass);
        Long diff = Long.parseLong(redisTemplate.opsForValue().get(CODE_DATE));
        if (redisTemplate.hasKey(CODE_PRX + ":" + member.getId())) {
            if (System.currentTimeMillis() - diff >= 0) {
                redisTemplate.delete(CODE_PRX+ ":" + member.getId());
                return new ResultUtil<Object>().setErrorMsg("验证码已失效，请重新发送！");
            }
            if (memberForget.getCode().equals(redisTemplate.opsForValue().get(CODE_PRX + ":" + member.getId()))) {
                if (memberService.updateById(member) != 1) {
                    return new ResultUtil<Object>().setErrorMsg("密码修改失败！");
                }
                redisTemplate.delete(CODE_PRX + ":" + member.getId());
                return new ResultUtil<Object>().setData(null);
            } else {
                return new ResultUtil<Object>().setErrorMsg("验证码错误！");
            }
        }
        return new ResultUtil<Object>().setErrorMsg("验证码已失效，请重新发送！");
    }

    @RequestMapping(value = "/member/updateEmail", method = RequestMethod.POST)
    public Result<Object> updateEmail(@RequestBody EmailDto emailDto) {
        if (registerService.isExEmail(emailDto.getEmail(), emailDto.getUserId()))
            return new ResultUtil<Object>().setErrorMsg("邮箱已被占用！");
        TbMember tbMember = registerService.selectById(emailDto.getUserId());
        tbMember.setEmail(emailDto.getEmail());
        if (memberService.updateById(tbMember) != 1)
            return new ResultUtil<Object>().setErrorMsg("修改失败！");
        return new ResultUtil<Object>().setData(null);
    }


    @RequestMapping(value = "/member/imageUpload", method = RequestMethod.POST)
    public Result<Object> imageUpload(@RequestBody CommonDto common, HttpServletRequest request) {
        try {
            String dataPrix = "";
            String data = "";
            if (common.getImgData() == null || "".equals(common.getImgData())) {
                throw new Exception("上传失败，上传图片数据为空");
            } else {
                String[] d = common.getImgData().split("base64,");
                if (d != null && d.length == 2) {
                    dataPrix = d[0];
                    data = d[1];
                } else {
                    throw new Exception("上传失败，数据不合法");
                }
            }
            String suffix = "";
            if ("data:image/jpeg;".equalsIgnoreCase(dataPrix)) {//data:image/jpeg;base64,base64编码的jpeg图片数据
                suffix = ".jpg";
            } else if ("data:image/bmp;".equalsIgnoreCase(dataPrix)) {//data:image/bmp;base64,base64编码的icon图片数据
                suffix = ".bmp";
            } else if ("data:image/gif;".equalsIgnoreCase(dataPrix)) {//data:image/gif;base64,base64编码的gif图片数据
                suffix = ".gif";
            } else if ("data:image/png;".equalsIgnoreCase(dataPrix)) {//data:image/png;base64,base64编码的png图片数据
                suffix = ".png";
            } else {
                throw new Exception("上传图片格式不合法");
            }
            String tempFileName = UUID.randomUUID().toString().replace("-", "") + suffix;

            //因为BASE64Decoder的jar问题，此处使用spring框架提供的工具包
            byte[] bs = Base64Utils.decodeFromString(data);
            try {
                //使用apache提供的工具类操作流
                String localImgPath = request.getServletContext().getRealPath("/upload/");
                FileUtils.writeByteArrayToFile(new File(localImgPath, tempFileName), bs);
                String imgPath = memberService.imageUpload(common.getUserId(), common.getToken(), localImgPath, tempFileName);
                return new ResultUtil<Object>().setData(imgPath);
            } catch (Exception ee) {
                return new ResultUtil<Object>().setErrorMsg("上传失败");
            }
        } catch (Exception e) {
            return new ResultUtil<Object>().setErrorMsg("上传失败");
        }
    }
}
