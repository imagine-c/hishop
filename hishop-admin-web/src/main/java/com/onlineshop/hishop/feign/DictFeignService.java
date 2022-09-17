package com.onlineshop.hishop.feign;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.pojo.TbDict;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;


@Component
@FeignClient(value = "hishop-admin")
public interface DictFeignService {

    @RequestMapping(value = "/admin/getDictList",method = RequestMethod.GET)
    public String getDictExtList(HttpServletResponse response);

    @RequestMapping(value = "/admin/getStopDictList",method = RequestMethod.GET)
    public String getStopDictList(HttpServletResponse response);

    @RequestMapping(value = "/admin/dict/list",method = RequestMethod.GET)
    public DataTablesResult getDictList();

    @RequestMapping(value = "/admin/dict/stop/list",method = RequestMethod.GET)
    public DataTablesResult getStopList();

    @RequestMapping(value = "/admin/dict/add",method = RequestMethod.POST)
    public Result<Object> addDict(@ModelAttribute TbDict tbDict);

    @RequestMapping(value = "/admin/dict/update",method = RequestMethod.POST)
    public Result<Object> updateDict(@ModelAttribute TbDict tbDict);

    @RequestMapping(value = "/admin/dict/del/{ids}",method = RequestMethod.DELETE)
    public Result<Object> delDict(@PathVariable int[] ids);

}
