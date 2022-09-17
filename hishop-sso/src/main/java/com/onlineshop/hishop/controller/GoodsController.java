package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.front.AllGoodsResult;
import com.onlineshop.hishop.dto.front.ProductDet;
import com.onlineshop.hishop.dto.front.SearchResult;
import com.onlineshop.hishop.pojo.TbPanel;
import com.onlineshop.hishop.pojo.TbPanelContent;
import com.onlineshop.hishop.service.ContentService;
import com.onlineshop.hishop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/front")
public class GoodsController {

    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/goods/navList", method = RequestMethod.GET)
    public Result<List<TbPanelContent>> getNavList() {

        List<TbPanelContent> list = contentService.getNavList();
        return new ResultUtil<List<TbPanelContent>>().setData(list);
    }

    @RequestMapping(value = "/goods/home", method = RequestMethod.GET)
    public Result<List<TbPanel>> getProductHome() {

        List<TbPanel> list = contentService.getHome();
        return new ResultUtil<List<TbPanel>>().setData(list);
    }

    @RequestMapping(value = "/goods/productDet", method = RequestMethod.GET)
    public Result<ProductDet> getProductDet(Long productId) {

        ProductDet productDet = contentService.getProductDet(productId);
        return new ResultUtil<ProductDet>().setData(productDet);
    }

    @RequestMapping(value = "/goods/allGoods", method = RequestMethod.GET)
    public Result<AllGoodsResult> getAllProduct(@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "20") int size,
                                                @RequestParam(defaultValue = "") String sort,
                                                @RequestParam(defaultValue = "") Long cid,
                                                @RequestParam(defaultValue = "-1") int priceGt,
                                                @RequestParam(defaultValue = "-1") int priceLte) {

        AllGoodsResult allGoodsResult = contentService.getAllProduct(page, size, sort, cid, priceGt, priceLte);
        return new ResultUtil<AllGoodsResult>().setData(allGoodsResult);
    }

    @RequestMapping(value = "/goods/recommend", method = RequestMethod.GET)
    public Result<List<TbPanel>> getRecommendGoods() {

        List<TbPanel> list = contentService.getRecommendGoods();
        return new ResultUtil<List<TbPanel>>().setData(list);
    }


}
