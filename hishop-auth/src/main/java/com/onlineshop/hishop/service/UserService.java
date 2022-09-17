package com.onlineshop.hishop.service;


import com.onlineshop.hishop.pojo.TbUser;

import java.util.Set;

/**
 * @author imagine
 */
public interface UserService {

    /**
     * 通过用户名获取用户
     *
     * @param username
     * @return
     */
    TbUser getUserByUsername(String username);

    /**
     * 通过用户名获取角色
     *
     * @param username
     * @return
     */
    Set<String> getRoles(String username);

    /**
     * 通过用户名获取权限
     *
     * @param username
     * @return
     */
    Set<String> getPermissions(String username);

}
