package com.onlineshop.hishop.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.onlineshop.hishop.domain.ZTreeNode;
import com.onlineshop.hishop.dto.DtoUtil;
import com.onlineshop.hishop.exception.HiShopException;
import com.onlineshop.hishop.jedis.JedisClient;
import com.onlineshop.hishop.mapper.TbPanelMapper;
import com.onlineshop.hishop.pojo.TbPanel;
import com.onlineshop.hishop.pojo.TbPanelExample;
import com.onlineshop.hishop.service.PanelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Component
@PropertySource("classpath:conf/content.properties")
public class PanelServiceImpl implements PanelService {

    private final static Logger log= LoggerFactory.getLogger(PanelServiceImpl.class);

    @Autowired
    private TbPanelMapper tbPanelMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${PRODUCT_HOME}")
    private String PRODUCT_HOME;

    @Override
    public TbPanel getTbPanelById(int id) {

        TbPanel tbPanel=tbPanelMapper.selectByPrimaryKey(id);
        if(tbPanel==null){
            throw new HiShopException("通过id获得板块失败");
        }
        return tbPanel;
    }

    @Override
    public List<ZTreeNode> getPanelList(int position, boolean showAll) {

        TbPanelExample example=new TbPanelExample();
        TbPanelExample.Criteria criteria=example.createCriteria();
        if(position==0&&!showAll){
            //除去非轮播
            criteria.andTypeNotEqualTo(0);
        }else if(position==-1){
            //仅含轮播
            position=0;
            criteria.andTypeEqualTo(0);
        }
        //首页板块
        criteria.andPositionEqualTo(position);
        example.setOrderByClause("sort_order");
        List<TbPanel> panelList=tbPanelMapper.selectByExample(example);

        List<ZTreeNode> list=new ArrayList<>();

        for(TbPanel tbPanel:panelList){
            ZTreeNode zTreeNode= DtoUtil.TbPanel2ZTreeNode(tbPanel);
            list.add(zTreeNode);
        }

        return list;
    }

    @Override
    public int addPanel(TbPanel tbPanel) {

        if(tbPanel.getType()==0){
            TbPanelExample example=new TbPanelExample();
            TbPanelExample.Criteria criteria=example.createCriteria();
            criteria.andTypeEqualTo(0);
            List<TbPanel> list = tbPanelMapper.selectByExample(example);
            if(list!=null&&list.size()>0){
                throw new HiShopException("已有轮播图板块,轮播图仅能添加1个!");
            }
        }

        tbPanel.setCreated(new Date());
        tbPanel.setUpdated(new Date());

        if(tbPanelMapper.insert(tbPanel)!=1){
            throw new HiShopException("添加板块失败");
        }
        //同步缓存
        deleteHomeRedis();
        return 1;
    }

    @Override
    public int updatePanel(TbPanel tbPanel) {

        TbPanel old=getTbPanelById(tbPanel.getId());
        tbPanel.setUpdated(new Date());

        if(tbPanelMapper.updateByPrimaryKey(tbPanel)!=1){
            throw new HiShopException("更新板块失败");
        }
        //同步缓存
        deleteHomeRedis();
        return 1;
    }

    @Override
    public int deletePanel(int id) {

        if(tbPanelMapper.deleteByPrimaryKey(id)!=1){
            throw new HiShopException("删除内容分类失败");
        }
        //同步缓存
        deleteHomeRedis();
        return 1;
    }

    /**
     * 同步首页缓存
     */
    public void deleteHomeRedis(){
        try {
            jedisClient.del(PRODUCT_HOME);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
