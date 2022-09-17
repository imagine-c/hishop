package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.front.Cart;
import com.onlineshop.hishop.dto.front.CartProduct;
import com.onlineshop.hishop.feign.CartFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(description = "购物车")
public class CartController {

    @Autowired
    CartFeignService cartFeignService;

    @RequestMapping(value = "/member/addCart", method = RequestMethod.POST)
    @ApiOperation(value = "添加购物车商品")
    public Result<Object> addCart(@RequestBody Cart cart) {

        return cartFeignService.addCart(cart);
    }

    @RequestMapping(value = "/member/cartList", method = RequestMethod.POST)
    @ApiOperation(value = "获取购物车商品列表")
    public Result<List<CartProduct>> getCartList(@RequestBody Cart cart) {

        return  cartFeignService.getCartList(cart);
    }

    @RequestMapping(value = "/member/cartEdit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑购物车商品")
    public Result<Object> updateCartNum(@RequestBody Cart cart) {

        return cartFeignService.updateCartNum(cart);
    }

    @RequestMapping(value = "/member/editCheckAll", method = RequestMethod.POST)
    @ApiOperation(value = "是否全选购物车商品")
    public Result<Object> editCheckAll(@RequestBody Cart cart) {

        return cartFeignService.editCheckAll(cart);
    }

    @RequestMapping(value = "/member/cartDel", method = RequestMethod.POST)
    @ApiOperation(value = "删除购物车商品")
    public Result<Object> deleteCartItem(@RequestBody Cart cart) {

        return cartFeignService.deleteCartItem(cart);
    }

    @RequestMapping(value = "/member/delCartChecked", method = RequestMethod.POST)
    @ApiOperation(value = "删除购物车选中商品")
    public Result<Object> delChecked(@RequestBody Cart cart) {

        return cartFeignService.delChecked(cart);
    }
}
