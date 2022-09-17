package com.onlineshop.hishop.service.impl;


import com.onlineshop.hishop.exception.HiShopException;
import com.onlineshop.hishop.mapper.TbMemberMapper;
import com.onlineshop.hishop.pojo.TbMember;
import com.onlineshop.hishop.pojo.TbMemberExample;
import com.onlineshop.hishop.service.RegisterService;
import com.onlineshop.hishop.utils.AddDateMinute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;


@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private TbMemberMapper tbMemberMapper;
    @Autowired
    private EmailUtilService emailUtilService;

    @Override
    public boolean checkData(String param, int type) {

        TbMemberExample example = new TbMemberExample();
        TbMemberExample.Criteria criteria = example.createCriteria();
        //1：用户名 2：手机号 3：邮箱
        if (type == 1) {
            criteria.andUsernameEqualTo(param);
        } else if (type == 2) {
            criteria.andPhoneEqualTo(param);
        } else if (type == 3) {
            criteria.andEmailEqualTo(param);
        } else {
            return false;
        }

        List<TbMember> list = tbMemberMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return false;
        }
        return true;
    }

    @Override
    public int RegisterEmail(String userName, String userPwd, String email) {

        TbMember tbMember = new TbMember();
        tbMember.setUsername(userName);

        if (userName.isEmpty() || userPwd.isEmpty()) {
            return -1; //用户名密码不能为空
        }
        if (!checkData(userName, 1)) {
            return 0; //该用户名已被注册
        }
        if (!checkData(email, 3)) {
            return 1; //该邮箱被占用
        }
        //MD5加密
        String md5Pass = DigestUtils.md5DigestAsHex(userPwd.getBytes());
        tbMember.setPassword(md5Pass);
        tbMember.setEmail(email);
        tbMember.setState(0);
        tbMember.setCreated(new Date());
        tbMember.setUpdated(new Date());
        tbMember.setDeleteDate(AddDateMinute.addDay(new Date(), 1));
        tbMember.setIsActive(false);

        if (!emailUtilService.SendRegisterEmail(email, userName)) {
            return 2; //邮箱发送失败
        }
        if (tbMemberMapper.insert(tbMember) != 1) {
            throw new HiShopException("保存用户失败");
        }
        return 3;
    }

    @Override
    public TbMember EmailActive(String username) {
        TbMemberExample example = new TbMemberExample();
        TbMemberExample.Criteria cri = example.createCriteria();
        cri.andUsernameEqualTo(username);
        List<TbMember> members = tbMemberMapper.selectByExample(example);
        TbMember member = new TbMember();
        if (members.isEmpty()) {
            member.setState(0);
            member.setIsActive(false);
            return member;// 激活超时，重新注册
        }
        TbMember member1 = members.get(0);
        if (member1.getIsActive()) {
            member.setState(1);
            member.setIsActive(false);
            return member;//已经激活
        }
        member1.setDeleteDate(null);
        member1.setUpdated(new Date());
        member1.setState(3);
        member1.setIsActive(true);
        if (tbMemberMapper.updateByPrimaryKey(member1) != 1) {
            member.setState(2);
            member.setIsActive(false);
            return member;//激活失败
        }
        member.setState(3);
        member.setIsActive(true);
        return member;//激活成功
    }


    @Override
    public boolean codeEmail(Long uid, String email) {
        return emailUtilService.SendCode(uid, email);
    }

    @Override
    public boolean isExEmail(String email, Long uid) {
        TbMemberExample example = new TbMemberExample();
        TbMemberExample.Criteria cri = example.createCriteria();
        cri.andEmailEqualTo(email);
        List<TbMember> list = tbMemberMapper.selectByExample(example);
        if (list.isEmpty())
            return false;
        if (list.get(0).getId() == uid)
            return false;
        return true;
    }

    @Override
    public TbMember selectById(Long uid) {
        return tbMemberMapper.selectByPrimaryKey(uid);
    }


}
