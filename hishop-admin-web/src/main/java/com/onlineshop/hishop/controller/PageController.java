package com.onlineshop.hishop.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@Api(description = "页面管理")
public class PageController {


    @RequestMapping("/")
    @ApiOperation(value = "主页")
    public String showIndex(){
        return "index";
    }

    @RequestMapping("/{page}")
    @ApiOperation(value = "页面跳转")
    public String showPage(@PathVariable String page){
        return page;
    }
}
