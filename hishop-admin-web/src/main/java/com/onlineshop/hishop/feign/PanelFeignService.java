package com.onlineshop.hishop.feign;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.domain.ZTreeNode;
import com.onlineshop.hishop.pojo.TbPanel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Component
@FeignClient(value = "hishop-content")
public interface PanelFeignService {

    @RequestMapping(value = "/admin/admin/panel/index/list",method = RequestMethod.GET)
    public List<ZTreeNode> getIndexPanel();

    @RequestMapping(value = "/admin/panel/indexAll/list",method = RequestMethod.GET)
    public List<ZTreeNode> getAllIndexPanel();

    @RequestMapping(value = "/admin/panel/indexBanner/list",method = RequestMethod.GET)
    public List<ZTreeNode> getIndexBannerPanel();

    @RequestMapping(value = "/admin/panel/other/list",method = RequestMethod.GET)
    public List<ZTreeNode> getRecommendPanel();

    @RequestMapping(value = "/admin/panel/add",method = RequestMethod.POST)
    public Result<Object> addContentCategory(@ModelAttribute TbPanel tbPanel);

    @RequestMapping(value = "/admin/panel/update",method = RequestMethod.POST)
    public Result<Object> updateContentCategory(@ModelAttribute TbPanel tbPanel);

    @RequestMapping(value = "/admin/panel/del/{ids}",method = RequestMethod.DELETE)
    public Result<Object> deleteContentCategory(@PathVariable int[] ids);

}
