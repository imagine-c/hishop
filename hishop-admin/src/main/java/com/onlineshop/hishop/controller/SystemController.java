package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.pojo.TbBase;
import com.onlineshop.hishop.pojo.TbOrderItem;
import com.onlineshop.hishop.pojo.TbShiroFilter;
import com.onlineshop.hishop.pojo.TbUser;
import com.onlineshop.hishop.service.SystemService;
import com.onlineshop.hishop.utils.IPInfoUtil;
import com.onlineshop.hishop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
public class SystemController {

    @Autowired
    private SystemService systemService;

    @RequestMapping(value = "/admin/sys/shiro/list",method = RequestMethod.GET)
    public DataTablesResult getShiroList(@ModelAttribute TbUser tbUser){

        DataTablesResult result=new DataTablesResult();
        List<TbShiroFilter> list=systemService.getShiroFilter();
        result.setData(list);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/admin/sys/shiro/count",method = RequestMethod.GET)
    public Result<Object> getUserCount(){

        Long result=systemService.countShiroFilter();
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/admin/sys/shiro/add",method = RequestMethod.POST)
    public Result<Object> addShiro(@ModelAttribute TbShiroFilter tbShiroFilter){

        systemService.addShiroFilter(tbShiroFilter);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/sys/shiro/update",method = RequestMethod.POST)
    public Result<Object> updateShiro(@ModelAttribute TbShiroFilter tbShiroFilter){

        systemService.updateShiroFilter(tbShiroFilter);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/sys/shiro/del/{ids}",method = RequestMethod.DELETE)
    public Result<Object> delShiro(@PathVariable int[] ids){

        for(int id:ids){
            systemService.deleteShiroFilter(id);
        }
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/sys/base",method = RequestMethod.GET)
    public Result<TbBase> getBase(){

        TbBase tbBase=systemService.getBase();
        return new ResultUtil<TbBase>().setData(tbBase);
    }

    @RequestMapping(value = "/admin/sys/base/update",method = RequestMethod.POST)
    public Result<Object> updateBase(@ModelAttribute TbBase tbBase){

        systemService.updateBase(tbBase);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/sys/weekHot",method = RequestMethod.GET)
    public Result<TbOrderItem> getWeekHot(){

        TbOrderItem tbOrderItem=systemService.getWeekHot();
        return new ResultUtil<TbOrderItem>().setData(tbOrderItem);
    }

    @RequestMapping(value = "/admin/sys/weather",method = RequestMethod.GET)
    public Result<Object> getWeather(HttpServletRequest request){

        String result= IPInfoUtil.getIpInfo(IPInfoUtil.getIpAddr(request));
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/admin/sys/log",method = RequestMethod.GET)
    public DataTablesResult getLog(@RequestParam int draw, @RequestParam int start,
                                   @RequestParam int length, @RequestParam("search[value]") String search,
                                   @RequestParam("order[0][column]") int orderCol,
                                   @RequestParam("order[0][dir]") String orderDir){

        //获取客户端需要排序的列
        String[] cols = {"checkbox","id", "name","type", "url", "request_type", "request_param", "user", "ip", "ip_info","time","create_date"};
        String orderColumn = cols[orderCol];
        //默认排序列
        if(orderColumn == null) {
            orderColumn = "create_date";
        }
        //获取排序方式 默认为desc(asc)
        if(orderDir == null) {
            orderDir = "desc";
        }
        DataTablesResult result= systemService.getLogList(draw,start,length,search,orderColumn,orderDir);
        return result;
    }

    @RequestMapping(value = "/admin/sys/log/count",method = RequestMethod.GET)
    public Result<Object> countLog(){

        Long result=systemService.countLog();
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/admin/sys/log/del/{ids}",method = RequestMethod.DELETE)
    public Result<Object> delLog(@PathVariable int[] ids){

        for(int id:ids){
            systemService.deleteLog(id);
        }
        return new ResultUtil<Object>().setData(null);
    }
}
