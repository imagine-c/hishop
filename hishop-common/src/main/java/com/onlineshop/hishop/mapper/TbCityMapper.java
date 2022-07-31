package com.onlineshop.hishop.mapper;

import com.onlineshop.hishop.pojo.TbCity;
import com.onlineshop.hishop.pojo.TbCityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbCityMapper {
    long countByExample(TbCityExample example);

    int deleteByExample(TbCityExample example);

    int deleteByPrimaryKey(String code);

    int insert(TbCity record);

    int insertSelective(TbCity record);

    List<TbCity> selectByExample(TbCityExample example);

    TbCity selectByPrimaryKey(String code);

    int updateByExampleSelective(@Param("record") TbCity record, @Param("example") TbCityExample example);

    int updateByExample(@Param("record") TbCity record, @Param("example") TbCityExample example);

    int updateByPrimaryKeySelective(TbCity record);

    int updateByPrimaryKey(TbCity record);
}