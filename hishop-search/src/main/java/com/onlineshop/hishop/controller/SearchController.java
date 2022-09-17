package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.front.SearchResult;
import com.onlineshop.hishop.service.SearchService;
import com.onlineshop.hishop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/front")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/goods/search", method = RequestMethod.GET)
    public Result<SearchResult> searchProduct(@RequestParam(defaultValue = "") String key,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "20") int size,
                                              @RequestParam(defaultValue = "") String sort,
                                              @RequestParam(defaultValue = "-1") int priceGt,
                                              @RequestParam(defaultValue = "-1") int priceLte) {

        SearchResult searchResult = searchService.search(key, page, size, sort, priceGt, priceLte);
        return new ResultUtil<SearchResult>().setData(searchResult);
    }

    @RequestMapping(value = "/goods/quickSearch", produces = "text/plain;charset=UTF-8", method = RequestMethod.GET)
    public String getQuickSearch(@RequestParam(defaultValue = "") String key) {

        return searchService.quickSearch(key);
    }
}
