package com.onlineshop.hishop.service;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.dto.front.AllGoodsResult;
import com.onlineshop.hishop.dto.front.ProductDet;
import com.onlineshop.hishop.pojo.TbPanel;
import com.onlineshop.hishop.pojo.TbPanelContent;

import java.util.List;

public interface ContentService {

    /**
     * 添加板块内容
     * @param tbPanelContent
     * @return
     */
    int addPanelContent(TbPanelContent tbPanelContent);

    /**
     * 通过panelId获取板块具体内容
     * @param panelId
     * @return
     */
    DataTablesResult getPanelContentListByPanelId(int panelId);

    /**
     * 删除板块内容
     * @param id
     * @return
     */
    int deletePanelContent(int id);

    /**
     * 编辑板块内容
     * @param tbPanelContent
     * @return
     */
    int updateContent(TbPanelContent tbPanelContent);

    /**
     * 通过id获取板块内容
     * @param id
     * @return
     */
    TbPanelContent getTbPanelContentById(int id);

    /**
     * 获取首页缓存
     * @return
     */
    String getIndexRedis();

    /**
     * 同步首页缓存
     * @return
     */
    int updateIndexRedis();

    /**
     * 获取推荐板块缓存
     * @return
     */
    String getRecommendRedis();

    /**
     * 同步推荐板块缓存
     * @return
     */
    int updateRecommendRedis();

}
