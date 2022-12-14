package com.onlineshop.hishop.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.front.AliPayParam;
import com.onlineshop.hishop.dto.front.Order;
import com.onlineshop.hishop.dto.front.OrderInfo;
import com.onlineshop.hishop.dto.front.PageOrder;
import com.onlineshop.hishop.pojo.TbOrder;
import com.onlineshop.hishop.service.OrderService;
import com.onlineshop.hishop.utils.AlipayConfig;
import com.onlineshop.hishop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/front")
@PropertySource("classpath:conf/order.yml")
public class OrderController {

    @Value("${notify_url}")
    private String notify_url;

    @Value("${return_url}")
    private String return_url;

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/member/orderList", method = RequestMethod.GET)
    public Result<PageOrder> getOrderList(String userId,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "5") int size) {
        PageOrder pageOrder = orderService.getOrderList(Long.valueOf(userId), page, size);
        return new ResultUtil<PageOrder>().setData(pageOrder);
    }

    @RequestMapping(value = "/member/orderDetail", method = RequestMethod.GET)
    public Result<Order> getOrder(String orderId) {
        Order order = orderService.getOrder(Long.valueOf(orderId));
        return new ResultUtil<Order>().setData(order);
    }

    @RequestMapping(value = "/member/addOrder", method = RequestMethod.POST)
    public Result<Object> addOrder(@RequestBody OrderInfo orderInfo) {
        Long orderId = orderService.createOrder(orderInfo);
        return new ResultUtil<Object>().setData(orderId.toString());
    }

    @RequestMapping(value = "/member/cancelOrder", method = RequestMethod.POST)
    public Result<Object> cancelOrder(@RequestBody Order order) {

        int result = orderService.cancelOrder(order.getOrderId());
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/delOrder", method = RequestMethod.GET)
    public Result<Object> delOrder(String orderId) {

        int result = orderService.delOrder(Long.valueOf(orderId));
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/toAliPay", method = RequestMethod.POST)
    public Result<Object> payOrder(@RequestBody AliPayParam aliPayParam) {

        //??????????????????AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.public_key, AlipayConfig.sign_type);
        //??????????????????
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url + "?orderId=" + aliPayParam.getOrderCode() + "&price=" + aliPayParam.getTotal());
        alipayRequest.setNotifyUrl(notify_url);
        //?????????????????????????????????????????????????????????????????????
        String out_trade_no = aliPayParam.getOrderCode();
        //?????????????????????
        String total_amount = String.valueOf(aliPayParam.getTotal());
        //?????????????????????
        String subject = aliPayParam.getOrderName();
        //?????????????????????
        String body = aliPayParam.getOrderName();

        // ?????????????????????????????????????????????????????????????????????????????????1m???15d???m-?????????h-?????????d-??????1c-?????????1c-??????????????????????????????????????????????????????0??????????????? ???????????????????????????????????? ??? 1.5h??????????????? 90m???
        String timeout_express = "1c";

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"timeout_express\":\"" + timeout_express + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //??????
        try {
            String result = alipayClient.pageExecute(alipayRequest).getBody();
            return new ResultUtil<Object>().setData(result);
        }catch (AlipayApiException e){
            e.printStackTrace();
            return new ResultUtil<Object>().setData(e.toString());
        }

    }

    @RequestMapping(value = "/member/paid", method = RequestMethod.GET)
    public Result<TbOrder> paid(String oid) {
        TbOrder order = orderService.getById(oid);
        order.setPaymentTime(new Date());
        order.setStatus(2);
        order.setUpdateTime(new Date());
        orderService.updateById(order);
        return new ResultUtil<TbOrder>().setData(order);
    }

    @RequestMapping(value = "/member/confirm", method = RequestMethod.GET)
    public Result<TbOrder> confirm(String orderId) {
        TbOrder order = orderService.getById(orderId);
        order.setStatus(4);
        orderService.updateById(order);
        return new ResultUtil<TbOrder>().setData(order);
    }

    @RequestMapping(value = "/member/refund", method = RequestMethod.GET)
    public Result<TbOrder> refund(String orderId) {
        TbOrder order = orderService.getById(orderId);
        order.setRefundTime(new Date());
        order.setStatus(7);
        orderService.updateById(order);
        return new ResultUtil<TbOrder>().setData(order);
    }

    @RequestMapping(value = "/member/cancelRefund", method = RequestMethod.GET)
    public Result<TbOrder> cancel(String orderId) {
        TbOrder order = orderService.getById(orderId);
        order.setStatus(9);
        orderService.updateById(order);
        return new ResultUtil<TbOrder>().setData(order);
    }

}
