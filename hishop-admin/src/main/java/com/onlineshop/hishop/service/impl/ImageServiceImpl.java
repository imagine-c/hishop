package com.onlineshop.hishop.service.impl;

import com.onlineshop.hishop.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:conf/admin.yml")
public class ImageServiceImpl implements ImageService{
    @Autowired
    private ProductImageUpload productImageUpload;
    @Value("${server.host}")
    private String host;
    @Value("${server.path}")
    private String ServerPath;

    @Override
    public String uploadImage(String localPath, String imgName){
        String imgPath= host+ServerPath+imgName;
        productImageUpload.uploadImg(localPath,ServerPath,imgName);
        return imgPath;
    }
}
