package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.domain.ZTreeNode;
import com.onlineshop.hishop.pojo.TbItemCat;
import com.onlineshop.hishop.service.ItemCatService;
import com.onlineshop.hishop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping(value = "/admin/item/cat/list",method = RequestMethod.GET)
    public List<ZTreeNode> getItemCatList(@RequestParam(name="id",defaultValue = "0") int parentId){

        List<ZTreeNode> list=itemCatService.getItemCatList(parentId);
        return list;
    }

    @RequestMapping(value = "/admin/item/cat/add",method = RequestMethod.POST)
    public Result<Object> addItemCategory(@ModelAttribute TbItemCat tbItemCat){
        itemCatService.addItemCat(tbItemCat);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/item/cat/update",method = RequestMethod.POST)
    public Result<Object> updateItemCategory(@ModelAttribute TbItemCat tbItemCat){

        itemCatService.updateItemCat(tbItemCat);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/item/cat/del/{id}",method = RequestMethod.DELETE)
    public Result<Object> deleteItemCategory(@PathVariable Long id){

        itemCatService.deleteItemCat(id);
        return new ResultUtil<Object>().setData(null);
    }
}
