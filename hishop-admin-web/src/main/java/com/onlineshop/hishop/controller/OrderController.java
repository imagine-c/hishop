package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.feign.OrderFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@Api(description= "订单管理")
public class OrderController {

    @Autowired
    private OrderFeignService orderFeignService;

    @RequestMapping(value = "/order/list", method = RequestMethod.GET)
    @ApiOperation(value = "获取订单列表")
    public DataTablesResult getOrderList(@RequestParam int draw, @RequestParam int start,
                                         @RequestParam int length, @RequestParam("search[value]") String search,
                                         @RequestParam("order[0][column]") int orderCol, @RequestParam("order[0][dir]") String orderDir){

        return orderFeignService.getOrderList(draw,start,length,search,orderCol,orderDir);
    }

    @RequestMapping(value = "/order/count",method = RequestMethod.GET)
    @ApiOperation(value = "获取订单总数")
    public Result<Object> getOrderCount(){

        return orderFeignService.getOrderCount();
    }

    @RequestMapping(value = "/order/detail/{orderId}",method = RequestMethod.GET)
    @ApiOperation(value = "获取订单详情")
    public Result<Object> getOrderDetail(@PathVariable String orderId){

        return orderFeignService.getOrderDetail(orderId);
    }

    @RequestMapping(value = "/order/remark",method = RequestMethod.POST)
    @ApiOperation(value = "订单备注")
    public Result<Object> remark(@RequestParam String orderId,
                                 @RequestParam(required = false) String message){

        return orderFeignService.remark(orderId,message);
    }

    @RequestMapping(value = "/order/deliver",method = RequestMethod.POST)
    @ApiOperation(value = "订单发货")
    public Result<Object> deliver(@RequestParam String orderId,
                                  @RequestParam String shippingName,
                                  @RequestParam String shippingCode,
                                  @RequestParam BigDecimal postFee){

        return orderFeignService.deliver(orderId, shippingName, shippingCode, postFee);
    }

    @RequestMapping(value = "/order/cancel/{orderId}",method = RequestMethod.GET)
    @ApiOperation(value = "订单取消")
    public Result<Object> cancelOrderByAdmin(@PathVariable String orderId){

        return orderFeignService.cancelOrderByAdmin(orderId);
    }
    @RequestMapping(value = "/order/paid/{orderId}",method = RequestMethod.GET)
    @ApiOperation(value = "订单已付款")
    public Result<Object> paid(@PathVariable String orderId){

        return orderFeignService.paid(orderId);
    }
    @RequestMapping(value = "/order/refunded/{orderId}",method = RequestMethod.GET)
    @ApiOperation(value = "订单已退款")
    public Result<Object> refunded(@PathVariable String orderId){

        return orderFeignService.refunded(orderId);
    }
    @RequestMapping(value = "/order/refundAgree/{orderId}",method = RequestMethod.GET)
    @ApiOperation(value = "同意退款")
    public Result<Object> refundAgree(@PathVariable String orderId) {

        return orderFeignService.refundAgree(orderId);
    }

    @RequestMapping(value = "/order/refundRefuse/{orderId}",method = RequestMethod.GET)
    @ApiOperation(value = "拒绝退款")
    public Result<Object> refundRefuse(@PathVariable String orderId){

        return orderFeignService.refundRefuse(orderId);
    }
    @RequestMapping(value = "/order/del/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除订单")
    public Result<Object> getUserInfo(@PathVariable String[] ids){

        return orderFeignService.getUserInfo(ids);
    }
}
