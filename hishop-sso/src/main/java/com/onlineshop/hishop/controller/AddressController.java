package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.pojo.TbAddress;
import com.onlineshop.hishop.service.AddressService;
import com.onlineshop.hishop.service.CodeService;
import com.onlineshop.hishop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/front")
public class AddressController {


    @Autowired
    private AddressService addressService;
    @Autowired
    private CodeService codeService;

    @RequestMapping(value = "/member/getAddrJson", method = RequestMethod.GET)
    public Result<Object> getAddrJson() {
        String addr = addressService.getData();
        return new ResultUtil<Object>().setData(addr);
    }

    @RequestMapping(value = "/member/addressList", method = RequestMethod.POST)
    public Result<List<TbAddress>> addressList(@RequestBody TbAddress tbAddress) {
        List<TbAddress> list = addressService.getAddressList(tbAddress.getUserId());
        return new ResultUtil<List<TbAddress>>().setData(list);
    }

    @RequestMapping(value = "/member/address", method = RequestMethod.POST)
    public Result<TbAddress> address(@RequestBody TbAddress tbAddress) {
        TbAddress address = addressService.getAddress(tbAddress.getAddressId());
        return new ResultUtil<TbAddress>().setData(address);
    }

    @RequestMapping(value = "/member/addAddress", method = RequestMethod.POST)
    public Result<Object> addAddress(@RequestBody TbAddress tbAddress) {
        tbAddress.setProvinceName(codeService.selectByProvinceCode(tbAddress.getProvinceCode()).getName());
        tbAddress.setCityName(codeService.selectByCityCode(tbAddress.getCityCode()).getName());
        tbAddress.setAreaName(codeService.selectByAreaCode(tbAddress.getAreaCode()).getName());
        tbAddress.setStreetName(codeService.selectByStreetCode(tbAddress.getStreetCode()).getName());
        int result = addressService.addAddress(tbAddress);
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/updateAddress", method = RequestMethod.POST)
    public Result<Object> updateAddress(@RequestBody TbAddress tbAddress) {
        tbAddress.setProvinceName(codeService.selectByProvinceCode(tbAddress.getProvinceCode()).getName());
        tbAddress.setCityName(codeService.selectByCityCode(tbAddress.getCityCode()).getName());
        tbAddress.setAreaName(codeService.selectByAreaCode(tbAddress.getAreaCode()).getName());
        tbAddress.setStreetName(codeService.selectByStreetCode(tbAddress.getStreetCode()).getName());
        int result = addressService.updateAddress(tbAddress);
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/delAddress", method = RequestMethod.POST)
    public Result<Object> delAddress(@RequestBody TbAddress tbAddress) {
        int result = addressService.delAddress(tbAddress);
        return new ResultUtil<Object>().setData(result);
    }
}
