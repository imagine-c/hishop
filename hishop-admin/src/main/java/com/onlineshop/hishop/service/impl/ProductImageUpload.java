package com.onlineshop.hishop.service.impl;

import com.onlineshop.hishop.exception.HiShopUploadException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.net.URI;
@Component
@PropertySource("classpath:conf/admin.properties")
public class ProductImageUpload {

    @Value("${hadoop.host}")
    private String host;

    public static Configuration ConnectHdfs(){
        Configuration configuration = new Configuration();
        configuration.set("dfs.client.use.datanode.hostname","true");
        return configuration;
    }

    public void uploadImg(String LocalPath,String ServerPath,String FileName){
        try {
            FileSystem fileSystem = FileSystem.get(new URI(host),ConnectHdfs(),"root");
            if (!fileSystem.exists(new Path(ServerPath)))
                fileSystem.mkdirs(new Path(ServerPath));
            fileSystem.copyFromLocalFile(new Path(LocalPath+FileName),new Path(ServerPath));
        }catch (Exception e){
            throw new HiShopUploadException(e.toString());
        }

    }
}
