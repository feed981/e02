package com.feed02;

import com.feed02.file.service.FileStorageService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest(classes = MinIOApplication.class)
@RunWith(SpringRunner.class)
public class MinIOTest2 {

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void test() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("D:\\freemaker\\list.html");
        String path = fileStorageService.uploadHtmlFile("", "list.html", fileInputStream);
        System.out.println(path);
    }


//    public static void main(String[] args) {
//        try {
//            FileInputStream fileInputStream = new FileInputStream("D:\\freemaker\\list.html");
//            // 获取连接 ，创建客户端
//            MinioClient minioClient = MinioClient.builder()
//                    .credentials("minioadmin" ,"minioadmin")
//                    .endpoint("http://192.168.33.11:9000").build();
//
//            // 上传
//            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
//                    .object("list.html") // 文件名称
//                    .contentType("text/html") // 类型
//                    .bucket("leadnews") // 桶名称
//                    .stream(fileInputStream, fileInputStream.available(), -1)
//                    .build();
//
//            minioClient.putObject(putObjectArgs);
//            System.out.println("http://192.168.33.11:9000/leadnews/list.html");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

}
