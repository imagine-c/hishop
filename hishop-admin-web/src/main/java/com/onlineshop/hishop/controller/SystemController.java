package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.feign.SystemFeignService;
import com.onlineshop.hishop.pojo.TbBase;
import com.onlineshop.hishop.pojo.TbOrderItem;
import com.onlineshop.hishop.pojo.TbShiroFilter;
import com.onlineshop.hishop.pojo.TbUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@Api(description= "系统配置管理")
public class SystemController {

    @Autowired
    private SystemFeignService systemFeignService;

    @RequestMapping(value = "/sys/shiro/list",method = RequestMethod.GET)
    @ApiOperation(value = "获取shiro过滤链配置")
    public DataTablesResult getShiroList(@ModelAttribute TbUser tbUser){

        return systemFeignService.getShiroList(tbUser);
    }

    @RequestMapping(value = "/sys/shiro/count",method = RequestMethod.GET)
    @ApiOperation(value = "统计shiro过滤链数")
    public Result<Object> getUserCount(){

        return systemFeignService.getUserCount();
    }

    @RequestMapping(value = "/sys/shiro/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加shiro过滤链")
    public Result<Object> addShiro(@ModelAttribute TbShiroFilter tbShiroFilter){

        return systemFeignService.addShiro(tbShiroFilter);
    }

    @RequestMapping(value = "/sys/shiro/update",method = RequestMethod.POST)
    @ApiOperation(value = "更新shiro过滤链")
    public Result<Object> updateShiro(@ModelAttribute TbShiroFilter tbShiroFilter){

        return systemFeignService.updateShiro(tbShiroFilter);
    }

    @RequestMapping(value = "/sys/shiro/del/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除shiro过滤链")
    public Result<Object> delShiro(@PathVariable int[] ids){

        return systemFeignService.delShiro(ids);
    }

    @RequestMapping(value = "/sys/base",method = RequestMethod.GET)
    @ApiOperation(value = "获取基本设置")
    public Result<TbBase> getBase(){

        return systemFeignService.getBase();
    }

    @RequestMapping(value = "/sys/base/update",method = RequestMethod.POST)
    @ApiOperation(value = "编辑基本设置")
    public Result<Object> updateBase(@ModelAttribute TbBase tbBase){

        return systemFeignService.updateBase(tbBase);
    }

    @RequestMapping(value = "/sys/weekHot",method = RequestMethod.GET)
    @ApiOperation(value = "获取本周热销商品数据")
    public Result<TbOrderItem> getWeekHot(){

        return systemFeignService.getWeekHot();
    }

    @RequestMapping(value = "/sys/weather",method = RequestMethod.GET)
    @ApiOperation(value = "获取天气信息")
    public Result<Object> getWeather(HttpServletRequest request){

        return systemFeignService.getWeather(request);
    }

    @RequestMapping(value = "/sys/log",method = RequestMethod.GET)
    @ApiOperation(value = "分页获取系统日志")
    public DataTablesResult getLog(@RequestParam int draw, @RequestParam int start,
                                   @RequestParam int length, @RequestParam("search[value]") String search,
                                   @RequestParam("order[0][column]") int orderCol,
                                   @RequestParam("order[0][dir]") String orderDir){


        return systemFeignService.getLog(draw,start,length,search,orderCol,orderDir);
    }

    @RequestMapping(value = "/sys/log/count",method = RequestMethod.GET)
    @ApiOperation(value = "获取系统日志总数")
    public Result<Object> countLog(){

        return systemFeignService.countLog();
    }

    @RequestMapping(value = "/sys/log/del/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除系统日志")
    public Result<Object> delLog(@PathVariable int[] ids){

        return systemFeignService.delLog(ids);
    }
}
