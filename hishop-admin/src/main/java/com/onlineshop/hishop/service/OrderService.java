package com.onlineshop.hishop.service;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.dto.OrderDetail;
import com.onlineshop.hishop.pojo.TbOrder;

import java.math.BigDecimal;


/**
 * @author Exrickx
 */
public interface OrderService {

    /**
     * 获得订单列表
     * @param draw
     * @param start
     * @param length
     * @param search
     * @param orderCol
     * @param orderDir
     * @return
     */
    DataTablesResult getOrderList(int draw, int start, int length, String search, String orderCol, String orderDir);

    /**
     * 统计订单数
     * @return
     */
    Long countOrder();

    /**
     * 获取订单详情
     * @param orderId
     * @return
     */
    OrderDetail getOrderDetail(String orderId);

    /**
     * 发货
     * @param orderId
     * @param shippingName
     * @param shippingCode
     * @param postFee
     * @return
     */
    int deliver(String orderId, String shippingName, String shippingCode, BigDecimal postFee);

    /**
     * 备注
     * @param orderId
     * @param message
     * @return
     */
    int remark(String orderId, String message);

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    int cancelOrderByAdmin(String orderId);

    /**
     * 删除订单
     * @param id
     * @return
     */
    int deleteOrder(String id);

    /**
     * 定时取消订单
     */
    int cancelOrder();

    TbOrder selectById(String oid);

    int updateById(TbOrder tbOrder);

    int confirmOrder();

    int autoRefund();

    int deliverEmail();
}
