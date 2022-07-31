package com.onlineshop.hishop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.onlineshop.hishop.pojo.TbDict;
import com.onlineshop.hishop.service.DictService;
import cn.hutool.core.util.StrUtil;
import com.onlineshop.hishop.constant.DictConstant;
import com.onlineshop.hishop.jedis.JedisClient;
import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@Api(description = "词典库")
public class DictController {

    @Reference
    private DictService dictService;

    @Autowired
    private JedisClient jedisClient;

    @RequestMapping(value = "/getDictList",method = RequestMethod.GET)
    @ApiOperation(value = "获得所有扩展词库")
    public String getDictExtList(HttpServletResponse response){

        String result = "";
        String v = jedisClient.get(DictConstant.EXT_KEY);
        if(StrUtil.isNotBlank(v)){
            return v;
        }
        List<TbDict> list=dictService.getDictList();
        for(TbDict tbDict : list){
            result += tbDict.getDict() + "\n";
        }
        if(StrUtil.isNotBlank(result)) {
            jedisClient.set(DictConstant.EXT_KEY, result);
        }
        response.addHeader(DictConstant.LAST_MODIFIED, jedisClient.get(DictConstant.LAST_MODIFIED));
        response.addHeader(DictConstant.ETAG, jedisClient.get(DictConstant.ETAG));
        return result;
    }

    @RequestMapping(value = "/getStopDictList",method = RequestMethod.GET)
    @ApiOperation(value = "获得所有扩展词库")
    public String getStopDictList(HttpServletResponse response){

        String result = "";
        String v = jedisClient.get(DictConstant.STOP_KEY);
        if(StrUtil.isNotBlank(v)){
            return v;
        }
        List<TbDict> list=dictService.getStopList();
        for(TbDict tbDict : list){
            result += tbDict.getDict() + "\n";
        }
        if(StrUtil.isNotBlank(result)){
            jedisClient.set(DictConstant.STOP_KEY, result);
        }
        response.addHeader(DictConstant.LAST_MODIFIED, jedisClient.get(DictConstant.LAST_MODIFIED));
        response.addHeader(DictConstant.ETAG, jedisClient.get(DictConstant.ETAG));
        return result;
    }

    @RequestMapping(value = "/dict/list",method = RequestMethod.GET)
    @ApiOperation(value = "获得所有扩展词库")
    public DataTablesResult getDictList(){

        DataTablesResult result = new DataTablesResult();
        List<TbDict> list=dictService.getDictList();
        result.setData(list);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/dict/stop/list",method = RequestMethod.GET)
    @ApiOperation(value = "获得所有停用词库")
    public DataTablesResult getStopList(){

        DataTablesResult result = new DataTablesResult();
        List<TbDict> list=dictService.getStopList();
        result.setData(list);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/dict/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加词典")
    public Result<Object> addDict(@ModelAttribute TbDict tbDict){

        dictService.addDict(tbDict);
        update();
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/dict/update",method = RequestMethod.POST)
    @ApiOperation(value = "编辑词典")
    public Result<Object> updateDict(@ModelAttribute TbDict tbDict){

        dictService.updateDict(tbDict);
        update();
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/dict/del/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除词典")
    public Result<Object> delDict(@PathVariable int[] ids){

        for(int id:ids){
            dictService.delDict(id);
        }
        update();
        return new ResultUtil<Object>().setData(null);
    }

    public void update(){
        //更新词典标识
        jedisClient.set(DictConstant.LAST_MODIFIED, String.valueOf(System.currentTimeMillis()));
        jedisClient.set(DictConstant.ETAG, String.valueOf(System.currentTimeMillis()));
        //更新缓存
        jedisClient.del(DictConstant.EXT_KEY);
        jedisClient.del(DictConstant.STOP_KEY);
    }
}
