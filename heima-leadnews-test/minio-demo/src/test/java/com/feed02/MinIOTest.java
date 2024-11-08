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

    public static void main(String[] args) {

        try {
            FileInputStream fileInputStream = new FileInputStream("D:\\freemaker\\list.html");
            // 获取连接 ，创建客户端
            MinioClient minioClient = MinioClient.builder()
                    .credentials("minioadmin" ,"minioadmin")
                    .endpoint("http://192.168.33.11:9000").build();

            // 上传
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object("list.html") // 文件名称
                    .contentType("text/html") // 类型
                    .bucket("leadnews") // 桶名称
                    .stream(fileInputStream, fileInputStream.available(), -1)
                    .build();

            minioClient.putObject(putObjectArgs);
            System.out.println("http://192.168.33.11:9000/leadnews/list.html");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
