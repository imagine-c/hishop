package com.onlineshop.hishop.service.impl;

import com.onlineshop.hishop.dto.DtoUtil;
import com.onlineshop.hishop.dto.front.CartProduct;
import com.onlineshop.hishop.mapper.TbItemMapper;
import com.onlineshop.hishop.pojo.TbItem;
import com.onlineshop.hishop.service.CartService;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;


@Service
@Log4j2
@PropertySource("classpath:conf/sso.yml")
public class CartServiceImpl implements CartService {



    @Value("${CART_PRE}")
    private String CART_PRE;

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public int addCart(long userId, long itemId, int num) {

        //hash: "key:用户id" field："商品id" value："商品信息"
        Boolean hexists = redisTemplate.opsForHash().hasKey(CART_PRE + ":" + userId, itemId + "");
        //如果存在数量相加
        if (hexists) {
            Object json = redisTemplate.opsForHash().get(CART_PRE + ":" + userId, itemId + "");
            if (json != null) {
                CartProduct cartProduct = new Gson().fromJson(json.toString(), CartProduct.class);
                cartProduct.setProductNum(cartProduct.getProductNum() + num);
                redisTemplate.opsForHash().put(CART_PRE + ":" + userId, itemId + "", new Gson().toJson(cartProduct));
            } else {
                return 0;
            }
            return 1;
        }
        //如果不存在，根据商品id取商品信息
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        if (item == null) {
            return 0;
        }
        CartProduct cartProduct = DtoUtil.TbItem2CartProduct(item);
        cartProduct.setProductNum((long) num);
        cartProduct.setChecked("1");
        redisTemplate.opsForHash().put(CART_PRE + ":" + userId, itemId + "", new Gson().toJson(cartProduct));
        return 1;
    }

    @Override
    public List<CartProduct> getCartList(long userId) {

        List<Object> jsonList = redisTemplate.opsForHash().values(CART_PRE + ":" + userId);
        List<CartProduct> list = new ArrayList<>();
        for (Object json : jsonList) {
            CartProduct cartProduct = new Gson().fromJson(json.toString(), CartProduct.class);
            list.add(cartProduct);
        }
        return list;
    }

    @Override
    public int updateCartNum(long userId, long itemId, int num, String checked) {

        Object json = redisTemplate.opsForHash().get(CART_PRE + ":" + userId, itemId + "");
        if (json == null) {
            return 0;
        }
        CartProduct cartProduct = new Gson().fromJson(json.toString(), CartProduct.class);
        cartProduct.setProductNum((long) num);
        cartProduct.setChecked(checked);
        redisTemplate.opsForHash().put(CART_PRE + ":" + userId, itemId + "", new Gson().toJson(cartProduct));
        return 1;
    }

    @Override
    public int checkAll(long userId, String checked) {
        List<Object> jsonList = redisTemplate.opsForHash().values(CART_PRE + ":" + userId);

        for (Object json : jsonList) {
            CartProduct cartProduct = new Gson().fromJson(json.toString(), CartProduct.class);
            if ("true".equals(checked)) {
                cartProduct.setChecked("1");
            } else if ("false".equals(checked)) {
                cartProduct.setChecked("0");
            } else {
                return 0;
            }
            redisTemplate.opsForHash().put(CART_PRE + ":" + userId, cartProduct.getProductId() + "", new Gson().toJson(cartProduct));
        }

        return 1;
    }

    @Override
    public int deleteCartItem(long userId, long itemId) {
        redisTemplate.opsForHash().delete(CART_PRE + ":" + userId, itemId + "");
        return 1;
    }

    @Override
    public int delChecked(long userId) {

        List<Object> jsonList = redisTemplate.opsForHash().values(CART_PRE + ":" + userId);
        for (Object json : jsonList) {
            CartProduct cartProduct = new Gson().fromJson(json.toString(), CartProduct.class);
            if ("1".equals(cartProduct.getChecked())) {
                redisTemplate.opsForHash().delete(CART_PRE + ":" + userId, cartProduct.getProductId() + "");
            }
        }
        return 1;
    }

}
