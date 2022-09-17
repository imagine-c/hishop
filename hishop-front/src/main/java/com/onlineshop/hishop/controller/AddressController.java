package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.feign.AddressFeignService;
import com.onlineshop.hishop.pojo.TbAddress;
import com.onlineshop.hishop.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@Api(description = "收货地址")
public class AddressController {

    @Autowired
    private AddressFeignService addressFeignService;

    @RequestMapping(value = "/member/getAddrJson", method = RequestMethod.GET)
    @ApiOperation(value = "Json中的收货地址")
    public Result<Object> getAddrJson() {
        return addressFeignService.getAddrJson();
    }

    @RequestMapping(value = "/member/addressList", method = RequestMethod.POST)
    @ApiOperation(value = "获得所有收货地址")
    public Result<List<TbAddress>> addressList(@RequestBody TbAddress tbAddress) {
        return addressFeignService.addressList(tbAddress);
    }

    @RequestMapping(value = "/member/address", method = RequestMethod.POST)
    @ApiOperation(value = "通过id获得收货地址")
    public Result<TbAddress> address(@RequestBody TbAddress tbAddress) {
        return addressFeignService.address(tbAddress);
    }

    @RequestMapping(value = "/member/addAddress", method = RequestMethod.POST)
    @ApiOperation(value = "添加收货地址")
    public Result<Object> addAddress(@RequestBody TbAddress tbAddress) {
        return addressFeignService.addAddress(tbAddress);
    }

    @RequestMapping(value = "/member/updateAddress", method = RequestMethod.POST)
    @ApiOperation(value = "编辑收货地址")
    public Result<Object> updateAddress(@RequestBody TbAddress tbAddress) {
       return addressFeignService.updateAddress(tbAddress);
    }

    @RequestMapping(value = "/member/delAddress", method = RequestMethod.POST)
    @ApiOperation(value = "删除收货地址")
    public Result<Object> delAddress(@RequestBody TbAddress tbAddress) {
       return addressFeignService.delAddress(tbAddress);
    }
}
