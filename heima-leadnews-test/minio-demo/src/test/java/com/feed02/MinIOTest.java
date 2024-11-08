package com.feed02;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinIOTest {
    private static final String ENDPOINT = "http://192.168.33.11:9000";
    private final static String separator = "/";
    private static final String BUCKET = "leadnews";
    public static void main(String[] args) {
//        upload("D:\\freemaker\\list.html" , "list.html" ,"text/html");
//        upload("D:\\local\\plugins\\css\\index.css" , "plugins/css/index.css" ,"text/css");
//        upload("D:\\local\\plugins\\js\\index.js"   ,"plugins/js/index.js" ,"text/js");
        upload("D:\\local\\plugins\\js\\axios.min.js"   ,"plugins/js/axios.min.js" ,"text/js");
    }

    public static void upload(String fileInputStreamPath ,String putObjectArgsObjectPath ,String contentType){
        try {
            FileInputStream fileInputStream = new FileInputStream(fileInputStreamPath);
            // 获取连接 ，创建客户端
            MinioClient minioClient = MinioClient.builder()
                    .credentials("minioadmin" ,"minioadmin")
                    .endpoint(ENDPOINT).build();

            // 上传
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object(putObjectArgsObjectPath) // 文件名称
                    .contentType(contentType) // 类型
                    .bucket(BUCKET) // 桶名称
                    .stream(fileInputStream, fileInputStream.available(), -1)
                    .build();

            minioClient.putObject(putObjectArgs);
            System.out.println(ENDPOINT + separator + BUCKET + separator + putObjectArgsObjectPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
