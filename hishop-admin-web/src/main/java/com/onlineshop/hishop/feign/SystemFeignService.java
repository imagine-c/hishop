package com.onlineshop.hishop.feign;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.pojo.TbBase;
import com.onlineshop.hishop.pojo.TbOrderItem;
import com.onlineshop.hishop.pojo.TbShiroFilter;
import com.onlineshop.hishop.pojo.TbUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Component
@FeignClient(value = "hishop-admin")
public interface SystemFeignService {

    @RequestMapping(value = "/admin/sys/shiro/list",method = RequestMethod.GET)
    public DataTablesResult getShiroList(@ModelAttribute TbUser tbUser);

    @RequestMapping(value = "/admin/sys/shiro/count",method = RequestMethod.GET)
    public Result<Object> getUserCount();

    @RequestMapping(value = "/admin/sys/shiro/add",method = RequestMethod.POST)
    public Result<Object> addShiro(@ModelAttribute TbShiroFilter tbShiroFilter);

    @RequestMapping(value = "/admin/sys/shiro/update",method = RequestMethod.POST)
    public Result<Object> updateShiro(@ModelAttribute TbShiroFilter tbShiroFilter);

    @RequestMapping(value = "/admin/sys/shiro/del/{ids}",method = RequestMethod.DELETE)
    public Result<Object> delShiro(@PathVariable int[] ids);

    @RequestMapping(value = "/admin/sys/base",method = RequestMethod.GET)
    public Result<TbBase> getBase();

    @RequestMapping(value = "/admin/sys/base/update",method = RequestMethod.POST)
    public Result<Object> updateBase(@ModelAttribute TbBase tbBase);

    @RequestMapping(value = "/admin/sys/weekHot",method = RequestMethod.GET)
    public Result<TbOrderItem> getWeekHot();

    @RequestMapping(value = "/admin/sys/weather",method = RequestMethod.GET)
    public Result<Object> getWeather(HttpServletRequest request);

    @RequestMapping(value = "/admin/sys/log",method = RequestMethod.GET)
    public DataTablesResult getLog(@RequestParam int draw, @RequestParam int start,
                                   @RequestParam int length, @RequestParam("search[value]") String search,
                                   @RequestParam("order[0][column]") int orderCol,
                                   @RequestParam("order[0][dir]") String orderDir);

    @RequestMapping(value = "/admin/sys/log/count",method = RequestMethod.GET)
    public Result<Object> countLog();

    @RequestMapping(value = "/admin/sys/log/del/{ids}",method = RequestMethod.DELETE)
    public Result<Object> delLog(@PathVariable int[] ids);
}
