package com.onlineshop.hishop.service;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.dto.MemberDto;
import com.onlineshop.hishop.pojo.TbMember;

import java.util.Date;


public interface MemberService {

    /**
     * 根据ID获取会员信息
     * @param memberId
     * @return
     */
    TbMember getMemberById(long memberId);

    /**
     * 分页获得会员列表
     * @param draw
     * @param start
     * @param length
     * @param search
     * @return
     */
    DataTablesResult getMemberList(int draw, int start, int length, String search,
                                   String minDate, String maxDate, String orderCol, String orderDir);

    /**
     * 分页获得移除会员列表
     * @param draw
     * @param start
     * @param length
     * @param search
     * @param minDate
     * @param maxDate
     * @param orderCol
     * @param orderDir
     * @return
     */
    DataTablesResult getRemoveMemberList(int draw, int start, int length, String search,
                                         String minDate, String maxDate, String orderCol, String orderDir);

    /**
     * 获得所有会员总数
     * @return
     */
    DataTablesResult getMemberCount();

    /**
     * 获得删除会员
     * @return
     */
    DataTablesResult getRemoveMemberCount();

    /**
     * 通过邮件获取
     * @param email
     * @return
     */
    TbMember getMemberByEmail(String email);

    /**
     * 通过手机获取
     * @param phone
     * @return
     */
    TbMember getMemberByPhone(String phone);

    /**
     * 通过获取名获取
     * @param username
     * @return
     */
    TbMember getMemberByUsername(String username);

    /**
     * 添加会员
     * @param memberDto
     * @return
     */
    TbMember addMember(MemberDto memberDto);

    /**
     * 更新会员信息
     * @param id
     * @param memberDto
     * @return
     */
    TbMember updateMember(Long id, MemberDto memberDto);

    /**
     * 修改会员密码
     * @param id
     * @param memberDto
     * @return
     */
    TbMember changePassMember(Long id, MemberDto memberDto);

    /**
     * 修改会员状态
     * @param id
     * @param state
     * @return
     */
    TbMember alertMemberState(Long id, Integer state);

    TbMember unBlockMember(Long id);

    /**
     * 彻底删除会员
     * @param id
     * @return
     */
    int deleteMember(Long id);

    /**
     * 验证编辑邮箱是否存在
     * @param id
     * @param email
     * @return
     */
    TbMember getMemberByEditEmail(Long id, String email);

    /**
     * 验证编辑手机是否存在
     * @param id
     * @param phone
     * @return
     */
    TbMember getMemberByEditPhone(Long id, String phone);

    /**
     * 验证编辑用户名是否存在
     * @param id
     * @param username
     * @return
     */
    TbMember getMemberByEditUsername(Long id, String username);

    int blockMember(Long id, Date BlockDate, Date unBlockDate);

    int cancelUserDel();

    int cancelUserBlock();
}
