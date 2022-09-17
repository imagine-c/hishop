package com.onlineshop.hishop.feign;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.pojo.TbPanelContent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Component
@FeignClient(value = "hishop-content")
public interface ContentFeignService {


    @RequestMapping(value = "/admin/content/list/{panelId}",method = RequestMethod.GET)
    public DataTablesResult getContentByCid(@PathVariable int panelId);

    @RequestMapping(value = "/admin/content/add",method = RequestMethod.POST)
    public Result<Object> addContent(@ModelAttribute TbPanelContent tbPanelContent);

    @RequestMapping(value = "/admin/content/update",method = RequestMethod.POST)
    public Result<Object> updateContent(@ModelAttribute TbPanelContent tbPanelContent);

    @RequestMapping(value = "/admin/content/del/{ids}",method = RequestMethod.DELETE)
    public Result<Object> addContent(@PathVariable int[] ids);
}
