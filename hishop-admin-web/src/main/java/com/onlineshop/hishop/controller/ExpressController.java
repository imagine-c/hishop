package com.onlineshop.hishop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.pojo.TbExpress;
import com.onlineshop.hishop.service.ExpressService;
import com.onlineshop.hishop.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "快递")
public class ExpressController {

    @Reference
    private ExpressService expressService;

    @RequestMapping(value = "/express/list",method = RequestMethod.GET)
    @ApiOperation(value = "获得所有快递")
    public DataTablesResult addressList(){

        DataTablesResult result = new DataTablesResult();
        List<TbExpress> list=expressService.getExpressList();
        result.setData(list);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/express/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加快递")
    public Result<Object> addTbExpress(@ModelAttribute TbExpress tbExpress){

        expressService.addExpress(tbExpress);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/express/update",method = RequestMethod.POST)
    @ApiOperation(value = "编辑快递")
    public Result<Object> updateAddress(@ModelAttribute TbExpress tbExpress){

        expressService.updateExpress(tbExpress);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/express/del/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除快递")
    public Result<Object> delAddress(@PathVariable int[] ids){

        for(int id:ids){
            expressService.delExpress(id);
        }
        return new ResultUtil<Object>().setData(null);
    }
}
