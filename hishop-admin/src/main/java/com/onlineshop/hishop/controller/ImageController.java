package com.onlineshop.hishop.controller;

import com.onlineshop.hishop.domain.KindEditorResult;
import com.onlineshop.hishop.domain.Result;
import com.onlineshop.hishop.service.ImageService;
import com.onlineshop.hishop.utils.FileUploadUtil;
import com.onlineshop.hishop.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;


@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "/admin/image/imageUpload",method = RequestMethod.POST)
    public Result<Object> uploadFile(@RequestParam("file") MultipartFile files,
                                     HttpServletRequest request){

        String imagePath=null;
        // 文件保存路径
        String filePath = request.getServletContext().getRealPath("/admin/upload/");
        String fileName = FileUploadUtil.rename(files.getOriginalFilename());
        // 转存文件
        File file=new File(filePath);
        if (!file.exists())
            file.mkdirs();
        try {
            //保存至服务器
            File file1=new File((filePath+fileName));
            files.transferTo(file1);
            imagePath= imageService.uploadImage(filePath,fileName);
            // 路径为文件且不为空则进行删除
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return new ResultUtil<Object>().setErrorMsg("上传失败");
        }
        return new ResultUtil<Object>().setData(imagePath);
    }

    @RequestMapping(value = "/admin/kindeditor/imageUpload",method = RequestMethod.POST)
    public KindEditorResult kindeditor(@RequestParam("imgFile") MultipartFile files, HttpServletRequest request){

        KindEditorResult kindEditorResult=new KindEditorResult();
        // 文件保存路径
        String filePath = request.getServletContext().getRealPath("/admin/upload/");
        String fileName = FileUploadUtil.rename(files.getOriginalFilename());
        System.out.println(filePath+fileName);
        //检查文件
        String message=FileUploadUtil.isValidImage(request,files);
        if(!message.equals("valid")){
            kindEditorResult.setError(1);
            kindEditorResult.setMessage(message);
            return kindEditorResult;
        }
        File file=new File(filePath);
        if (!file.exists())
            file.mkdirs();
        if (file.getParentFile().exists())
            System.out.println("file");
        // 转存文件
        try {
            //保存至服务器
            File file1=new File((filePath+fileName));
            files.transferTo(file1);
            String imagePath=imageService.uploadImage(filePath,fileName);
            // 路径为文件且不为空则进行删除
            if (file1.isFile() && file1.exists()) {
                file1.delete();
            }
            kindEditorResult.setError(0);
            kindEditorResult.setUrl(imagePath);
            return kindEditorResult;
        } catch (IOException e) {
            e.printStackTrace();
        }
        kindEditorResult.setError(1);
        kindEditorResult.setMessage("上传失败");
        return kindEditorResult;
    }
}
