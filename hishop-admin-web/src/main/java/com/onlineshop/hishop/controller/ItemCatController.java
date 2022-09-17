package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.domain.ZTreeNode;
import com.onlineshop.hishop.feign.ItemCatFeignService;
import com.onlineshop.hishop.pojo.TbItemCat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(description = "商品分类信息")
public class ItemCatController {

    @Autowired
    private ItemCatFeignService itemCatFeignService;

    @RequestMapping(value = "/item/cat/list",method = RequestMethod.GET)
    @ApiOperation(value = "通过父ID获取商品分类列表")
    public List<ZTreeNode> getItemCatList(@RequestParam(name="id",defaultValue = "0") int parentId){

        return itemCatFeignService.getItemCatList(parentId);
    }

    @RequestMapping(value = "/item/cat/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加商品分类")
    public Result<Object> addItemCategory(@ModelAttribute TbItemCat tbItemCat){

        return itemCatFeignService.addItemCategory(tbItemCat);
    }

    @RequestMapping(value = "/item/cat/update",method = RequestMethod.POST)
    @ApiOperation(value = "编辑商品分类")
    public Result<Object> updateItemCategory(@ModelAttribute TbItemCat tbItemCat){

        return itemCatFeignService.updateItemCategory(tbItemCat);
    }

    @RequestMapping(value = "/item/cat/del/{id}",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除商品分类")
    public Result<Object> deleteItemCategory(@PathVariable Long id){

        return itemCatFeignService.deleteItemCategory(id);
    }
}
