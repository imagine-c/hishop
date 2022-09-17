package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.front.SearchResult;
import com.onlineshop.hishop.feign.SearchFeignService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @Autowired
    private SearchFeignService searchFeignService;

    @RequestMapping(value = "/goods/search", method = RequestMethod.GET)
    @ApiOperation(value = "搜索商品ES")
    public Result<SearchResult> searchProduct(@RequestParam(defaultValue = "") String key,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "20") int size,
                                              @RequestParam(defaultValue = "") String sort,
                                              @RequestParam(defaultValue = "-1") int priceGt,
                                              @RequestParam(defaultValue = "-1") int priceLte) {
        return searchFeignService.searchProduct(key, page, size, sort, priceGt, priceLte) ;
    }

    @RequestMapping(value = "/goods/quickSearch", produces = "text/plain;charset=UTF-8", method = RequestMethod.GET)
    @ApiOperation(value = "快速搜索")
    public String getQuickSearch(@RequestParam(defaultValue = "") String key) {

        return searchFeignService.getQuickSearch(key);
    }
}
