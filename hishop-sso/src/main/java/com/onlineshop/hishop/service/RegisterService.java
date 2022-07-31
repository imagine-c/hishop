package com.onlineshop.hishop.service;


import com.onlineshop.hishop.pojo.TbMember;

public interface RegisterService {

    /**
     * 勾选
     * @param param
     * @param type
     * @return
     */
    boolean checkData(String param, int type);

    /**
     * 注册
     * @param userName
     * @param userPwd
     * @param email
     * @return
     */
    int RegisterEmail(String userName, String userPwd,String email);

    TbMember EmailActive(String username);

    boolean codeEmail(Long uid,String email);

    boolean isExEmail(String email,Long uid);

    TbMember selectById(Long uid);
}
