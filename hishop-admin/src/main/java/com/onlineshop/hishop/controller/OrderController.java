package com.onlineshop.hishop.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.OrderDetail;
import com.onlineshop.hishop.pojo.TbOrder;
import com.onlineshop.hishop.service.OrderService;
import com.onlineshop.hishop.utils.AlipayConfig;
import com.onlineshop.hishop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/admin/order/list",method = RequestMethod.GET)
    public DataTablesResult getOrderList(@RequestParam int draw, @RequestParam int start,
                                         @RequestParam int length, @RequestParam("search[value]") String search,
                                         @RequestParam("order[0][column]") int orderCol, @RequestParam("order[0][dir]") String orderDir){

        //获取客户端需要排序的列
        String[] cols = {"checkbox","order_id", "payment","shipping_code", "user_id", "buyer_nick", "create_time", "update_time", "payment_time","refund_success_time", "close_time","end_time","status"};
        String orderColumn = cols[orderCol];
        //默认排序列
        if(orderColumn == null) {
            orderColumn = "create_time";
        }
        //获取排序方式 默认为desc(asc)
        if(orderDir == null) {
            orderDir = "desc";
        }
        DataTablesResult result= orderService.getOrderList(draw,start,length,search,orderColumn,orderDir);
        return result;
    }

    @RequestMapping(value = "/admin/order/count",method = RequestMethod.GET)
    public Result<Object> getOrderCount(){

        Long result=orderService.countOrder();
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/admin/order/detail/{orderId}",method = RequestMethod.GET)
    public Result<Object> getOrderDetail(@PathVariable String orderId){

        OrderDetail orderDetail=orderService.getOrderDetail(orderId);
        return new ResultUtil<Object>().setData(orderDetail);
    }

    @RequestMapping(value = "/admin/order/remark",method = RequestMethod.POST)
    public Result<Object> remark(@RequestParam String orderId,
                                 @RequestParam(required = false) String message){

        orderService.remark(orderId,message);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/order/deliver",method = RequestMethod.POST)
    public Result<Object> deliver(@RequestParam String orderId,
                                  @RequestParam String shippingName,
                                  @RequestParam String shippingCode,
                                  @RequestParam BigDecimal postFee){

        orderService.deliver(orderId, shippingName, shippingCode, postFee);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/order/cancel/{orderId}",method = RequestMethod.GET)
    public Result<Object> cancelOrderByAdmin(@PathVariable String orderId){

        orderService.cancelOrderByAdmin(orderId);
        return new ResultUtil<Object>().setData(null);
    }
    @RequestMapping(value = "/admin/order/paid/{orderId}",method = RequestMethod.GET)
    public Result<Object> paid(@PathVariable String orderId){
        TbOrder tbOrder = orderService.selectById(orderId);
        tbOrder.setPaymentTime(new Date());
        tbOrder.setUpdateTime(new Date());
        tbOrder.setStatus(2);
        int result = orderService.updateById(tbOrder);
        return new ResultUtil<Object>().setData(result);
    }
    @RequestMapping(value = "/admin/order/refunded/{orderId}",method = RequestMethod.GET)
    public Result<Object> refunded(@PathVariable String orderId){
        TbOrder tbOrder = orderService.selectById(orderId);
        tbOrder.setRefundSuccessTime(new Date());
        tbOrder.setUpdateTime(new Date());
        tbOrder.setStatus(8);
        int result = orderService.updateById(tbOrder);
        return new ResultUtil<Object>().setData(result);
    }
    @RequestMapping(value = "/admin/order/refundAgree/{orderId}",method = RequestMethod.GET)
    public Result<Object> refundAgree(@PathVariable String orderId) {
        TbOrder tbOrder = orderService.selectById(orderId);
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeRefundModel refundModel = new AlipayTradeRefundModel();
        refundModel.setOutTradeNo(orderId);
//        refundModel.setTradeNo("2019053122001401581000086386");
        refundModel.setRefundAmount(String.valueOf(tbOrder.getPayment()));
        refundModel.setRefundReason("正常退款");
//        refundModel.setOutRequestNo("155954693408364");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizModel(refundModel);
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if ("10000".equals(response.getCode())){
                tbOrder.setRefundSuccessTime(new Date());
                tbOrder.setStatus(8);
                int result = orderService.updateById(tbOrder);
                return new ResultUtil<Object>().setData(result);
            }
        }catch (AlipayApiException e){
            e.printStackTrace();
        }

        return new ResultUtil<Object>().setErrorMsg("退款失败！");
    }

    @RequestMapping(value = "/admin/order/refundRefuse/{orderId}",method = RequestMethod.GET)
    public Result<Object> refundRefuse(@PathVariable String orderId){
        TbOrder tbOrder = orderService.selectById(orderId);
        tbOrder.setUpdateTime(new Date());
        tbOrder.setStatus(9);
        int result = orderService.updateById(tbOrder);
        return new ResultUtil<Object>().setData(result);
    }
    @RequestMapping(value = "/admin/order/del/{ids}",method = RequestMethod.DELETE)
    public Result<Object> getUserInfo(@PathVariable String[] ids){

        for(String id:ids){
            orderService.deleteOrder(id);
        }
        return new ResultUtil<Object>().setData(null);
    }
}
