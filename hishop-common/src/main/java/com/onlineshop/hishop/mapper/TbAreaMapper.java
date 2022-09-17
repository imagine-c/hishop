package com.onlineshop.hishop.mapper;

import com.onlineshop.hishop.pojo.TbArea;
import com.onlineshop.hishop.pojo.TbAreaExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbAreaMapper {
    long countByExample(TbAreaExample example);

    int deleteByExample(TbAreaExample example);

    int deleteByPrimaryKey(String code);

    int insert(TbArea record);

    int insertSelective(TbArea record);

    List<TbArea> selectByExample(TbAreaExample example);

    TbArea selectByPrimaryKey(String code);

    int updateByExampleSelective(@Param("record") TbArea record, @Param("example") TbAreaExample example);

    int updateByExample(@Param("record") TbArea record, @Param("example") TbAreaExample example);

    int updateByPrimaryKeySelective(TbArea record);

    int updateByPrimaryKey(TbArea record);
}