@ConfigurationProperties(prefix = "file.minio")

Spring Boot 从配置文件中加载以 file.minio 为前缀的属性并绑定到 MinIOConfigProperties 类的字段上

```yml
file:
  minio:
    accessKey: minioadmin
    secretKey: minioadmin
    bucket: leadnews
    endpoint: http://192.168.33.11:9000
    readPath: http://192.168.33.11:9000
```

