package com.onlineshop.hishop.feign;


import com.onlineshop.hishop.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Component
@FeignClient(value = "hishop-content")
public interface RedisFeignService {

    @RequestMapping(value = "/admin/redis/index/list",method = RequestMethod.GET)
    public Result<Object> getIndexRedis();

    @RequestMapping(value = "/admin/redis/index/update",method = RequestMethod.GET)
    public Result<Object> updateIndexRedis();

    @RequestMapping(value = "/admin/redis/recommend/list",method = RequestMethod.GET)
    public Result<Object> getRecommendRedis();
    @RequestMapping(value = "/admin/redis/recommend/update",method = RequestMethod.GET)

    public Result<Object> updateRecommendRedis();

}
