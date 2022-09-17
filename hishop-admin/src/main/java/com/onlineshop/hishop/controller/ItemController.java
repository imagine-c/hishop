package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.ItemDto;
import com.onlineshop.hishop.pojo.TbItem;
import com.onlineshop.hishop.service.ItemService;
import com.onlineshop.hishop.utils.ResultUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
public class ItemController{


    @Autowired
    private ItemService itemService;
//    @Autowired
//    private SearchItemService searchItemService;

    @RequestMapping(value = "/admin/item/{itemId}",method = RequestMethod.GET)
    public Result<ItemDto> getItemById(@PathVariable Long itemId){
        ItemDto itemDto=itemService.getItemById(itemId);
        if (itemDto.getCid()==null)
            System.out.println(12313);
        return new ResultUtil<ItemDto>().setData(itemDto);
    }

    @RequestMapping(value = "/admin/item/list",method = RequestMethod.GET)
    public DataTablesResult getItemList(@RequestParam int draw, @RequestParam int start,
                                        @RequestParam int length, @RequestParam int cid,
                                        @RequestParam("search[value]") String search,
                                        @RequestParam("order[0][column]") int orderCol,
                                        @RequestParam("order[0][dir]") String orderDir){


        //获取客户端需要排序的列
        String[] cols = {"checkbox","id", "image", "title", "sell_point", "price", "created", "updated", "status"};
        String orderColumn = cols[orderCol];
        if(orderColumn == null) {
            orderColumn = "created";
        }
        //获取排序方式 默认为desc(asc)
        if(orderDir == null) {
            orderDir = "desc";
        }
        DataTablesResult result=itemService.getItemList(draw,start,length,cid,search,orderColumn,orderDir);
        return result;
    }

    @RequestMapping(value = "/admin/item/listSearch",method = RequestMethod.GET)
    public DataTablesResult getItemSearchList(@RequestParam int draw, @RequestParam int start,
                                              @RequestParam int length,@RequestParam int cid,
                                              @RequestParam String searchKey, @RequestParam String minDate,
                                              @RequestParam String maxDate, @RequestParam("search[value]") String search,
                                              @RequestParam("order[0][column]") int orderCol,
                                              @RequestParam("order[0][dir]") String orderDir){

        //获取客户端需要排序的列
        String[] cols = {"checkbox","id", "image", "title", "sell_point", "price", "created", "updated", "status"};
        //默认排序列
        String orderColumn = cols[orderCol];
        if(orderColumn == null) {
            orderColumn = "created";
        }
        //获取排序方式 默认为desc(asc)
        if(orderDir == null) {
            orderDir = "desc";
        }
        if(!search.isEmpty()){
            searchKey=search;
        }
        DataTablesResult result=itemService.getItemSearchList(draw,start,length,cid,searchKey,minDate,maxDate,orderColumn,orderDir);
        return result;
    }

    @RequestMapping(value = "/admin/item/count",method = RequestMethod.GET)
    public DataTablesResult getAllItemCount(){

        DataTablesResult result=itemService.getAllItemCount();
        return result;
    }

    @RequestMapping(value = "/admin/item/stop/{id}",method = RequestMethod.PUT)
    public Result<TbItem> stopItem(@PathVariable Long id){
        TbItem tbItem = new TbItem();
        if (itemService.isExist(id))
            return new ResultUtil<TbItem>().setErrorMsg("该商品为首页板块中的商品，无法下架");
        tbItem = itemService.alertItemState(id,0);
        return new ResultUtil<TbItem>().setData(tbItem);
    }


    @RequestMapping(value = "/admin/item/start/{id}",method = RequestMethod.PUT)
    public Result<TbItem> startItem(@PathVariable Long id){

        TbItem tbItem = itemService.alertItemState(id,1);
        return new ResultUtil<TbItem>().setData(tbItem);
    }

//    @RequestMapping(value = "/admin/item/del/{ids}",method = RequestMethod.DELETE)
//    @ApiOperation(value = "删除商品")
//    public Result<TbItem> deleteItem(@PathVariable Long[] ids){
//
//        for(Long id:ids){
//            itemService.deleteItem(id);
//        }
//        return new ResultUtil<TbItem>().setData(null);
//    }

    @RequestMapping(value = "/admin/item/add",method = RequestMethod.POST)
    public Result<TbItem> addItem(ItemDto itemDto){
        TbItem tbItem=itemService.addItem(itemDto);
        return new ResultUtil<TbItem>().setData(tbItem);
    }

    @RequestMapping(value = "/admin/item/update/{id}",method = RequestMethod.POST)
    public Result<TbItem> updateItem(@PathVariable Long id, ItemDto itemDto){

        TbItem tbItem=itemService.updateItem(id,itemDto);
        return new ResultUtil<TbItem>().setData(tbItem);
    }

//    @RequestMapping(value = "/admin/admin/item/importIndex",method = RequestMethod.GET)
//    @ApiOperation(value = "导入商品索引至ES")
//    public Result<Object> importIndex(){
//
//        searchItemService.importAllItems();
//        return new ResultUtil<Object>().setData(null);
//    }
//
//    @RequestMapping(value = "/admin/admin/es/getInfo",method = RequestMethod.GET)
//    @ApiOperation(value = "获取ES信息")
//    public Result<Object> getESInfo(){
//
//        EsInfo esInfo=searchItemService.getEsInfo();
//        return new ResultUtil<Object>().setData(esInfo);
//    }

}
