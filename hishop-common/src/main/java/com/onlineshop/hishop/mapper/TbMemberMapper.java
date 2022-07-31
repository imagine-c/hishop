package com.onlineshop.hishop.mapper;

import com.onlineshop.hishop.pojo.TbMember;
import com.onlineshop.hishop.pojo.TbMemberExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbMemberMapper {
    long countByExample(TbMemberExample example);

    int deleteByExample(TbMemberExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbMember record);

    int insertSelective(TbMember record);

    List<TbMember> selectByExample(TbMemberExample example);

    TbMember selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbMember record, @Param("example") TbMemberExample example);

    int updateByExample(@Param("record") TbMember record, @Param("example") TbMemberExample example);

    int updateByPrimaryKeySelective(TbMember record);

    int updateByPrimaryKey(TbMember record);

    List<TbMember> selectByMemberInfo(@Param("search") String search, @Param("minDate") String minDate,
                                      @Param("maxDate") String maxDate, @Param("orderCol") String orderCol,
                                      @Param("orderDir") String orderDir);

    List<TbMember> selectByRemoveMemberInfo(@Param("search") String search, @Param("minDate") String minDate,
                                            @Param("maxDate") String maxDate, @Param("orderCol") String orderCol,
                                            @Param("orderDir") String orderDir);
}