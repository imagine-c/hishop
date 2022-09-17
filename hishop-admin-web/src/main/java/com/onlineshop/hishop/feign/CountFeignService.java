package com.onlineshop.hishop.feign;

import com.onlineshop.hishop.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Component
@FeignClient(value = "hishop-admin")
public interface CountFeignService {


    @RequestMapping(value = "/admin/count/order",method = RequestMethod.GET)
    public Result<Object> countOrder(@RequestParam int type,
                                     @RequestParam(required = false) String startTime,
                                     @RequestParam(required = false) String endTime,
                                     @RequestParam(required = false) int year);
}
