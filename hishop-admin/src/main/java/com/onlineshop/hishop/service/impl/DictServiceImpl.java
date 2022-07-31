package com.onlineshop.hishop.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.onlineshop.hishop.constant.DictConstant;
import com.onlineshop.hishop.mapper.TbDictMapper;
import com.onlineshop.hishop.pojo.TbDict;
import com.onlineshop.hishop.pojo.TbDictExample;
import com.onlineshop.hishop.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;

@Service
@Component
public class DictServiceImpl implements DictService {

    @Autowired
    private TbDictMapper tbDictMapper;

    @Override
    public List<TbDict> getDictList() {

        TbDictExample example=new TbDictExample();
        TbDictExample.Criteria criteria=example.createCriteria();
        //条件查询
        criteria.andTypeEqualTo(DictConstant.DICT_EXT);
        List<TbDict> list = tbDictMapper.selectByExample(example);
        return list;
    }

    @Override
    public List<TbDict> getStopList() {

        TbDictExample example=new TbDictExample();
        TbDictExample.Criteria criteria=example.createCriteria();
        //条件查询
        criteria.andTypeEqualTo(DictConstant.DICT_STOP);
        List<TbDict> list = tbDictMapper.selectByExample(example);
        return list;
    }

    @Override
    public int addDict(TbDict tbDict) {

        tbDictMapper.insert(tbDict);
        return 1;
    }

    @Override
    public int updateDict(TbDict tbDict) {

        tbDictMapper.updateByPrimaryKey(tbDict);
        return 1;
    }

    @Override
    public int delDict(int id) {

        tbDictMapper.deleteByPrimaryKey(id);
        return 1;
    }
}
