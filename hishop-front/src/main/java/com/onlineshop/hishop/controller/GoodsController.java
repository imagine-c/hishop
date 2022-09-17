package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.front.AllGoodsResult;
import com.onlineshop.hishop.dto.front.ProductDet;
import com.onlineshop.hishop.feign.ContentFeignService;
import com.onlineshop.hishop.pojo.TbPanel;
import com.onlineshop.hishop.pojo.TbPanelContent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(description = "商品页面展示")
public class GoodsController {

    @Autowired
    private ContentFeignService contentFeignService;


    @RequestMapping(value = "/goods/navList", method = RequestMethod.GET)
    @ApiOperation(value = "获取导航栏")
    public Result<List<TbPanelContent>> getNavList() {
        return contentFeignService.getNavList();
    }

    @RequestMapping(value = "/goods/home", method = RequestMethod.GET)
    @ApiOperation(value = "首页内容展示")
    public Result<List<TbPanel>> getProductHome() {
        return contentFeignService.getProductHome();
    }

    @RequestMapping(value = "/goods/productDet", method = RequestMethod.GET)
    @ApiOperation(value = "商品详情")
    public Result<ProductDet> getProductDet(Long productId) {
        return contentFeignService.getProductDet(productId);
    }

    @RequestMapping(value = "/goods/allGoods", method = RequestMethod.GET)
    @ApiOperation(value = "所有商品")
    public Result<AllGoodsResult> getAllProduct(@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "20") int size,
                                                @RequestParam(defaultValue = "") String sort,
                                                @RequestParam(defaultValue = "") Long cid,
                                                @RequestParam(defaultValue = "-1") int priceGt,
                                                @RequestParam(defaultValue = "-1") int priceLte) {
        return contentFeignService.getAllProduct(page,size,sort,cid,priceGt,priceLte);

    }

    @RequestMapping(value = "/goods/recommend", method = RequestMethod.GET)
    @ApiOperation(value = "商品推荐板块")
    public Result<List<TbPanel>> getRecommendGoods() {
        return contentFeignService.getRecommendGoods();
    }


}
