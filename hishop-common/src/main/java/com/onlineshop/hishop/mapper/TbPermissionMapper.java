package com.onlineshop.hishop.mapper;

import com.onlineshop.hishop.pojo.TbPermission;
import com.onlineshop.hishop.pojo.TbPermissionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbPermissionMapper {
    long countByExample(TbPermissionExample example);

    int deleteByExample(TbPermissionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbPermission record);

    int insertSelective(TbPermission record);

    List<TbPermission> selectByExample(TbPermissionExample example);

    TbPermission selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbPermission record, @Param("example") TbPermissionExample example);

    int updateByExample(@Param("record") TbPermission record, @Param("example") TbPermissionExample example);

    int updateByPrimaryKeySelective(TbPermission record);

    int updateByPrimaryKey(TbPermission record);
}