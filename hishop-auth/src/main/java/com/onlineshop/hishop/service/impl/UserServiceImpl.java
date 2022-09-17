package com.onlineshop.hishop.service.impl;


import com.onlineshop.hishop.mapper.TbUserMapper;
import com.onlineshop.hishop.pojo.TbUser;
import com.onlineshop.hishop.pojo.TbUserExample;
import com.onlineshop.hishop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author imagine
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public TbUser getUserByUsername(String username) {

        List<TbUser> list = new ArrayList<>();
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        try {
            list = tbUserMapper.selectByExample(example);
        } catch (Exception e) {
           e.printStackTrace();
        }
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public Set<String> getRoles(String username) {

        return tbUserMapper.getRoles(username);
    }

    @Override
    public Set<String> getPermissions(String username) {

        return tbUserMapper.getPermissions(username);
    }

}
