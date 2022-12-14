package com.onlineshop.hishop.service;

import com.onlineshop.hishop.pojo.TbMember;

/**
 * @author imagine
 */
public interface MemberService {

    /**
     * 头像上传
     *
     * @param userId
     * @param token
     * @param localImgPath
     * @param imgName
     * @return
     */
    String imageUpload(Long userId, String token, String localImgPath, String imgName);

    TbMember getByUserName(String username);

    int updateById(TbMember tbMember);
}
