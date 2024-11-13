# 素材上传

1. 网关进行token解析后，把解析后的用户信息存储到header中
2. 自媒体微服务使用拦截器获取到header中的的用户信息，并放入到threadlocal中

- heima-leadnews-gateway/heima-leadnews-wemedia-gateway/src/main/java/com/feed02/wemedia/gateway/filter/AuthorizeFilter.java
- heima-leadnews-service/heima-leadnews-wemedia/src/main/java/com/feed02/wemedia/config/WebMvcConfig.java
- heima-leadnews-service/heima-leadnews-wemedia/src/main/java/com/feed02/wemedia/interceptor/WmTokenInterceptor.java
- heima-leadnews-utils/src/main/java/com/feed02/utils/thread/WmThreadLocalUtil.java

||||
|--|--|--|
| 接口路径 | /api/v1/material/upload_picture |
| 请求方式 | POST                            |
| 参数     | MultipartFile                   | Springmvc指定的文件接收类型|
| 响应结果 | ResponseResult                  |成功需要回显图片，返回素材对象|


1. 检查参数MultipartFile
2. 把图片上传到minIO中，获取到图片请求的路径
3. 存数据库 把用户id和图片上的路径保存到素材表中 
4. 返回结果

- heima-leadnews-model/src/main/java/com/feed02/model/wemedia/pojos/WmMaterial.java
- heima-leadnews-service/heima-leadnews-wemedia/pom.xml
- heima-leadnews-service/heima-leadnews-wemedia/src/main/java/com/feed02/wemedia/controller/v1/WmMaterialController.java
- heima-leadnews-service/heima-leadnews-wemedia/src/main/java/com/feed02/wemedia/mapper/WmMaterialMapper.java
- heima-leadnews-service/heima-leadnews-wemedia/src/main/java/com/feed02/wemedia/service/WmMaterialService.java
- heima-leadnews-service/heima-leadnews-wemedia/src/main/java/com/feed02/wemedia/service/impl/WmMaterialServiceImpl.java


# 访问 MinIO 中的图片链接显示出编码错误的字符

是因为未正确识别文件的内容类型（MIME 类型）

MinIO 服务器返回的数据被浏览器解释为文本内容，而不是图片

检查 MinIO 的 Content-Type 配置 image/jpeg

```shell
curl -I http://<IP>:9000/leadnews/2024/11/13/d572dda3dcef41d2b4e4b2a9880e605e.jpg
```

- heima-leadnews-basic/heima-file-spring-boot-starter/src/main/java/com/feed02/file/service/FileStorageService.java
- heima-leadnews-basic/heima-file-spring-boot-starter/src/main/java/com/feed02/file/service/impl/MinIOFileStorageService.java
- heima-leadnews-service/heima-leadnews-article/src/test/java/feed02/CreateStaticUrlTest.java
- heima-leadnews-test/minio-demo/src/test/java/com/feed02/MinIOTest2.java
