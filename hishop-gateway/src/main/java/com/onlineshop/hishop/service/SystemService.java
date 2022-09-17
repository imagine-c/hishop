package com.onlineshop.hishop.service;



import com.onlineshop.hishop.pojo.TbShiroFilter;

import java.util.List;

/**
 * @author imagine
 */
public interface SystemService {

    /**
     * 获得shiro过滤链配置
     *
     * @return
     */
    List<TbShiroFilter> getShiroFilter();

}
