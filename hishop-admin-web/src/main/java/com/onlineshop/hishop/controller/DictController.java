package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.feign.DictFeignService;
import com.onlineshop.hishop.pojo.TbDict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


@RestController
@Api(description = "词典库")
public class DictController {

    @Autowired
    private DictFeignService dictFeignService;

    @RequestMapping(value = "/getDictList",method = RequestMethod.GET)
    @ApiOperation(value = "获得所有扩展词库")
    public String getDictExtList(HttpServletResponse response){

        return dictFeignService.getDictExtList(response);
    }

    @RequestMapping(value = "/getStopDictList",method = RequestMethod.GET)
    @ApiOperation(value = "获得所有扩展词库")
    public String getStopDictList(HttpServletResponse response){

        return dictFeignService.getStopDictList(response);
    }

    @RequestMapping(value = "/dict/list",method = RequestMethod.GET)
    @ApiOperation(value = "获得所有扩展词库")
    public DataTablesResult getDictList(){

        return dictFeignService.getDictList();
    }

    @RequestMapping(value = "/dict/stop/list",method = RequestMethod.GET)
    @ApiOperation(value = "获得所有停用词库")
    public DataTablesResult getStopList(){

        return dictFeignService.getStopList();
    }

    @RequestMapping(value = "/dict/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加词典")
    public Result<Object> addDict(@ModelAttribute TbDict tbDict){

        return dictFeignService.addDict(tbDict);
    }

    @RequestMapping(value = "/dict/update",method = RequestMethod.POST)
    @ApiOperation(value = "编辑词典")
    public Result<Object> updateDict(@ModelAttribute TbDict tbDict){

        return dictFeignService.updateDict(tbDict);
    }

    @RequestMapping(value = "/dict/del/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除词典")
    public Result<Object> delDict(@PathVariable int[] ids){

        return dictFeignService.delDict(ids);
    }

}
