package com.onlineshop.hishop.mapper;

import com.onlineshop.hishop.pojo.TbRolePerm;
import com.onlineshop.hishop.pojo.TbRolePermExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbRolePermMapper {
    long countByExample(TbRolePermExample example);

    int deleteByExample(TbRolePermExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbRolePerm record);

    int insertSelective(TbRolePerm record);

    List<TbRolePerm> selectByExample(TbRolePermExample example);

    TbRolePerm selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbRolePerm record, @Param("example") TbRolePermExample example);

    int updateByExample(@Param("record") TbRolePerm record, @Param("example") TbRolePermExample example);

    int updateByPrimaryKeySelective(TbRolePerm record);

    int updateByPrimaryKey(TbRolePerm record);
}