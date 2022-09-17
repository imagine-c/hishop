package com.onlineshop.hishop.controller;


import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.feign.RedisFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@Api(description = "缓存管理")
public class RedisController {

    @Autowired
    private RedisFeignService redisFeignService;

    @RequestMapping(value = "/redis/index/list",method = RequestMethod.GET)
    @ApiOperation(value = "获取首页缓存")
    public Result<Object> getIndexRedis(){
        
        return redisFeignService.getIndexRedis();
    }

    @RequestMapping(value = "/redis/index/update",method = RequestMethod.GET)
    @ApiOperation(value = "刷新首页缓存")
    public Result<Object> updateIndexRedis(){
        
        return redisFeignService.updateIndexRedis();
    }

    @RequestMapping(value = "/redis/recommend/list",method = RequestMethod.GET)
    @ApiOperation(value = "获取推荐板块缓存")
    public Result<Object> getRecommendRedis(){
        
        return redisFeignService.getRecommendRedis();
    }

    @RequestMapping(value = "/redis/recommend/update",method = RequestMethod.GET)
    @ApiOperation(value = "刷新推荐板块缓存")
    public Result<Object> updateRecommendRedis(){
        
        return  redisFeignService.updateRecommendRedis();
    }

}
