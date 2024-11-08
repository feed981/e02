INSERT INTO nacos_config.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES(2, 'leadnews-app-gateway', 'DEFAULT_GROUP', 'spring:
  cloud:
    gateway:
      globalcors:
        add-to-simple-url-handler-mapping: true
        corsConfigurations:
          ''[/**]'':
            allowedHeaders: "*"
            allowedOrigins: "*"
            allowedMethods:
              - GET
              - POST
              - DELETE
              - PUT
              - OPTION
      routes:
        # 平台管理
        - id: user
          uri: lb://leadnews-user
          predicates:
            - Path=/user/**
          filters:
            - StripPrefix= 1
        # 文章管理
        - id: article
          uri: lb://leadnews-article
          predicates:
            - Path=/article/**
          filters:
            - StripPrefix= 1', '4de57935515904aa476b3c5a7c9ab88e', '2024-11-01 19:48:13', '2024-11-01 19:48:13', NULL, '10.0.2.2', '', '', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO nacos_config.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES(3, 'leadnews-article', 'DEFAULT_GROUP', 'spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/leadnews_article?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false
    username: root
    password: qwe123
# 设置Mapper接口所对应的XML文件位置，如果你在Mapper接口中有自定义方法，需要进行该配置
mybatis-plus:
  mapper-locations: classpath*:mapper/*.xml
  # 设置别名包扫描路径，通过该属性可以给包中的类注册别名
  type-aliases-package: com.feed02.model.article.pojos

file:
  minio:
    accessKey: minioadmin
    secretKey: minioadmin
    bucket: leadnews
    endpoint: http://192.168.33.11:9000/
    readPath: http://192.168.33.11:9000/', '7fdd61984be8a7357a14e526b4f8f419', '2024-11-01 19:48:37', '2024-11-08 16:08:01', NULL, '10.0.2.2', '', '', '', '', '', 'yaml', '', '');
INSERT INTO nacos_config.config_info (id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, `type`, c_schema, encrypted_data_key) VALUES(4, 'leadnews-user', 'DEFAULT_GROUP', 'spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/leadnews_user?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false
    username: root
    password: qwe123
# 设置Mapper接口所对应的XML文件位置，如果你在Mapper接口中有自定义方法，需要进行该配置
mybatis-plus:
  mapper-locations: classpath*:mapper/*.xml
  # 设置别名包扫描路径，通过该属性可以给包中的类注册别名
  type-aliases-package: com.feed02.model.user.pojos', 'ce4f77aeab14c302f9319753b9aed693', '2024-11-01 19:48:55', '2024-11-01 19:48:55', NULL, '10.0.2.2', '', '', NULL, NULL, NULL, 'yaml', NULL, '');
