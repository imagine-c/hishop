package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.domain.ZTreeNode;
import com.onlineshop.hishop.feign.PanelFeignService;
import com.onlineshop.hishop.pojo.TbPanel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Log4j2
@Api(description = "板块列表")
public class PanelController {


    @Autowired
    private PanelFeignService panelFeignService;

    @RequestMapping(value = "/panel/index/list",method = RequestMethod.GET)
    @ApiOperation(value = "获得首页板块列表不含轮播")
    public List<ZTreeNode> getIndexPanel(){
        
        return panelFeignService.getIndexPanel();
    }

    @RequestMapping(value = "/panel/indexAll/list",method = RequestMethod.GET)
    @ApiOperation(value = "获得首页板块列表含轮播")
    public List<ZTreeNode> getAllIndexPanel(){
        
        return panelFeignService.getAllIndexPanel();
    }

    @RequestMapping(value = "/panel/indexBanner/list",method = RequestMethod.GET)
    @ApiOperation(value = "获得首页轮播板块列表")
    public List<ZTreeNode> getIndexBannerPanel(){
        
        return panelFeignService.getIndexBannerPanel();
    }

    @RequestMapping(value = "/panel/other/list",method = RequestMethod.GET)
    @ApiOperation(value = "获得其它添加板块")
    public List<ZTreeNode> getRecommendPanel(){
        
        return panelFeignService.getRecommendPanel();
    }

    @RequestMapping(value = "/panel/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加板块")
    public Result<Object> addContentCategory(@ModelAttribute TbPanel tbPanel){
        
        return panelFeignService.addContentCategory(tbPanel);
    }

    @RequestMapping(value = "/panel/update",method = RequestMethod.POST)
    @ApiOperation(value = "编辑内容分类")
    public Result<Object> updateContentCategory(@ModelAttribute TbPanel tbPanel){
        
        return panelFeignService.updateContentCategory(tbPanel);
    }

    @RequestMapping(value = "/panel/del/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除内容分类")
    public Result<Object> deleteContentCategory(@PathVariable int[] ids){
        
        return panelFeignService.deleteContentCategory(ids);
    }
}
