package com.onlineshop.hishop.controller;


import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.service.ContentService;
import com.onlineshop.hishop.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(description = "缓存管理")
public class RedisController {

    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/admin/redis/index/list",method = RequestMethod.GET)
    @ApiOperation(value = "获取首页缓存")
    public Result<Object> getIndexRedis(){

        String json = contentService.getIndexRedis();
        return new ResultUtil<Object>().setData(json);
    }

    @RequestMapping(value = "/admin/redis/index/update",method = RequestMethod.GET)
    @ApiOperation(value = "刷新首页缓存")
    public Result<Object> updateIndexRedis(){

        contentService.updateIndexRedis();
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/redis/recommend/list",method = RequestMethod.GET)
    @ApiOperation(value = "获取推荐板块缓存")
    public Result<Object> getRecommendRedis(){

        String json = contentService.getRecommendRedis();
        return new ResultUtil<Object>().setData(json);
    }

    @RequestMapping(value = "/admin/redis/recommend/update",method = RequestMethod.GET)
    @ApiOperation(value = "刷新推荐板块缓存")
    public Result<Object> updateRecommendRedis(){

        contentService.updateRecommendRedis();
        return new ResultUtil<Object>().setData(null);
    }

}
