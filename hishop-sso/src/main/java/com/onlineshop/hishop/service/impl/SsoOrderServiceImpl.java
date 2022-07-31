package com.onlineshop.hishop.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.onlineshop.hishop.dto.DtoUtil;
import com.onlineshop.hishop.dto.front.CartProduct;
import com.onlineshop.hishop.dto.front.Order;
import com.onlineshop.hishop.dto.front.OrderInfo;
import com.onlineshop.hishop.dto.front.PageOrder;
import com.onlineshop.hishop.exception.HiShopException;
import com.onlineshop.hishop.jedis.JedisClient;
import com.onlineshop.hishop.mapper.*;
import com.onlineshop.hishop.pojo.*;
import com.onlineshop.hishop.service.SsoOrderService;
import com.onlineshop.hishop.utils.IDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Component
@PropertySource("classpath:conf/sso.properties")
public class SsoOrderServiceImpl implements SsoOrderService {

    private final static Logger log= LoggerFactory.getLogger(SsoOrderServiceImpl.class);

    @Autowired
    private TbMemberMapper tbMemberMapper;    //用户
    @Autowired
    private TbOrderMapper tbOrderMapper;    //订单
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;  //订单商品
    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;  //订单物流

    @Autowired
    private JedisClient jedisClient;

    @Value("${CART_PRE}")
    private String CART_PRE;


    @Override
    public PageOrder getOrderList(Long userId, int page, int size) {
        //分页
        if(page<=0) {
            page = 1;
        }
        PageHelper.startPage(page,size);

        PageOrder pageOrder=new PageOrder();
        List<Order> list=new ArrayList<>();

        TbOrderExample example=new TbOrderExample();
        TbOrderExample.Criteria criteria= example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        example.setOrderByClause("create_time DESC");
        List<TbOrder> listOrder =tbOrderMapper.selectByExample(example);
        for(TbOrder tbOrder:listOrder){
            judgeOrder(tbOrder);
            Order order=new Order();
            //orderId
            order.setOrderId(Long.valueOf(tbOrder.getOrderId()));
            //orderStatus
            order.setOrderStatus(String.valueOf(tbOrder.getStatus()));
            //createDate
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = formatter.format(tbOrder.getCreateTime());
            order.setCreateDate(date);
            order.setConsign(tbOrder.getIsConsign());
            if (tbOrder.getRefundTime()!=null)
                order.setRefundDate(formatter.format(tbOrder.getRefundTime()));
            if (tbOrder.getRefundSuccessTime()!=null)
                order.setRefundSuccessDate(formatter.format(tbOrder.getRefundSuccessTime()));
            //address
            TbOrderShipping tbOrderShipping=tbOrderShippingMapper.selectByPrimaryKey(tbOrder.getOrderId());
            TbAddress address=new TbAddress();
            address.setUserName(tbOrderShipping.getReceiverName());
            address.setDetailed(tbOrderShipping.getReceiverAddress());
            address.setTel(tbOrderShipping.getReceiverPhone());
            order.setAddressInfo(address);
            //orderTotal
            if(tbOrder.getPayment()==null){
                order.setOrderTotal(new BigDecimal(0));
            }else{
                order.setOrderTotal(tbOrder.getPayment());
            }
            //goodsList
            TbOrderItemExample exampleItem=new TbOrderItemExample();
            TbOrderItemExample.Criteria criteriaItem= exampleItem.createCriteria();
            criteriaItem.andOrderIdEqualTo(tbOrder.getOrderId());
            List<TbOrderItem> listItem =tbOrderItemMapper.selectByExample(exampleItem);
            List<CartProduct> listProduct=new ArrayList<>();
            for(TbOrderItem tbOrderItem:listItem){

                CartProduct cartProduct= DtoUtil.TbOrderItem2CartProduct(tbOrderItem);

                listProduct.add(cartProduct);
            }
            order.setGoodsList(listProduct);
            list.add(order);
        }

        PageInfo<Order> pageInfo=new PageInfo<>(list);
        pageOrder.setTotal(getMemberOrderCount(userId));
        pageOrder.setData(list);
        return pageOrder;
    }

    @Override
    public Order getOrder(Long orderId) {

        Order order=new Order();

        TbOrder tbOrder=tbOrderMapper.selectByPrimaryKey(String.valueOf(orderId));
        if(tbOrder==null){
            throw new HiShopException("通过id获取订单失败");
        }

        String validTime=judgeOrder(tbOrder);
        if(validTime!=null){
            order.setFinishDate(validTime);
        }

        //orderId
        order.setOrderId(Long.valueOf(tbOrder.getOrderId()));
        //orderStatus
        order.setOrderStatus(String.valueOf(tbOrder.getStatus()));
        //createDate
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String createDate = formatter.format(tbOrder.getCreateTime());
        order.setCreateDate(createDate);
        //payDate
        if(tbOrder.getPaymentTime()!=null){
            String payDate = formatter.format(tbOrder.getPaymentTime());
            order.setPayDate(payDate);
        }
        //closeDate
        if(tbOrder.getCloseTime()!=null){
            String closeDate = formatter.format(tbOrder.getCloseTime());
            order.setCloseDate(closeDate);
        }
        //finishDate
        if(tbOrder.getEndTime()!=null&&tbOrder.getStatus()==4){
            String finishDate = formatter.format(tbOrder.getEndTime());
            order.setFinishDate(finishDate);
        }
        //address
        TbOrderShipping tbOrderShipping=tbOrderShippingMapper.selectByPrimaryKey(tbOrder.getOrderId());
        TbAddress address=new TbAddress();
        address.setUserName(tbOrderShipping.getReceiverName());
        address.setDetailed(tbOrderShipping.getReceiverAddress());
        address.setTel(tbOrderShipping.getReceiverPhone());
        order.setAddressInfo(address);
        //orderTotal
        if(tbOrder.getPayment()==null){
            order.setOrderTotal(new BigDecimal(0));
        }else{
            order.setOrderTotal(tbOrder.getPayment());
        }
        //goodsList
        TbOrderItemExample exampleItem=new TbOrderItemExample();
        TbOrderItemExample.Criteria criteriaItem= exampleItem.createCriteria();
        criteriaItem.andOrderIdEqualTo(tbOrder.getOrderId());
        List<TbOrderItem> listItem =tbOrderItemMapper.selectByExample(exampleItem);
        List<CartProduct> listProduct=new ArrayList<>();
        for(TbOrderItem tbOrderItem:listItem){

            CartProduct cartProduct= DtoUtil.TbOrderItem2CartProduct(tbOrderItem);

            listProduct.add(cartProduct);
        }
        order.setGoodsList(listProduct);
        return order;
    }

    @Override
    public int cancelOrder(Long orderId) {

        TbOrder tbOrder=tbOrderMapper.selectByPrimaryKey(String.valueOf(orderId));
        if(tbOrder==null){
            throw new HiShopException("通过id获取订单失败");
        }
        tbOrder.setStatus(5);
        tbOrder.setCloseTime(new Date());
        if(tbOrderMapper.updateByPrimaryKey(tbOrder)!=1){
            throw new HiShopException("取消订单失败");
        }
        return 1;
    }

    @Override
    public Long createOrder(OrderInfo orderInfo) {

        TbMember member=tbMemberMapper.selectByPrimaryKey(Long.valueOf(orderInfo.getUserId()));
        if(member==null){
            throw new HiShopException("获取下单用户失败");
        }

        TbOrder order=new TbOrder();
        //生成订单ID
        Long orderId = IDUtil.getRandomId();
        order.setOrderId(String.valueOf(orderId));
        order.setUserId(Long.valueOf(orderInfo.getUserId()));
        order.setBuyerNick(member.getUsername());
        order.setPayment(orderInfo.getOrderTotal());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        //0、未付款，1、已付款，2、未发货，3、已发货，4、交易成功，5、交易关闭，6、交易失败
        order.setStatus(0);
        order.setIsConsign(false);

        if(tbOrderMapper.insert(order)!=1){
            throw new HiShopException("生成订单失败");
        }

        List<CartProduct> list=orderInfo.getGoodsList();
        for(CartProduct cartProduct:list){
            TbOrderItem orderItem=new TbOrderItem();
            //生成订单商品ID
            Long orderItemId = IDUtil.getRandomId();
            orderItem.setId(String.valueOf(orderItemId));
            orderItem.setItemId(String.valueOf(cartProduct.getProductId()));
            orderItem.setOrderId(String.valueOf(orderId));
            orderItem.setNum(Math.toIntExact(cartProduct.getProductNum()));
            orderItem.setPrice(cartProduct.getSalePrice());
            orderItem.setTitle(cartProduct.getProductName());
            orderItem.setPicPath(cartProduct.getProductImg());
            orderItem.setTotalFee(cartProduct.getSalePrice().multiply(BigDecimal.valueOf(cartProduct.getProductNum())));

            if(tbOrderItemMapper.insert(orderItem)!=1){
                throw new HiShopException("生成订单商品失败");
            }

            //删除购物车中含该订单的商品
            try{
                List<String> jsonList = jedisClient.hvals(CART_PRE + ":" + orderInfo.getUserId());
                for (String json : jsonList) {
                    CartProduct cart = new Gson().fromJson(json,CartProduct.class);
                    if(cart.getProductId().equals(cartProduct.getProductId())){
                        jedisClient.hdel(CART_PRE + ":" + orderInfo.getUserId(),cart.getProductId()+"");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //物流表
        TbOrderShipping orderShipping=new TbOrderShipping();
        orderShipping.setOrderId(String.valueOf(orderId));
        orderShipping.setReceiverName(orderInfo.getUserName());
        orderShipping.setReceiverAddress(orderInfo.getProvinceName()+orderInfo.getCityName()+
                orderInfo.getAreaName()+orderInfo.getStreetName()+orderInfo.getDetailed());
        orderShipping.setReceiverPhone(orderInfo.getTel());
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        if(tbOrderShippingMapper.insert(orderShipping)!=1){
            throw new HiShopException("生成物流信息失败");
        }

        return orderId;
    }

    @Override
    public int delOrder(Long orderId) {

        if(tbOrderMapper.deleteByPrimaryKey(String.valueOf(orderId))!=1){
            throw new HiShopException("删除订单失败");
        }

        TbOrderItemExample example=new TbOrderItemExample();
        TbOrderItemExample.Criteria criteria= example.createCriteria();
        criteria.andOrderIdEqualTo(String.valueOf(orderId));
        List<TbOrderItem> list =tbOrderItemMapper.selectByExample(example);
        for(TbOrderItem tbOrderItem:list){
            if(tbOrderItemMapper.deleteByPrimaryKey(tbOrderItem.getId())!=1){
                throw new HiShopException("删除订单商品失败");
            }
        }

        if(tbOrderShippingMapper.deleteByPrimaryKey(String.valueOf(orderId))!=1){
            throw new HiShopException("删除物流失败");
        }
        return 1;
    }

    @Override
    public TbOrder getById(String oid) {
        return tbOrderMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateById(TbOrder tbOrder) {
        if (tbOrderMapper.updateByPrimaryKey(tbOrder)!=1){
            throw new HiShopException("更新订单失败");
        }
        return 1;

    }

    /**
     * 判断订单是否超时未支付
     */
    public String judgeOrder(TbOrder tbOrder){

        String result=null;
        if(tbOrder.getStatus()==0){
            //判断是否已超1天
            long diff=System.currentTimeMillis()-tbOrder.getCreateTime().getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            if(days>=1){
                //设置失效
                tbOrder.setStatus(5);
                tbOrder.setCloseTime(new Date());
                if(tbOrderMapper.updateByPrimaryKey(tbOrder)!=1){
                    throw new HiShopException("更新订单失效失败");
                }
            }else {
                //返回到期时间
                long time=tbOrder.getCreateTime().getTime()+1000 * 60 * 60 * 24;
                result= String.valueOf(time);
            }
        }
        return result;
    }

    public int getMemberOrderCount(Long userId){

        TbOrderExample example=new TbOrderExample();
        TbOrderExample.Criteria criteria= example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<TbOrder> listOrder =tbOrderMapper.selectByExample(example);
        if(listOrder!=null){
            return listOrder.size();
        }
        return 0;
    }
}
