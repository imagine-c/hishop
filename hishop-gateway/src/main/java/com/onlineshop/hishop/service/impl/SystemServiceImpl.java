package com.onlineshop.hishop.service.impl;



import com.onlineshop.hishop.mapper.TbShiroFilterMapper;
import com.onlineshop.hishop.pojo.TbShiroFilter;
import com.onlineshop.hishop.pojo.TbShiroFilterExample;
import com.onlineshop.hishop.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author imagine
 */
@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    private TbShiroFilterMapper tbShiroFilterMapper;


    @Override
    public List<TbShiroFilter> getShiroFilter() {

        TbShiroFilterExample example = new TbShiroFilterExample();
        example.setOrderByClause("sort_order");
        List<TbShiroFilter> list = tbShiroFilterMapper.selectByExample(example);
        if (list != null) {
            return list;
        }
        return null;
    }

}
