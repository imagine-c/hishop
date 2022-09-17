package com.onlineshop.hishop.service;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.dto.front.AllGoodsResult;
import com.onlineshop.hishop.dto.front.ProductDet;
import com.onlineshop.hishop.pojo.TbPanel;
import com.onlineshop.hishop.pojo.TbPanelContent;

import java.util.List;

public interface ContentService {

    /**
     * 获取首页数据
     * @return
     */
    List<TbPanel> getHome();

    /**
     * 获取商品推荐板块
     * @return
     */
    List<TbPanel> getRecommendGoods();


    /**
     * 获取商品详情
     * @param id
     * @return
     */
    ProductDet getProductDet(Long id);

    /**
     * 分页多条件获取全部商品
     * @param page
     * @param size
     * @param sort
     * @param priceGt
     * @param priceLte
     * @return
     */
    AllGoodsResult getAllProduct(int page, int size, String sort, Long cid, int priceGt, int priceLte);

    /**
     * 获取导航栏
     * @return
     */
    List<TbPanelContent> getNavList();

}
