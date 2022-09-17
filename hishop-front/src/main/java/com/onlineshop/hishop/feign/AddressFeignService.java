package com.onlineshop.hishop.feign;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.pojo.TbAddress;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Component
@FeignClient("hishop-sso")
public interface AddressFeignService {

    @RequestMapping(value = "/front/member/getAddrJson", method = RequestMethod.GET)
    public Result<Object> getAddrJson();
    
    @RequestMapping(value = "/front/member/addressList", method = RequestMethod.POST)
    public Result<List<TbAddress>> addressList(@RequestBody TbAddress tbAddress);

    @RequestMapping(value = "/front/member/address", method = RequestMethod.POST)
    public Result<TbAddress> address(@RequestBody TbAddress tbAddress);

    @RequestMapping(value = "/front/member/addAddress", method = RequestMethod.POST)
    public Result<Object> addAddress(@RequestBody TbAddress tbAddress);

    @RequestMapping(value = "/front/member/updateAddress", method = RequestMethod.POST)
    public Result<Object> updateAddress(@RequestBody TbAddress tbAddress);

    @RequestMapping(value = "/front/member/delAddress", method = RequestMethod.POST)
    public Result<Object> delAddress(@RequestBody TbAddress tbAddress);
}
