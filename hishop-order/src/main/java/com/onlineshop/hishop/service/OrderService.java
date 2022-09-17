package com.onlineshop.hishop.service;

import com.onlineshop.hishop.dto.front.Order;
import com.onlineshop.hishop.dto.front.OrderInfo;
import com.onlineshop.hishop.dto.front.PageOrder;
import com.onlineshop.hishop.pojo.TbOrder;

public interface OrderService {

    /**
     * 分页获得用户订单
     *
     * @param userId
     * @param page
     * @param size
     * @return
     */
    PageOrder getOrderList(Long userId, int page, int size);

    /**
     * 获得单个订单
     *
     * @param orderId
     * @return
     */
    Order getOrder(Long orderId);

    /**
     * 取消订单
     *
     * @param orderId
     * @return
     */
    int cancelOrder(Long orderId);

    /**
     * 创建订单
     *
     * @param orderInfo
     * @return
     */
    Long createOrder(OrderInfo orderInfo);

    /**
     * 删除订单
     *
     * @param orderId
     * @return
     */
    int delOrder(Long orderId);

    TbOrder getById(String oid);

    int updateById(TbOrder tbOrder);

}
