package com.onlineshop.hishop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
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
import com.onlineshop.hishop.service.SsoOrderService;
import com.onlineshop.hishop.utils.AlipayConfig;
import com.onlineshop.hishop.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Api(description = "订单")
@PropertySource("classpath:conf/front.properties")
public class OrderController {

    @Value("${notify_url}")
    private String notify_url;

    @Value("${return_url}")
    private String return_url;


    @Reference
    private SsoOrderService orderService;

    @RequestMapping(value = "/member/orderList",method = RequestMethod.GET)
    @ApiOperation(value = "获得用户所有订单")
    public Result<PageOrder> getOrderList(String userId,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "5") int size){
        PageOrder pageOrder=orderService.getOrderList(Long.valueOf(userId), page, size);
        return new ResultUtil<PageOrder>().setData(pageOrder);
    }

    @RequestMapping(value = "/member/orderDetail",method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取订单")
    public Result<Order> getOrder(String orderId){
        Order order=orderService.getOrder(Long.valueOf(orderId));
        return new ResultUtil<Order>().setData(order);
    }

    @RequestMapping(value = "/member/addOrder",method = RequestMethod.POST)
    @ApiOperation(value = "创建订单")
    public Result<Object> addOrder(@RequestBody OrderInfo orderInfo){
        Long orderId=orderService.createOrder(orderInfo);
        return new ResultUtil<Object>().setData(orderId.toString());
    }

    @RequestMapping(value = "/member/cancelOrder",method = RequestMethod.POST)
    @ApiOperation(value = "取消订单")
    public Result<Object> cancelOrder(@RequestBody Order order){

        int result=orderService.cancelOrder(order.getOrderId());
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/delOrder",method = RequestMethod.GET)
    @ApiOperation(value = "删除订单")
    public Result<Object> delOrder(String orderId){

        int result=orderService.delOrder(Long.valueOf(orderId));
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/toAliPay",method = RequestMethod.POST)
    @ApiOperation(value = "支付订单")
    public Result<Object> payOrder(@RequestBody AliPayParam aliPayParam) throws AlipayApiException {

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.public_key, AlipayConfig.sign_type);
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url+"?orderId="+aliPayParam.getOrderCode()+"&price="+aliPayParam.getTotal());
        alipayRequest.setNotifyUrl(notify_url);
        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = aliPayParam.getOrderCode();
        //付款金额，必填
        String total_amount = String.valueOf(aliPayParam.getTotal());
        //订单名称，必填
        String subject = aliPayParam.getOrderName();
        //商品描述，可空
        String body = aliPayParam.getOrderName();

        // 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
        String timeout_express = "1c";

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"timeout_express\":\""+ timeout_express +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/paid",method = RequestMethod.GET)
    @ApiOperation(value = "支付完成")
    public Result<TbOrder> paid(String oid)  {
        TbOrder order = orderService.getById(oid);
        order.setPaymentTime(new Date());
        order.setStatus(2);
        order.setUpdateTime(new Date());
        orderService.updateById(order);
        return new ResultUtil<TbOrder>().setData(order);
    }

    @RequestMapping(value = "/member/confirm",method = RequestMethod.GET)
    @ApiOperation(value = "确认收货")
    public Result<TbOrder> confirm(String orderId)  {
        TbOrder order = orderService.getById(orderId);
        order.setStatus(4);
        orderService.updateById(order);
        return new ResultUtil<TbOrder>().setData(order);
    }
    @RequestMapping(value = "/member/refund",method = RequestMethod.GET)
    @ApiOperation(value = "申请退款")
    public Result<TbOrder> refund(String orderId)  {
        TbOrder order = orderService.getById(orderId);
        order.setRefundTime(new Date());
        order.setStatus(7);
        orderService.updateById(order);
        return new ResultUtil<TbOrder>().setData(order);
    }
    @RequestMapping(value = "/member/cancelRefund",method = RequestMethod.GET)
    @ApiOperation(value = "取消申请退款")
    public Result<TbOrder> cancel(String orderId)  {
        TbOrder order = orderService.getById(orderId);
        order.setStatus(9);
        orderService.updateById(order);
        return new ResultUtil<TbOrder>().setData(order);
    }

}
