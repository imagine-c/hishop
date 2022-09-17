package com.onlineshop.hishop.service;

import com.onlineshop.hishop.pojo.TbArea;
import com.onlineshop.hishop.pojo.TbCity;
import com.onlineshop.hishop.pojo.TbProvince;
import com.onlineshop.hishop.pojo.TbStreet;

public interface CodeService {
    TbProvince selectByProvinceCode(String code);

    TbCity selectByCityCode(String code);

    TbArea selectByAreaCode(String code);

    TbStreet selectByStreetCode(String code);
}
