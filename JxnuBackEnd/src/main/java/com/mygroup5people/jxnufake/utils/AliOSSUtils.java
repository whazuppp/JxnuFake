package com.mygroup5people.jxnufake.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.UUID;
/**
 * 阿里云 OSS 工具类
 */
@Slf4j
@Component
public class AliOSSUtils {
    //注入配置参数实体类对象
    @Autowired
    private AliOSSProperties aliOSSProperties;
    public String upload(MultipartFile multipartFile) throws
            IOException {
        // 获取上传的文件的输入流
        InputStream inputStream = multipartFile.getInputStream();
        // 避免文件覆盖
        String originalFilename =
                multipartFile.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() +
                originalFilename.substring(originalFilename.lastIndexOf("."));
        //上传文件到 OSS
        OSS ossClient = new
                OSSClientBuilder().build(aliOSSProperties.getEndpoint(),
                aliOSSProperties.getAccessKeyId(),
                aliOSSProperties.getAccessKeySecret());
        ossClient.putObject(aliOSSProperties.getBucketName(),
                fileName, inputStream);
        //文件访问路径
        String url =aliOSSProperties.getEndpoint().split("//")[0] +
                "//" + aliOSSProperties.getBucketName() + "." +
                aliOSSProperties.getEndpoint().split("//")[1] + "/" + fileName;
        // 关闭ossClient
        ossClient.shutdown();
        return url;// 把上传到oss的路径返回
    }

}
