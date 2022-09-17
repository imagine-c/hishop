package com.onlineshop.hishop.feign;

import com.onlineshop.hishop.domain.KindEditorResult;
import com.onlineshop.hishop.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


@Component
@FeignClient(value = "hishop-admin")
public interface ImageFeignService {

    @RequestMapping(value = "/admin/image/imageUpload",method = RequestMethod.POST)
    public Result<Object> uploadFile(@RequestParam("file") MultipartFile files,
                                     HttpServletRequest request);

    @RequestMapping(value = "/admin/kindeditor/imageUpload",method = RequestMethod.POST)
    public KindEditorResult kindeditor(@RequestParam("imgFile") MultipartFile files, HttpServletRequest request);
}
