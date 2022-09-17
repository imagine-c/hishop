package com.onlineshop.hishop.controller;

import cn.hutool.core.util.StrUtil;
import com.onlineshop.hishop.constant.DictConstant;
import com.onlineshop.hishop.domain.DataTablesResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.pojo.TbDict;
import com.onlineshop.hishop.service.DictService;
import com.onlineshop.hishop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
public class DictController {

    @Autowired
    private DictService dictService;
    
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/admin/getDictList",method = RequestMethod.GET)
    public String getDictExtList(HttpServletResponse response){

        String result = "";
        String v = redisTemplate.opsForValue().get(DictConstant.EXT_KEY);
        if(StrUtil.isNotBlank(v)){
            return v;
        }
        List<TbDict> list=dictService.getDictList();
        for(TbDict tbDict : list){
            result += tbDict.getDict() + "\n";
        }
        if(StrUtil.isNotBlank(result)) {
            redisTemplate.opsForValue().set(DictConstant.EXT_KEY, result);
        }
        response.addHeader(DictConstant.LAST_MODIFIED, redisTemplate.opsForValue().get(DictConstant.LAST_MODIFIED));
        response.addHeader(DictConstant.ETAG, redisTemplate.opsForValue().get(DictConstant.ETAG));
        return result;
    }

    @RequestMapping(value = "/admin/getStopDictList",method = RequestMethod.GET)
    public String getStopDictList(HttpServletResponse response){

        String result = "";
        String v = redisTemplate.opsForValue().get(DictConstant.STOP_KEY);
        if(StrUtil.isNotBlank(v)){
            return v;
        }
        List<TbDict> list=dictService.getStopList();
        for(TbDict tbDict : list){
            result += tbDict.getDict() + "\n";
        }
        if(StrUtil.isNotBlank(result)){
            redisTemplate.opsForValue().set(DictConstant.STOP_KEY, result);
        }
        response.addHeader(DictConstant.LAST_MODIFIED, redisTemplate.opsForValue().get(DictConstant.LAST_MODIFIED));
        response.addHeader(DictConstant.ETAG, redisTemplate.opsForValue().get(DictConstant.ETAG));
        return result;
    }

    @RequestMapping(value = "/admin/dict/list",method = RequestMethod.GET)
    public DataTablesResult getDictList(){

        DataTablesResult result = new DataTablesResult();
        List<TbDict> list=dictService.getDictList();
        result.setData(list);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/admin/dict/stop/list",method = RequestMethod.GET)
    public DataTablesResult getStopList(){

        DataTablesResult result = new DataTablesResult();
        List<TbDict> list=dictService.getStopList();
        result.setData(list);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/admin/dict/add",method = RequestMethod.POST)
    public Result<Object> addDict(@ModelAttribute TbDict tbDict){

        dictService.addDict(tbDict);
        update();
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/dict/update",method = RequestMethod.POST)
    public Result<Object> updateDict(@ModelAttribute TbDict tbDict){

        dictService.updateDict(tbDict);
        update();
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/dict/del/{ids}",method = RequestMethod.DELETE)
    public Result<Object> delDict(@PathVariable int[] ids){

        for(int id:ids){
            dictService.delDict(id);
        }
        update();
        return new ResultUtil<Object>().setData(null);
    }

    public void update(){
        //更新词典标识
        redisTemplate.opsForValue().set(DictConstant.LAST_MODIFIED, String.valueOf(System.currentTimeMillis()));
        redisTemplate.opsForValue().set(DictConstant.ETAG, String.valueOf(System.currentTimeMillis()));
        //更新缓存
        redisTemplate.delete(DictConstant.EXT_KEY);
        redisTemplate.delete(DictConstant.STOP_KEY);
    }
}
