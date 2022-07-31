package com.onlineshop.hishop.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import com.onlineshop.hishop.dto.DtoUtil;
import com.onlineshop.hishop.dto.front.Member;
import com.onlineshop.hishop.jedis.JedisClient;
import com.onlineshop.hishop.mapper.TbMemberMapper;
import com.onlineshop.hishop.pojo.TbMember;
import com.onlineshop.hishop.pojo.TbMemberExample;
import com.onlineshop.hishop.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

@Service
@Component
@PropertySource("classpath:conf/sso.properties")
public class LoginServiceImpl implements LoginService {

	@Autowired
	private TbMemberMapper tbMemberMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public Member userLogin(String username, String password) {
		TbMemberExample example = new TbMemberExample();
		example.createCriteria().andUsernameEqualTo(username);
		List<TbMember> list = tbMemberMapper.selectByExample(example);
		if (list.isEmpty()){
			Member member=new Member();
			member.setState(0);
			member.setMessage("用户名不存在");
			return member;
		}
		TbMember tbMember = list.get(0);
		if (tbMember.getState()==0){
			Member member=new Member();
			member.setState(0);
			member.setMessage("用户未激活，请尽快根据邮件激活");
			return member;
		}
		if (tbMember.getState()==1){
			Member member=new Member();
			member.setState(1);
			member.setMessage("用户被冻结，无法登录");
			return member;
		}
		if (tbMember.getState()==2){
			Member member=new Member();
			member.setState(2);
			member.setMessage("用户被封号，请联系管理员");
			return member;
		}

		//md5加密
		if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbMember.getPassword())) {
			Member member=new Member();
			member.setState(0);
			member.setMessage("用户名或密码错误");
			return member;
		}
		String token = UUID.randomUUID().toString();
		Member member= DtoUtil.TbMemer2Member(tbMember);
		member.setToken(token);
		member.setState(3);
		// 用户信息写入redis：key："SESSION:token" value："user"
		jedisClient.set("SESSION:" + token, new Gson().toJson(member));
		jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
		return member;
	}

	@Override
	public Member getUserByToken(String token) {

		String json = jedisClient.get("SESSION:" + token);
		if (json==null) {
			Member member=new Member();
			member.setState(0);
			member.setMessage("用户登录已过期");
			return member;
		}
		//重置过期时间
		jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
		Member member = new Gson().fromJson(json,Member.class);
		return member;
	}

	@Override
	public int logout(String token) {

		jedisClient.del("SESSION:" + token);
		return 1;
	}

}
