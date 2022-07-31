package com.onlineshop.hishop.utils;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class FileUploadUtil {

    public static String rename(String fileName){
        String extName = fileName.substring(fileName.lastIndexOf("."));
        return UUID.randomUUID().toString().replace("-","")+extName;
    }

    public  static String extension(String fileName){
        int index = fileName.lastIndexOf(".");
        //获取文件扩展名
        if (index == -1) {
            return null;
        }
        return fileName.substring(index + 1);
    }

    public static String isValidImage(HttpServletRequest request, MultipartFile file){
        //最大文件大小
        long maxSize = 5242880;
        //定义允许上传的文件扩展名
        HashMap<String, String> extMap = new HashMap<String, String>();
        extMap.put("image", "gif,jpg,jpeg,png,bmp");

        if(!ServletFileUpload.isMultipartContent(request)){
            return "请选择文件";
        }

        if(file.getSize() > maxSize){
            return "上传文件大小超过5MB限制";
        }
        //检查扩展名
        String fileName=file.getOriginalFilename();
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if(!Arrays.<String>asList(extMap.get("image").split(",")).contains(fileExt)){
            return "上传文件扩展名是不允许的扩展名\n只允许" + extMap.get("image") + "格式";
        }

        return "valid";
    }
}
