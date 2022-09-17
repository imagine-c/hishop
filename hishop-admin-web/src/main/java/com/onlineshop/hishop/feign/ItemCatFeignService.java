package com.onlineshop.hishop.feign;

import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.domain.ZTreeNode;
import com.onlineshop.hishop.pojo.TbItemCat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
@FeignClient(value = "hishop-admin")
public interface ItemCatFeignService {

    @RequestMapping(value = "/admin/item/cat/list",method = RequestMethod.GET)
    public List<ZTreeNode> getItemCatList(@RequestParam(name="id",defaultValue = "0") int parentId);

    @RequestMapping(value = "/admin/item/cat/add",method = RequestMethod.POST)
    public Result<Object> addItemCategory(@ModelAttribute TbItemCat tbItemCat);

    @RequestMapping(value = "/admin/item/cat/update",method = RequestMethod.POST)
    public Result<Object> updateItemCategory(@ModelAttribute TbItemCat tbItemCat);

    @RequestMapping(value = "/admin/item/cat/del/{id}",method = RequestMethod.DELETE)
    public Result<Object> deleteItemCategory(@PathVariable Long id);
}
