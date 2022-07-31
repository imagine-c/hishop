package com.onlineshop.hishop.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.onlineshop.hishop.mapper.TbAreaMapper;
import com.onlineshop.hishop.mapper.TbCityMapper;
import com.onlineshop.hishop.mapper.TbProvinceMapper;
import com.onlineshop.hishop.mapper.TbStreetMapper;
import com.onlineshop.hishop.pojo.TbArea;
import com.onlineshop.hishop.pojo.TbCity;
import com.onlineshop.hishop.pojo.TbProvince;
import com.onlineshop.hishop.pojo.TbStreet;
import com.onlineshop.hishop.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Service
@Component
public class CodeServiceImpl implements CodeService {
    @Autowired
    private TbProvinceMapper tbProvinceMapper;
    @Autowired
    private TbCityMapper tbCityMapper;
    @Autowired
    private TbAreaMapper tbAreaMapper;
    @Autowired
    private TbStreetMapper tbStreetMapper;

    @Override
    public TbProvince selectByProvinceCode(String code) {
        return tbProvinceMapper.selectByPrimaryKey(code);
    }

    @Override
    public TbCity selectByCityCode(String code) {
        return tbCityMapper.selectByPrimaryKey(code);
    }

    @Override
    public TbArea selectByAreaCode(String code) {
        return tbAreaMapper.selectByPrimaryKey(code);
    }

    @Override
    public TbStreet selectByStreetCode(String code) {
        return tbStreetMapper.selectByPrimaryKey(code);
    }
}
