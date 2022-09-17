package com.onlineshop.hishop.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.dto.OrderDetail;
import com.onlineshop.hishop.exception.HiShopException;
import com.onlineshop.hishop.mapper.TbOrderItemMapper;
import com.onlineshop.hishop.mapper.TbOrderMapper;
import com.onlineshop.hishop.mapper.TbOrderShippingMapper;
import com.onlineshop.hishop.pojo.*;
import com.onlineshop.hishop.service.OrderService;
import com.onlineshop.hishop.utils.AlipayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper tbOrderMapper;
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;
    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;
    @Autowired
    private EmailUtil emailUtil;

    @Override
    public DataTablesResult getOrderList(int draw, int start, int length, String search, String orderCol, String orderDir) {

        DataTablesResult result=new DataTablesResult();
        //分页
        PageHelper.startPage(start/length+1,length);
        List<TbOrder> list = tbOrderMapper.selectByMulti("%"+search+"%",orderCol,orderDir);
        PageInfo<TbOrder> pageInfo=new PageInfo<>(list);

        result.setRecordsFiltered((int)pageInfo.getTotal());
        result.setRecordsTotal(Math.toIntExact(cancelOrder()));

        result.setDraw(draw);
        result.setData(list);
        return result;
    }

    @Override
    public Long countOrder() {

        TbOrderExample example=new TbOrderExample();
        Long result=tbOrderMapper.countByExample(example);
        if(result==null){
            throw new HiShopException("统计订单数目失败");
        }
        return result;
    }

    @Override
    public OrderDetail getOrderDetail(String orderId) {

        OrderDetail orderDetail = new OrderDetail();
        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(orderId);

        TbOrderItemExample example=new TbOrderItemExample();
        TbOrderItemExample.Criteria criteria= example.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        List<TbOrderItem> orderItemList = tbOrderItemMapper.selectByExample(example);

        TbOrderShipping tbOrderShipping=tbOrderShippingMapper.selectByPrimaryKey(orderId);

        orderDetail.setTbOrder(tbOrder);
        orderDetail.setTbOrderItem(orderItemList);
        orderDetail.setTbOrderShipping(tbOrderShipping);
        return orderDetail;
    }

    @Override
    public int deliver(String orderId, String shippingName, String shippingCode, BigDecimal postFee) {
        TbOrder o = tbOrderMapper.selectByPrimaryKey(orderId);
        o.setShippingName(shippingName);
        o.setShippingCode(shippingCode);
        o.setPostFee(postFee);
        o.setConsignTime(new Date());
        o.setUpdateTime(new Date());
        o.setIsConsign(true);
        //之前忘记设置常量了 将就这样看吧 0、未付款，1、已付款，2、未发货，3、已发货，4、交易成功，5、交易关闭
        o.setStatus(3);
        tbOrderMapper.updateByPrimaryKey(o);
        return 1;
    }

    @Override
    public int remark(String orderId, String message) {

        TbOrder o = tbOrderMapper.selectByPrimaryKey(orderId);
        o.setBuyerMessage(message);
        o.setUpdateTime(new Date());
        tbOrderMapper.updateByPrimaryKey(o);
        return 1;
    }

    @Override
    public int cancelOrderByAdmin(String orderId) {

        TbOrder o = tbOrderMapper.selectByPrimaryKey(orderId);
        o.setCloseTime(new Date());
        o.setUpdateTime(new Date());
        //之前忘记设置常量了 将就这样看吧 0、未付款，1、已付款，2、未发货，3、已发货，4、交易成功，5、交易关闭
        o.setStatus(5);
        tbOrderMapper.updateByPrimaryKey(o);
        return 1;
    }

    @Override
    public int deleteOrder(String id) {

        if(tbOrderMapper.deleteByPrimaryKey(id)!=1){
            throw new HiShopException("删除订单数失败");
        }

        TbOrderItemExample example=new TbOrderItemExample();
        TbOrderItemExample.Criteria criteria= example.createCriteria();
        criteria.andOrderIdEqualTo(id);
        List<TbOrderItem> list =tbOrderItemMapper.selectByExample(example);
        for(TbOrderItem tbOrderItem:list){
            if(tbOrderItemMapper.deleteByPrimaryKey(tbOrderItem.getId())!=1){
                throw new HiShopException("删除订单商品失败");
            }
        }

        if(tbOrderShippingMapper.deleteByPrimaryKey(id)!=1){
            throw new HiShopException("删除物流失败");
        }
        return 1;
    }

    @Override
    public int cancelOrder() {
        TbOrderExample example=new TbOrderExample();
        List<TbOrder> list=tbOrderMapper.selectByExample(example);
        for(TbOrder tbOrder:list){
            judgeOrder(tbOrder);
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
                tbOrder.setUpdateTime(new Date());
                if(tbOrderMapper.updateByPrimaryKey(tbOrder)!=1){
                    throw new HiShopException("设置订单关闭失败");
                }
            }else {
                //返回到期时间
                long time=tbOrder.getCreateTime().getTime()+1000 * 60 * 60 * 24;
                result= String.valueOf(time);
            }
        }
        return result;
    }
    @Override
    public TbOrder selectById(String oid) {
        return tbOrderMapper.selectByPrimaryKey(oid);
    }
    @Override
    public int updateById(TbOrder tbOrder) {
        return tbOrderMapper.updateByPrimaryKey(tbOrder);
    }

    @Override
    public int confirmOrder() {
        TbOrderExample example=new TbOrderExample();
        TbOrderExample.Criteria cri = example.createCriteria();
        cri.andStatusEqualTo(3);
        List<TbOrder> list=tbOrderMapper.selectByExample(example);
        for (TbOrder order:list){
            judgeConfirm(order);
        }
        return 1;
    }

    /**
     * 15天自动确认收货
     * @param tbOrder
     * @return
     */
    public String judgeConfirm(TbOrder tbOrder){

        String result=null;
        //判断是否已超15天
        long diff=System.currentTimeMillis()-tbOrder.getConsignTime().getTime();
        long days = diff / (1000 * 60 * 60 * 24);
        if(days>=15){
            tbOrder.setStatus(4);
            tbOrder.setUpdateTime(new Date());
            if(tbOrderMapper.updateByPrimaryKey(tbOrder)!=1){
                throw new HiShopException("设置订单确认收货失败");
            }
        }else {
            //返回到期时间
            long time=tbOrder.getConsignTime().getTime()+1000 * 60 * 60 * 24;
            result= String.valueOf(time);
        }
        return result;
    }


    @Override
    public int autoRefund() {
        TbOrderExample example=new TbOrderExample();
        TbOrderExample.Criteria cri = example.createCriteria();
        cri.andStatusEqualTo(7);
        List<TbOrder> list=tbOrderMapper.selectByExample(example);
        for (TbOrder order:list){
            judgeRefund(order);
        }
        return 1;
    }

    /**
     * 7天自动退款
     * @param tbOrder
     * @return
     */
    public String judgeRefund(TbOrder tbOrder){
        String result=null;
        //判断是否已超7天
        long diff=System.currentTimeMillis()-tbOrder.getRefundTime().getTime();
        long days = diff / (1000 * 60 * 60 * 24);
        if(days>=7){
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
            AlipayTradeRefundModel refundModel = new AlipayTradeRefundModel();
            refundModel.setOutTradeNo(tbOrder.getOrderId());
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
                    tbOrder.setStatus(8);
                    tbOrder.setRefundSuccessTime(new Date());
                    tbOrder.setUpdateTime(new Date());
                    if(tbOrderMapper.updateByPrimaryKey(tbOrder)!=1){
                        throw new HiShopException("设置订单退款失败");
                    }
                }
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
        }else {
            //返回到期时间
            long time=tbOrder.getRefundTime().getTime()+1000 * 60 * 60 * 24;
            result= String.valueOf(time);
        }
        return result;
    }

    @Override
    public int deliverEmail() {
        TbOrderExample example=new TbOrderExample();
        TbOrderExample.Criteria cri = example.createCriteria();
        cri.andStatusEqualTo(2);
        List<TbOrder> list=tbOrderMapper.selectByExample(example);
        for (TbOrder order:list){
            judgeEmail(order);
        }
        return 1;
    }

    /**
     * 3天提醒发货
     * @param tbOrder
     * @return
     */
    public String judgeEmail(TbOrder tbOrder){

        String result=null;
        //判断是否已超3天
        long diff=System.currentTimeMillis()-tbOrder.getPaymentTime().getTime();
        long days = diff / (1000 * 60 * 60 * 24);
        if(days>=3){
            emailUtil.SendDeliverEmail(tbOrder);
        }else {
            //返回到期时间
            long time=tbOrder.getPaymentTime().getTime()+1000 * 60 * 60 * 24;
            result= String.valueOf(time);
        }
        return result;
    }
}
