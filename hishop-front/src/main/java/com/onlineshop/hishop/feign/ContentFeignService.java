package com.onlineshop.hishop.feign;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.front.AllGoodsResult;
import com.onlineshop.hishop.dto.front.ProductDet;
import com.onlineshop.hishop.pojo.TbPanel;
import com.onlineshop.hishop.pojo.TbPanelContent;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient("hishop-sso")
public interface ContentFeignService {
    
    @RequestMapping(value = "/front/goods/navList", method = RequestMethod.GET)
    public Result<List<TbPanelContent>> getNavList();

    @RequestMapping(value = "/front/goods/home", method = RequestMethod.GET)
    public Result<List<TbPanel>> getProductHome();

    @RequestMapping(value = "/front/goods/productDet", method = RequestMethod.GET)
    public Result<ProductDet> getProductDet(Long productId);

    @RequestMapping(value = "/front/goods/allGoods", method = RequestMethod.GET)
    public Result<AllGoodsResult> getAllProduct(@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "20") int size,
                                                @RequestParam(defaultValue = "") String sort,
                                                @RequestParam(defaultValue = "") Long cid,
                                                @RequestParam(defaultValue = "-1") int priceGt,
                                                @RequestParam(defaultValue = "-1") int priceLte);
    
    @RequestMapping(value = "/front/goods/recommend", method = RequestMethod.GET)
    public Result<List<TbPanel>> getRecommendGoods();
    
    
}
