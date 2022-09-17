package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.front.AliPayParam;
import com.onlineshop.hishop.dto.front.Order;
import com.onlineshop.hishop.dto.front.OrderInfo;
import com.onlineshop.hishop.dto.front.PageOrder;
import com.onlineshop.hishop.feign.OrderFeignService;
import com.onlineshop.hishop.pojo.TbOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(description = "订单")
public class OrderController {

    @Autowired
    private OrderFeignService orderFeignService;

    @RequestMapping(value = "/member/orderList", method = RequestMethod.GET)
    @ApiOperation(value = "获得用户所有订单")
    public Result<PageOrder> getOrderList(@RequestParam String userId,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "5") int size) {
        return orderFeignService.getOrderList(userId, page, size);
    }

    @RequestMapping(value = "/member/orderDetail", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取订单")
    public Result<Order> getOrder(@RequestParam String orderId) {
        return orderFeignService.getOrder(orderId);
    }

    @RequestMapping(value = "/member/addOrder", method = RequestMethod.POST)
    @ApiOperation(value = "创建订单")
    public Result<Object> addOrder(@RequestBody OrderInfo orderInfo) {
        return orderFeignService.addOrder(orderInfo);
    }

    @RequestMapping(value = "/member/cancelOrder", method = RequestMethod.POST)
    @ApiOperation(value = "取消订单")
    public Result<Object> cancelOrder(@RequestBody Order order) {
        return orderFeignService.cancelOrder(order);
    }

    @RequestMapping(value = "/member/delOrder", method = RequestMethod.GET)
    @ApiOperation(value = "删除订单")
    public Result<Object> delOrder(@RequestParam String orderId) {
        return orderFeignService.delOrder(orderId);

    }

    @RequestMapping(value = "/member/toAliPay", method = RequestMethod.POST)
    @ApiOperation(value = "支付订单")
    public Result<Object> payOrder(@RequestBody AliPayParam aliPayParam) {
        return orderFeignService.payOrder(aliPayParam);

    }

    @RequestMapping(value = "/member/paid", method = RequestMethod.GET)
    @ApiOperation(value = "支付完成")
    public Result<TbOrder> paid(@RequestParam String oid) {
        return orderFeignService.paid(oid);
    }

    @RequestMapping(value = "/member/confirm", method = RequestMethod.GET)
    @ApiOperation(value = "确认收货")
    public Result<TbOrder> confirm(@RequestParam String orderId) {
        return orderFeignService.confirm(orderId);
    }

    @RequestMapping(value = "/member/refund", method = RequestMethod.GET)
    @ApiOperation(value = "申请退款")
    public Result<TbOrder> refund(@RequestParam String orderId) {
        return orderFeignService.refund(orderId);
    }

    @RequestMapping(value = "/member/cancelRefund", method = RequestMethod.GET)
    @ApiOperation(value = "取消申请退款")
    public Result<TbOrder> cancel(@RequestParam String orderId) {
        return orderFeignService.confirm(orderId);
    }

}
