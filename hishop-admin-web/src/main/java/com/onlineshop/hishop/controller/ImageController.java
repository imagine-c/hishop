package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.KindEditorResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.feign.ImageFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
@Api(description = "图片上传统一接口")
public class ImageController {

    @Autowired
    private ImageFeignService imageFeignService;

    @RequestMapping(value = "/image/imageUpload",method = RequestMethod.POST)
    @ApiOperation(value = "WebUploader图片上传")
    public Result<Object> uploadFile(@RequestParam("file") MultipartFile files,
                                     HttpServletRequest request){

        return imageFeignService.uploadFile(files,request);
    }

    @RequestMapping(value = "/kindeditor/imageUpload",method = RequestMethod.POST)
    @ApiOperation(value = "KindEditor图片上传")
    public KindEditorResult kindeditor(@RequestParam("imgFile") MultipartFile files, HttpServletRequest request){

        return imageFeignService.kindeditor(files,request);
    }
}
