package com.onlineshop.hishop.feign;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.front.AliPayParam;
import com.onlineshop.hishop.dto.front.Order;
import com.onlineshop.hishop.dto.front.OrderInfo;
import com.onlineshop.hishop.dto.front.PageOrder;
import com.onlineshop.hishop.pojo.TbOrder;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient("hishop-order")
public interface OrderFeignService {

    @RequestMapping(value = "/front/member/orderList", method = RequestMethod.GET)
    @ApiOperation(value = "获得用户所有订单")
    public Result<PageOrder> getOrderList(@RequestParam String userId,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "5") int size);

    @RequestMapping(value = "/front/member/orderDetail", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取订单")
    public Result<Order> getOrder(@RequestParam String orderId);
    
    @RequestMapping(value = "/front/member/addOrder", method = RequestMethod.POST)
    public Result<Object> addOrder(@RequestBody OrderInfo orderInfo);

    @RequestMapping(value = "/front/member/cancelOrder", method = RequestMethod.POST)
    public Result<Object> cancelOrder(@RequestBody Order order);

    @RequestMapping(value = "/front/member/delOrder", method = RequestMethod.GET)
    public Result<Object> delOrder(@RequestParam String orderId);

    @RequestMapping(value = "/front/member/toAliPay", method = RequestMethod.POST)
    public Result<Object> payOrder(@RequestBody AliPayParam aliPayParam);

    @RequestMapping(value = "/front/member/paid", method = RequestMethod.GET)
    public Result<TbOrder> paid(@RequestParam String oid);

    @RequestMapping(value = "/front/member/confirm", method = RequestMethod.GET)
    public Result<TbOrder> confirm(@RequestParam String orderId);

    @RequestMapping(value = "/front/member/refund", method = RequestMethod.GET)
    public Result<TbOrder> refund(@RequestParam String orderId);

    @RequestMapping(value = "/front/member/cancelRefund", method = RequestMethod.GET)
    public Result<TbOrder> cancel(@RequestParam String orderId);

}
