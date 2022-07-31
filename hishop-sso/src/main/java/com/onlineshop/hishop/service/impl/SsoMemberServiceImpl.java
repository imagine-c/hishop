package com.onlineshop.hishop.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import com.onlineshop.hishop.dto.front.Member;
import com.onlineshop.hishop.exception.HiShopException;
import com.onlineshop.hishop.jedis.JedisClient;
import com.onlineshop.hishop.mapper.TbMemberMapper;
import com.onlineshop.hishop.pojo.TbMember;
import com.onlineshop.hishop.pojo.TbMemberExample;
import com.onlineshop.hishop.service.LoginService;
import com.onlineshop.hishop.service.SsoMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;


@Service
@Component
@PropertySource("classpath:conf/sso.properties")
public class SsoMemberServiceImpl implements SsoMemberService {

    private Logger log = LoggerFactory.getLogger(SsoMemberServiceImpl.class);

    @Autowired
    private LoginService loginService;
    @Autowired
    private TbMemberMapper tbMemberMapper;
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private ImageUpload imgUpload;
    @Value("${server.host}")
    private String host;
    @Value("${server.path}")
    private String ServerPath;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public String imageUpload(Long userId,String token,String localImgPath,String imgName) {

        String imgPath= host+ServerPath+imgName;
        TbMember tbMember=tbMemberMapper.selectByPrimaryKey(userId);
        if(tbMember==null){
            throw new HiShopException("通过id获取用户失败");
        }
        imgUpload.uploadImg(localImgPath,ServerPath,imgName);
        //删除本地文件
        File file=new File((localImgPath+imgName));
        if (file.isFile() && file.exists()) {
            file.delete();
        }
        tbMember.setFile(imgPath);
        if(tbMemberMapper.updateByPrimaryKey(tbMember)!=1){
            throw new HiShopException("更新用户头像失败");
        }
        //更新缓存
        Member member=loginService.getUserByToken(token);
        member.setFile(imgPath);
        jedisClient.set("SESSION:" + token, new Gson().toJson(member));
        return imgPath;
    }

    @Override
    public TbMember getByUserName(String username) {
        TbMemberExample tbMemberExample = new TbMemberExample();
        TbMemberExample.Criteria cri = tbMemberExample.createCriteria();
        cri.andUsernameEqualTo(username);
        List<TbMember> members = tbMemberMapper.selectByExample(tbMemberExample);
        if (!members.isEmpty())
            return members.get(0);
        return null;
    }

    @Override
    public int updateById(TbMember tbMember) {
        return tbMemberMapper.updateByPrimaryKey(tbMember);
    }

}
