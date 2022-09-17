package com.onlineshop.hishop.feign;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.front.Cart;
import com.onlineshop.hishop.dto.front.CartProduct;
import com.onlineshop.hishop.utils.ResultUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Component
@FeignClient("hishop-sso")
public interface CartFeignService {

    @RequestMapping(value = "/front/member/addCart", method = RequestMethod.POST)
    public Result<Object> addCart(@RequestBody Cart cart);

    @RequestMapping(value = "/front/member/cartList", method = RequestMethod.POST)
    public Result<List<CartProduct>> getCartList(@RequestBody Cart cart);

    @RequestMapping(value = "/front/member/cartEdit", method = RequestMethod.POST)
    public Result<Object> updateCartNum(@RequestBody Cart cart);
    
    @RequestMapping(value = "/front/member/editCheckAll", method = RequestMethod.POST)
    public Result<Object> editCheckAll(@RequestBody Cart cart);

    @RequestMapping(value = "/front/member/cartDel", method = RequestMethod.POST)
    public Result<Object> deleteCartItem(@RequestBody Cart cart);

    @RequestMapping(value = "/front/member/delCartChecked", method = RequestMethod.POST)
    public Result<Object> delChecked(@RequestBody Cart cart);
    
}
