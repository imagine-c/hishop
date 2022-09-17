package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.feign.CountFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@Api(description = "统计")
public class CountController {

    @Autowired
    private CountFeignService countFeignService;

    @RequestMapping(value = "/count/order",method = RequestMethod.GET)
    @ApiOperation(value = "通过panelId获得板块内容列表")
    public Result<Object> countOrder(@RequestParam int type,
                                     @RequestParam(required = false) String startTime,
                                     @RequestParam(required = false) String endTime,
                                     @RequestParam(required = false) int year){

        return countFeignService.countOrder(type, startTime, endTime, year);
    }
}
