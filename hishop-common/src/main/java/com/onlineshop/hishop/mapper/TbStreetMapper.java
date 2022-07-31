package com.onlineshop.hishop.mapper;

import com.onlineshop.hishop.pojo.TbStreet;
import com.onlineshop.hishop.pojo.TbStreetExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbStreetMapper {
    long countByExample(TbStreetExample example);

    int deleteByExample(TbStreetExample example);

    int deleteByPrimaryKey(String code);

    int insert(TbStreet record);

    int insertSelective(TbStreet record);

    List<TbStreet> selectByExample(TbStreetExample example);

    TbStreet selectByPrimaryKey(String code);

    int updateByExampleSelective(@Param("record") TbStreet record, @Param("example") TbStreetExample example);

    int updateByExample(@Param("record") TbStreet record, @Param("example") TbStreetExample example);

    int updateByPrimaryKeySelective(TbStreet record);

    int updateByPrimaryKey(TbStreet record);
}