package com.onlineshop.hishop.feign;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.pojo.TbExpress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@FeignClient(value = "hishop-admin")
public interface ExpressFeignService {

    @RequestMapping(value = "/admin/express/list",method = RequestMethod.GET)
    public DataTablesResult addressList();

    @RequestMapping(value = "/admin/express/add",method = RequestMethod.POST)
    public Result<Object> addTbExpress(@ModelAttribute TbExpress tbExpress);

    @RequestMapping(value = "/admin/express/update",method = RequestMethod.POST)
    public Result<Object> updateAddress(@ModelAttribute TbExpress tbExpress);

    @RequestMapping(value = "/admin/express/del/{ids}",method = RequestMethod.DELETE)
    public Result<Object> delAddress(@PathVariable int[] ids);
}
