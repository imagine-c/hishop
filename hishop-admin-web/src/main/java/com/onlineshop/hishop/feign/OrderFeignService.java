package com.onlineshop.hishop.feign;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Component
@FeignClient(value = "hishop-admin")
public interface OrderFeignService {

    @RequestMapping(value = "/admin/order/list",method = RequestMethod.GET)
    public DataTablesResult getOrderList(@RequestParam int draw, @RequestParam int start,
                                         @RequestParam int length, @RequestParam("search[value]") String search,
                                         @RequestParam("order[0][column]") int orderCol, @RequestParam("order[0][dir]") String orderDir);

    @RequestMapping(value = "/admin/order/count",method = RequestMethod.GET)
    public Result<Object> getOrderCount();

    @RequestMapping(value = "/admin/order/detail/{orderId}",method = RequestMethod.GET)
    public Result<Object> getOrderDetail(@PathVariable String orderId);

    @RequestMapping(value = "/admin/order/remark",method = RequestMethod.POST)
    public Result<Object> remark(@RequestParam String orderId,
                                 @RequestParam(required = false) String message);

    @RequestMapping(value = "/admin/order/deliver",method = RequestMethod.POST)
    public Result<Object> deliver(@RequestParam String orderId,
                                  @RequestParam String shippingName,
                                  @RequestParam String shippingCode,
                                  @RequestParam BigDecimal postFee);

    @RequestMapping(value = "/admin/order/cancel/{orderId}",method = RequestMethod.GET)
    public Result<Object> cancelOrderByAdmin(@PathVariable String orderId);

    @RequestMapping(value = "/admin/order/paid/{orderId}",method = RequestMethod.GET)
    public Result<Object> paid(@PathVariable String orderId);

    @RequestMapping(value = "/admin/order/refunded/{orderId}",method = RequestMethod.GET)
    public Result<Object> refunded(@PathVariable String orderId);

    @RequestMapping(value = "/admin/order/refundAgree/{orderId}",method = RequestMethod.GET)
    public Result<Object> refundAgree(@PathVariable String orderId);

    @RequestMapping(value = "/admin/order/refundRefuse/{orderId}",method = RequestMethod.GET)

    public Result<Object> refundRefuse(@PathVariable String orderId);
    @RequestMapping(value = "/admin/order/del/{ids}",method = RequestMethod.DELETE)

    public Result<Object> getUserInfo(@PathVariable String[] ids);
}
