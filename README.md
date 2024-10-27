# 网关-app端网关搭建

在heima-leadnews-gateway下创建heima-leadnews-app-gateway微服务

引导类：

```java
package com.heima.app.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  //开启注册中心
public class AppGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppGatewayApplication.class,args);
    }
}
```

bootstrap.yml

```yaml
server:
  port: 51601
spring:
  application:
    name: leadnews-app-gateway
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.33.11:8848
      config:
        server-addr: 192.168.33.11:8848
        file-extension: yml
```

在nacos的配置中心创建dataid为leadnews-app-gateway的yml配置


```yaml
spring:
  cloud:
    gateway:
      globalcors:
        add-to-simple-url-handler-mapping: true
        corsConfigurations:
          '[/**]':
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
```


添加依赖

spring.cloud.version 2020.0.5 和 nacos 2.2.1.RELEASE 不兼容
        
父工程 heima-leadnews
```xml

    <!-- 继承Spring boot工程 -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.9.RELEASE</version>
    </parent>

   <properties>
      <com.alibaba.cloud>2.2.1.RELEASE</com.alibaba.cloud>

        <spring.cloud.version>Hoxton.SR3</spring.cloud.version>
    </properties>

            <!-- Spring Cloud Dependencies -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring.cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

        <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
                <version>${com.alibaba.cloud}</version>
            </dependency>

            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
                <version>${com.alibaba.cloud}</version>
            </dependency>

```
子工程 heima-leadnews-gateway

```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>

        <dependency>
            <groupId>javax.validation</groupId>
            <artifactId>validation-api</artifactId>
            <version>2.0.1.Final</version> <!-- 或者最新版本 -->
        </dependency>

        <dependency>
            <groupId>org.hibernate.validator</groupId>
            <artifactId>hibernate-validator</artifactId>
            <version>6.1.5.Final</version> <!-- 或者最新版本 -->
        </dependency>

        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>

        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
        </dependency>
```

应用程序在启动时遇到了 NoClassDefFoundError 和 ClassNotFoundException，特别是缺少 javax.validation.ValidationException 类。这通常表明您的项目缺少必要的依赖项，尤其是与 Bean 验证相关的类。

缺失的依赖：
javax.validation.ValidationException 是 Java Bean Validation API 的一部分，通常由 javax.validation:validation-api 提供。这个类在 Spring Cloud Gateway 中用于验证配置和参数。
Spring Cloud 版本：
您使用的是 Spring Cloud Hoxton.SR3 和 Spring Cloud Gateway 2.2.2.RELEASE。这些版本可能需要特定的依赖项来支持其功能。


添加验证 API 依赖：
在您的 pom.xml 中添加以下依赖，以引入 Java Bean Validation API：
```xml
<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
    <version>2.0.1.Final</version> <!-- 或者最新版本 -->
</dependency>
```
添加实现库：
通常，您还需要一个实现库，例如 Hibernate Validator。可以添加以下依赖：
```xml
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>6.1.5.Final</version> <!-- 或者最新版本 -->
</dependency>
```



环境搭建完成以后，启动项目网关和用户两个服务，使用postman进行测试

请求地址：http://localhost:51601/user/api/v1/login/login_auth   
