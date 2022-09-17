package com.onlineshop.hishop.mapper;

import com.onlineshop.hishop.pojo.TbProvince;
import com.onlineshop.hishop.pojo.TbProvinceExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbProvinceMapper {
    long countByExample(TbProvinceExample example);

    int deleteByExample(TbProvinceExample example);

    int deleteByPrimaryKey(String code);

    int insert(TbProvince record);

    int insertSelective(TbProvince record);

    List<TbProvince> selectByExample(TbProvinceExample example);

    TbProvince selectByPrimaryKey(String code);

    int updateByExampleSelective(@Param("record") TbProvince record, @Param("example") TbProvinceExample example);

    int updateByExample(@Param("record") TbProvince record, @Param("example") TbProvinceExample example);

    int updateByPrimaryKeySelective(TbProvince record);

    int updateByPrimaryKey(TbProvince record);
}