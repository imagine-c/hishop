package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.ItemDto;
import com.onlineshop.hishop.feign.ItemFeignService;
import com.onlineshop.hishop.pojo.TbItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(description = "商品列表信息")
@Log4j2
public class ItemController{


    @Autowired
    private ItemFeignService itemFeignService;
//    @Autowired
//    private SearchitemFeignService searchitemFeignService;

    @RequestMapping(value = "/item/{itemId}",method = RequestMethod.GET)
    @ApiOperation(value = "通过ID获取商品")
    public Result<ItemDto> getItemById(@PathVariable Long itemId){

        return itemFeignService.getItemById(itemId);
    }

    @RequestMapping(value = "/item/list",method = RequestMethod.GET)
    @ApiOperation(value = "分页搜索排序获取商品列表")
    public DataTablesResult getItemList(@RequestParam int draw, @RequestParam int start,
                                        @RequestParam int length, @RequestParam int cid,
                                        @RequestParam("search[value]") String search,
                                        @RequestParam("order[0][column]") int orderCol,
                                        @RequestParam("order[0][dir]") String orderDir){

        return itemFeignService.getItemList(draw,start,length,cid,search,orderCol,orderDir);
    }

    @RequestMapping(value = "/item/listSearch",method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页搜索排序获取商品列表")
    public DataTablesResult getItemSearchList(@RequestParam int draw, @RequestParam int start,
                                              @RequestParam int length,@RequestParam int cid,
                                              @RequestParam String searchKey, @RequestParam String minDate,
                                              @RequestParam String maxDate, @RequestParam("search[value]") String search,
                                              @RequestParam("order[0][column]") int orderCol,
                                              @RequestParam("order[0][dir]") String orderDir){

        return itemFeignService.getItemSearchList(draw,start,length,cid,searchKey,minDate,maxDate,search,orderCol,orderDir);
    }

    @RequestMapping(value = "/item/count",method = RequestMethod.GET)
    @ApiOperation(value = "获得商品总数目")
    public DataTablesResult getAllItemCount(){

        return itemFeignService.getAllItemCount();
    }

    @RequestMapping(value = "/item/stop/{id}",method = RequestMethod.PUT)
    @ApiOperation(value = "下架商品")
    public Result<TbItem> stopItem(@PathVariable Long id){

        return itemFeignService.stopItem(id);
    }


    @RequestMapping(value = "/item/start/{id}",method = RequestMethod.PUT)
    @ApiOperation(value = "发布商品")
    public Result<TbItem> startItem(@PathVariable Long id){

        return itemFeignService.startItem(id);
    }

//    @RequestMapping(value = "/item/del/{ids}",method = RequestMethod.DELETE)
//    @ApiOperation(value = "删除商品")
//    public Result<TbItem> deleteItem(@PathVariable Long[] ids){
//
//        for(Long id:ids){
//            itemFeignService.deleteItem(id);
//        }
//        return new ResultUtil<TbItem>().setData(null);
//    }

    @RequestMapping(value = "/item/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加商品")
    public Result<TbItem> addItem(ItemDto itemDto){

        return itemFeignService.addItem(itemDto);
    }

    @RequestMapping(value = "/item/update/{id}",method = RequestMethod.POST)
    @ApiOperation(value = "编辑商品")
    public Result<TbItem> updateItem(@PathVariable Long id, ItemDto itemDto){

        return itemFeignService.updateItem(id,itemDto);
    }

//    @RequestMapping(value = "/admin/item/importIndex",method = RequestMethod.GET)
//    @ApiOperation(value = "导入商品索引至ES")
//    public Result<Object> importIndex(){
//
//        searchitemFeignService.importAllItems();
//        return new ResultUtil<Object>().setData(null);
//    }
//
//    @RequestMapping(value = "/admin/es/getInfo",method = RequestMethod.GET)
//    @ApiOperation(value = "获取ES信息")
//    public Result<Object> getESInfo(){
//
//        EsInfo esInfo=searchitemFeignService.getEsInfo();
//        return new ResultUtil<Object>().setData(esInfo);
//    }

}
