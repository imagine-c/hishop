package com.onlineshop.hishop.service.impl;


import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.exception.HiShopException;
import com.onlineshop.hishop.mapper.TbItemMapper;
import com.onlineshop.hishop.mapper.TbPanelContentMapper;
import com.onlineshop.hishop.mapper.TbPanelMapper;
import com.onlineshop.hishop.pojo.*;
import com.onlineshop.hishop.service.ContentService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Log4j2
@PropertySource("classpath:conf/content.yml")
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbPanelMapper tbPanelMapper;
    @Autowired
    private TbPanelContentMapper tbPanelContentMapper;
    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${RECOMEED_PANEL}")
    private String RECOMEED_PANEL;

    @Value("${PRODUCT_HOME}")
    private String PRODUCT_HOME;

    @Value("${HEADER_PANEL}")
    private String HEADER_PANEL;

    @Value("${HEADER_PANEL_ID}")
    private Integer HEADER_PANEL_ID;


    @Override
    public int addPanelContent(TbPanelContent tbPanelContent) {

        tbPanelContent.setCreated(new Date());
        tbPanelContent.setUpdated(new Date());
        if(tbPanelContentMapper.insert(tbPanelContent)!=1){
            throw new HiShopException("添加首页板块内容失败");
        }
        //同步导航栏缓存
        if(tbPanelContent.getPanelId()==HEADER_PANEL_ID){
            updateNavListRedis();
        }
        //同步缓存
        deleteHomeRedis();
        return 1;
    }

    @Override
    public DataTablesResult getPanelContentListByPanelId(int panelId) {
        DataTablesResult result=new DataTablesResult();
        List<TbPanelContent> list=new ArrayList<>();

        TbPanelContentExample example=new TbPanelContentExample();
        TbPanelContentExample.Criteria criteria=example.createCriteria();
        //条件查询
        criteria.andPanelIdEqualTo(panelId);
        list=tbPanelContentMapper.selectByExample(example);
        for(TbPanelContent content:list){
            if(content.getProductId()!=null){
                TbItem tbItem=tbItemMapper.selectByPrimaryKey(content.getProductId());
                content.setProductName(tbItem.getTitle());
                content.setSalePrice(tbItem.getPrice());
                content.setSubTitle(tbItem.getSellPoint());
            }
        }

        result.setData(list);
        return result;
    }

    @Override
    public int deletePanelContent(int id) {

        if(tbPanelContentMapper.deleteByPrimaryKey(id)!=1){
            throw new HiShopException("删除首页板块失败");
        }
        //同步导航栏缓存
        if(id==HEADER_PANEL_ID){
            updateNavListRedis();
        }
        //同步缓存
        deleteHomeRedis();
        return 1;
    }

    @Override
    public int updateContent(TbPanelContent tbPanelContent) {

        TbPanelContent old=getTbPanelContentById(tbPanelContent.getId());
        if(StringUtils.isBlank(tbPanelContent.getPicUrl())){
            tbPanelContent.setPicUrl(old.getPicUrl());
        }
        if(StringUtils.isBlank(tbPanelContent.getPicUrl2())){
            tbPanelContent.setPicUrl2(old.getPicUrl2());
        }
        if(StringUtils.isBlank(tbPanelContent.getPicUrl3())){
            tbPanelContent.setPicUrl3(old.getPicUrl3());
        }
        tbPanelContent.setCreated(old.getCreated());
        tbPanelContent.setUpdated(new Date());
        if(tbPanelContentMapper.updateByPrimaryKey(tbPanelContent)!=1){
            throw new HiShopException("更新板块内容失败");
        }
        //同步导航栏缓存
        if(tbPanelContent.getPanelId()==HEADER_PANEL_ID){
            updateNavListRedis();
        }
        //同步缓存
        deleteHomeRedis();
        return 1;
    }

    @Override
    public TbPanelContent getTbPanelContentById(int id) {

        TbPanelContent tbPanelContent=tbPanelContentMapper.selectByPrimaryKey(id);
        if(tbPanelContent==null){
            throw new HiShopException("通过id获取板块内容失败");
        }
        return tbPanelContent;
    }


    @Override
    public String getIndexRedis() {

        try{
            String json=redisTemplate.opsForValue().get(PRODUCT_HOME);
            return json;
        }catch (Exception e){
            log.error(e.toString());
        }
        return "";
    }

    @Override
    public int updateIndexRedis() {

        deleteHomeRedis();
        return 1;
    }

    @Override
    public String getRecommendRedis() {

        try{
            String json=redisTemplate.opsForValue().get(RECOMEED_PANEL);
            return json;
        }catch (Exception e){
            log.error(e.toString());
        }
        return "";
    }

    @Override
    public int updateRecommendRedis() {

        try {
            redisTemplate.delete(RECOMEED_PANEL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 1;
    }

    public void updateNavListRedis() {

        try {
            redisTemplate.delete(HEADER_PANEL);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 同步首页缓存
     */
    public void deleteHomeRedis(){
        try {
            redisTemplate.delete(PRODUCT_HOME);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
