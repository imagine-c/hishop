package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.pojo.TbExpress;
import com.onlineshop.hishop.service.ExpressService;
import com.onlineshop.hishop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExpressController {

    @Autowired
    private ExpressService expressService;

    @RequestMapping(value = "/admin/express/list",method = RequestMethod.GET)
    public DataTablesResult addressList(){

        DataTablesResult result = new DataTablesResult();
        List<TbExpress> list=expressService.getExpressList();
        result.setData(list);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/admin/express/add",method = RequestMethod.POST)
    public Result<Object> addTbExpress(@ModelAttribute TbExpress tbExpress){

        expressService.addExpress(tbExpress);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/express/update",method = RequestMethod.POST)
    public Result<Object> updateAddress(@ModelAttribute TbExpress tbExpress){

        expressService.updateExpress(tbExpress);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/express/del/{ids}",method = RequestMethod.DELETE)
    public Result<Object> delAddress(@PathVariable int[] ids){

        for(int id:ids){
            expressService.delExpress(id);
        }
        return new ResultUtil<Object>().setData(null);
    }
}
