package com.onlineshop.hishop.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.onlineshop.hishop.jedis.JedisClient;
import com.onlineshop.hishop.dto.DtoUtil;
import com.onlineshop.hishop.dto.front.CartProduct;
import com.onlineshop.hishop.mapper.TbItemMapper;
import com.onlineshop.hishop.pojo.TbItem;
import com.onlineshop.hishop.service.CartService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;


@Service
@Component
@PropertySource("classpath:conf/sso.properties")
public class CartServiceImpl implements CartService {

    private final static Logger log= LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private JedisClient jedisClient;
    @Value("${CART_PRE}")
    private String CART_PRE;

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public int addCart(long userId, long itemId, int num) {

        //hash: "key:用户id" field："商品id" value："商品信息"
        Boolean hexists = jedisClient.hexists(CART_PRE + ":" + userId, itemId + "");
        //如果存在数量相加
        if (hexists) {
            String json = jedisClient.hget(CART_PRE + ":" + userId, itemId + "");
            if(json!=null){
                CartProduct cartProduct = new Gson().fromJson(json,CartProduct.class);
                cartProduct.setProductNum(cartProduct.getProductNum() + num);
                jedisClient.hset(CART_PRE + ":" + userId, itemId + "", new Gson().toJson(cartProduct));
            }else {
                return 0;
            }
            return 1;
        }
        //如果不存在，根据商品id取商品信息
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        if(item==null){
            return 0;
        }
        CartProduct cartProduct= DtoUtil.TbItem2CartProduct(item);
        cartProduct.setProductNum((long) num);
        cartProduct.setChecked("1");
        jedisClient.hset(CART_PRE  + ":" + userId, itemId + "", new Gson().toJson(cartProduct));
        return 1;
    }

    @Override
    public List<CartProduct> getCartList(long userId) {

        List<String> jsonList = jedisClient.hvals(CART_PRE + ":" + userId);
        List<CartProduct> list = new ArrayList<>();
        for (String json : jsonList) {
            CartProduct cartProduct = new Gson().fromJson(json,CartProduct.class);
            list.add(cartProduct);
        }
        return list;
    }

    @Override
    public int updateCartNum(long userId, long itemId, int num, String checked) {

        String json = jedisClient.hget(CART_PRE + ":" + userId, itemId + "");
        if(json==null){
            return 0;
        }
        CartProduct cartProduct = new Gson().fromJson(json,CartProduct.class);
        cartProduct.setProductNum((long) num);
        cartProduct.setChecked(checked);
        jedisClient.hset(CART_PRE + ":" + userId, itemId + "", new Gson().toJson(cartProduct));
        return 1;
    }

    @Override
    public int checkAll(long userId,String checked) {

        List<String> jsonList = jedisClient.hvals(CART_PRE + ":" + userId);

        for (String json : jsonList) {
            CartProduct cartProduct = new Gson().fromJson(json,CartProduct.class);
            if("true".equals(checked)) {
                cartProduct.setChecked("1");
            }else if("false".equals(checked)) {
                cartProduct.setChecked("0");
            }else {
                return 0;
            }
            jedisClient.hset(CART_PRE + ":" + userId, cartProduct.getProductId() + "", new Gson().toJson(cartProduct));
        }

        return 1;
    }

    @Override
    public int deleteCartItem(long userId, long itemId) {

        jedisClient.hdel(CART_PRE + ":" + userId, itemId + "");
        return 1;
    }

    @Override
    public int delChecked(long userId) {

        List<String> jsonList = jedisClient.hvals(CART_PRE+":"+userId);
        for (String json : jsonList) {
            CartProduct cartProduct = new Gson().fromJson(json,CartProduct.class);
            if("1".equals(cartProduct.getChecked())) {
                jedisClient.hdel(CART_PRE+":"+userId, cartProduct.getProductId()+"");
            }
        }
        return 1;
    }

}
