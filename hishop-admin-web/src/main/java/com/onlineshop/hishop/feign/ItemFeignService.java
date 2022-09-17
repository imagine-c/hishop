package com.onlineshop.hishop.feign;

import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.dto.ItemDto;
import com.onlineshop.hishop.pojo.TbItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "hishop-admin")
public interface ItemFeignService {


    @RequestMapping(value = "/admin/item/{itemId}",method = RequestMethod.GET)
    public Result<ItemDto> getItemById(@PathVariable Long itemId);

    @RequestMapping(value = "/admin/item/list",method = RequestMethod.GET)
    public DataTablesResult getItemList(@RequestParam int draw, @RequestParam int start,
                                        @RequestParam int length, @RequestParam int cid,
                                        @RequestParam("search[value]") String search,
                                        @RequestParam("order[0][column]") int orderCol,
                                        @RequestParam("order[0][dir]") String orderDir);

    @RequestMapping(value = "/admin/item/listSearch",method = RequestMethod.GET)
    
    public DataTablesResult getItemSearchList(@RequestParam int draw, @RequestParam int start,
                                              @RequestParam int length,@RequestParam int cid,
                                              @RequestParam String searchKey, @RequestParam String minDate,
                                              @RequestParam String maxDate, @RequestParam("search[value]") String search,
                                              @RequestParam("order[0][column]") int orderCol,
                                              @RequestParam("order[0][dir]") String orderDir);

    @RequestMapping(value = "/admin/item/count",method = RequestMethod.GET)
    public DataTablesResult getAllItemCount();

    @RequestMapping(value = "/admin/item/stop/{id}",method = RequestMethod.PUT)
    public Result<TbItem> stopItem(@PathVariable Long id);


    @RequestMapping(value = "/admin/item/start/{id}",method = RequestMethod.PUT)
    public Result<TbItem> startItem(@PathVariable Long id);

//    @RequestMapping(value = "/admin/item/del/{ids}",method = RequestMethod.DELETE)
//    public Result<TbItem> deleteItem(@PathVariable Long[] ids);

    @RequestMapping(value = "/admin/item/add",method = RequestMethod.POST)
    public Result<TbItem> addItem(ItemDto itemDto);

    @RequestMapping(value = "/admin/item/update/{id}",method = RequestMethod.POST)
    public Result<TbItem> updateItem(@PathVariable Long id, ItemDto itemDto);

//    @RequestMapping(value = "/admin/admin/item/importIndex",method = RequestMethod.GET)
//    
//    public Result<Object> importIndex(){
//
//        searchItemService.importAllItems();
//        return new ResultUtil<Object>().setData(null);
//    }
//
//    @RequestMapping(value = "/admin/admin/es/getInfo",method = RequestMethod.GET)
//    
//    public Result<Object> getESInfo(){
//
//        EsInfo esInfo=searchItemService.getEsInfo();
//        return new ResultUtil<Object>().setData(esInfo);
//    }

}
