package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.feign.ContentFeignService;
import com.onlineshop.hishop.pojo.TbPanelContent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@Log4j2
@Api(description = "板块内容管理")
public class ContentController {

    @Autowired
    private ContentFeignService contentFeignService;

    @RequestMapping(value = "/content/list/{panelId}",method = RequestMethod.GET)
    @ApiOperation(value = "通过panelId获得板块内容列表")
    public DataTablesResult getContentByCid(@PathVariable int panelId){
        
        return contentFeignService.getContentByCid(panelId);
    }

    @RequestMapping(value = "/content/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加板块内容")
    public Result<Object> addContent(@ModelAttribute TbPanelContent tbPanelContent){
        
        return contentFeignService.addContent(tbPanelContent);
    }

    @RequestMapping(value = "/content/update",method = RequestMethod.POST)
    @ApiOperation(value = "编辑板块内容")
    public Result<Object> updateContent(@ModelAttribute TbPanelContent tbPanelContent){
        
        return updateContent(tbPanelContent);
    }

    @RequestMapping(value = "/content/del/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除板块内容")
    public Result<Object> addContent(@PathVariable int[] ids){
        
        return contentFeignService.addContent(ids);
    }
}
