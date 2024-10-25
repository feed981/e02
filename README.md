# nacos-demo 

virtual box NAT 要增加连线埠转送规则 


| 名称 | 协议 | 主机 IP|主机端口|客户端 IP|客户端端口|
| -------- | -------- | -------- |-------- |-------- |-------- |
| Nacos HTTP     | TCP     | 127.0.0.1     |8848|192.168.33.11|8848|

先访问 http://127.0.0.1:8848/nacos 或 http://192.168.33.11:8848/nacos

## 开启Telnet

Win + R，输入 control
程序，然后选择 程序和功能
启用或关闭 Windows 功能
在弹出的窗口中，找到 Telnet 客户端

```bash 
C:\Users\f>telnet 192.168.33.11 8848
```

## 项目

```xml 
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.example</groupId>
  <artifactId>nacos-demo</artifactId>
  <packaging>jar</packaging>

  <name>nacos-demo</name>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.2.1.RELEASE</version>
    <relativePath/>
  </parent>

  <dependencies>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Boot Test 依赖 -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>

    <dependency>
      <groupId>com.alibaba.cloud</groupId>
      <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
      <version>2.2.1.RELEASE</version>
    </dependency>

    <dependency>
      <groupId>com.alibaba.cloud</groupId>
      <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
      <version>2.2.1.RELEASE</version>
    </dependency>

  </dependencies>

</project>

```
```yml 
server:
  port: 51801
spring:
  application:
    name: nacos-demo-service
  cloud:
    nacos:
      discovery:
        autoRegister: true # Nacos服务自动注册
        server-addr: 127.0.0.1:8848
      config:
        server-addr: 127.0.0.1:8848
        file-extension: yml

```
```java 
package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  // 开启服务发现功能
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

```
控制器

GET localhost:51801/config
访问失败 config.value 就打印 default-value 成功则是 aaa
```java 
package org.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Value("${config.value:default-value}")
    private String configValue;

    @GetMapping("/config")
    public String getConfig() {
        return "nacos: " + configValue;
    }
}
```

## nacos configuration 
http://127.0.0.1:8848/nacos 或 http://192.168.33.11:8848/nacos

| Column 1 | Column 2 | Column 3|
| -------- | -------- |-------- |
| Data ID     | nacos-demo-service     | 对应bootstrap.yml 的配置 spring:application:name: |
|Format|yaml||
|Configuration Content:|config: value: aaa||

# 功能实现

目标: Spring Boot 成功请求 Nacos 并收到返回

## 版本是 2.2.1.RELEASE

父工程 heima-leadnews

有太多依赖报错看的雾煞煞，所以父工程Spring Cloud  相关依赖都注解掉

原本Spring Boot 版本是 2.3.9.RELEASE Spring Cloud Alibaba 的版本是 2.2.6.RELEASE 报错

在社区和一些开发者的反馈中，通常建议使用相同主版本的 Spring Cloud 和 Spring Boot，以确保最佳兼容性

当前使用的 Spring Boot 版本是 2.2.1.RELEASE，而 Spring Cloud Alibaba 的版本是 2.2.1.RELEASE。这两个版本是兼容的，因此可以正常使用

```xml
       <!-- 继承Spring boot工程 -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.1.RELEASE</version>
    </parent>



            <!-- Spring Cloud Dependencies -->
<!--            <dependency>-->
<!--                <groupId>org.springframework.cloud</groupId>-->
<!--                <artifactId>spring-cloud-dependencies</artifactId>-->
<!--                <version>${spring.cloud.version}</version>-->
<!--                <type>pom</type>-->
<!--                <scope>import</scope>-->
<!--            </dependency>-->

            <!-- Spring Cloud Alibaba Dependencies -->
<!--            <dependency>-->
<!--                <groupId>com.alibaba.cloud</groupId>-->
<!--                <artifactId>spring-cloud-alibaba-dependencies</artifactId>-->
<!--                <version>${com.alibaba.cloud}</version>-->
<!--                <type>pom</type>-->
<!--                <scope>import</scope>-->
<!--            </dependency>-->

<!--            <dependency>-->
<!--                <groupId>com.alibaba.nacos</groupId>-->
<!--                <artifactId>nacos-client</artifactId>-->
<!--                <version>${nacos.version}</version> &lt;!&ndash; 确保使用与 nacos-server 兼容的版本 &ndash;&gt;-->
<!--            </dependency>-->

```

子工程 heima-leadnews-service

```xml
    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>

        <com.alibaba.cloud>2.2.1.RELEASE</com.alibaba.cloud>
    </properties>

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

## 项目

### 子工程 heima-leadnews-service

入口
```java
package com.feed02.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.feed02.user.mapper")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class ,args);
    }
}

```
控制器
```java
package com.feed02.user.controller.v1;

import com.feed02.model.common.dtos.ResponseResult;
import com.feed02.model.user.dtos.LoginDto;
import com.feed02.user.service.IApUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
public class ApUserLoginController {

    @Autowired
    private IApUserService apUserService;
    @PostMapping("/login_auth")
    public ResponseResult login(@RequestBody LoginDto dto){
        return apUserService.login(dto);
    }
}

```


mapper

```java
package com.feed02.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feed02.model.user.pojos.ApUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApUserMapper extends BaseMapper<ApUser> {
}

```

IService

```java
package com.feed02.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feed02.model.common.dtos.ResponseResult;
import com.feed02.model.user.dtos.LoginDto;
import com.feed02.model.user.pojos.ApUser;

public interface IApUserService extends IService<ApUser> {
    // app端登陆功能
    public ResponseResult login(LoginDto dto);
}

```

ServiceImpl

```java
package com.feed02.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feed02.model.common.dtos.ResponseResult;
import com.feed02.model.common.enums.AppHttpCodeEnum;
import com.feed02.model.user.dtos.LoginDto;
import com.feed02.model.user.pojos.ApUser;
import com.feed02.user.mapper.ApUserMapper;
import com.feed02.user.service.IApUserService;
import com.heima.utils.common.AppJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper , ApUser> implements IApUserService {
    @Override
    public ResponseResult login(LoginDto dto) {
        //1.正常登录 用户名和密码
        if(StringUtils.isNotBlank(dto.getPhone()) && StringUtils.isNotBlank(dto.getPassword())){
            //1.1 根据手机号查询用户信息
            ApUser dbUser = getOne(Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone, dto.getPhone()));
            if(dbUser == null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"用户信息不存在");
            }

            //1.2 比对密码
            String salt = dbUser.getSalt();
            String password = dto.getPassword();
            String pswd = DigestUtils.md5DigestAsHex((password + salt).getBytes());
            if(!pswd.equals(dbUser.getPassword())){
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }

            //1.3 返回数据 jwt
            Map<String, Object> map = new HashMap<>();
            map.put("token" , AppJwtUtil.getToken(dbUser.getId().longValue()));
            dbUser.setSalt("");
            dbUser.setPassword("");
            map.put("user",dbUser);
            return ResponseResult.okResult(map);
        }else {
            // 2. 游客登录
            Map<String, Object> map = new HashMap<>();
            map.put("token" , AppJwtUtil.getToken(0L));
            return ResponseResult.okResult(map);
        }
    }
}

```

bootstrap.yml

它在 Spring Boot 启动时会比 application.yml 更早加载，因此适合用于配置服务发现和配置中心等。

```yml
server:
  port: 51801
spring:
  application:
    name: leadnews-user
  cloud:
    nacos:
      discovery:
        autoRegister: true # Nacos服务自动注册
        server-addr: 127.0.0.1:8848
      config:
        server-addr: 127.0.0.1:8848
        file-extension: yml
```

### 子工程 heima-leadnews-model
dto

```java
package com.feed02.model.user.dtos;

import lombok.Data;

@Data
public class LoginDto {
    private String phone; // 手机号
    private String password; // 密码
}

```

### 子工程 heima-leadnews-utils
AppJwtUtil

## nacos configuration 
http://127.0.0.1:8848/nacos 或 http://192.168.33.11:8848/nacos

| Column 1 | Column 2 | 
| -------- | -------- |
| Data ID     | leadnews-user     | 
|Format|yaml|

Configuration Content
```yml
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/leadnews_user?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false
    username: root
    password: qwe123
# 设置Mapper接口所对应的XML文件位置，如果你在Mapper接口中有自定义方法，需要进行该配置
mybatis-plus:
  mapper-locations: classpath*:mapper/*.xml
  # 设置别名包扫描路径，通过该属性可以给包中的类注册别名
  type-aliases-package: com.feed02.model.user.pojos
```

## 测试


先把bootstrap.yml注掉 nacos相关都住解掉， 用 application.yml 这个侧
```yml 
server:
  port: 51801
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/leadnews_user?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false
    username: root
    password: qwe123
# 设置Mapper接口所对应的XML文件位置，如果你在Mapper接口中有自定义方法，需要进行该配置
mybatis-plus:
  mapper-locations: classpath*:mapper/*.xml
  # 设置别名包扫描路径，通过该属性可以给包中的类注册别名
  type-aliases-package: com.feed02.model.user.pojos
```

### 0. 访问时后console会狂跳这个讯息，建议还是先加下

 WARN: Establishing SSL connection without server's identity verification is not recommended. According to MySQL 5.5.45+, 5.6.26+ and 5.7.6+ requirements SSL connection must be established by default if explicit option isn't set. For compliance with existing applications not using SSL the verifyServerCertificate property is set to 'false'. You need either to explicitly disable SSL by setting useSSL=false, or set useSSL=true and provide truststore for server certificate verification.
 
 日志中有关于 SSL 连接的警告，表示在没有服务器身份验证的情况下建立 SSL 连接是不推荐的。这通常是因为你的 MySQL 数据库连接配置中未明确指定 useSSL 属性。
 
 不需要 SSL 连接，可以在数据库连接 URL 中添加 useSSL=false：


### 1. MyBatis 在处理某个表达式时遇到了问题

ERROR o.a.c.c.C.[.[.[.[dispatcherServlet] - Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.builder.BuilderException: Error evaluating expression 'ew.sqlSegment != null and ew.sqlSegment != '' and ew.nonEmptyOfWhere'. Cause: org.apache.ibatis.ognl.OgnlException: sqlSegment [java.lang.ExceptionInInitializerError]] with root cause
java.lang.reflect.InaccessibleObjectException: Unable to make field private final java.lang.Class java.lang.invoke.SerializedLambda.capturingClass accessible: module java.base does not "opens java.lang.invoke" to unnamed module @54e1c68b


org.mybatis.spring.MyBatisSystemException 和 java.lang.reflect.InaccessibleObjectException。这表明 MyBatis 在处理某个表达式时遇到了问题，可能与 Java 9+ 的模块系统有关。

启动应用时添加 JVM 参数，以打开需要的模块：

菜单栏中的 Run（运行），然后选择 Edit Configurations...
添加 VM 选项：
在右侧的设置面板中，找到 VM options（虚拟机选项）输入框。
```
--add-opens java.base/java.lang.invoke=ALL-UNNAMED
```

### 2. 在 pom.xml 中添加 JAXB 相关的依赖
ERROR o.a.c.c.C.[.[.[.[dispatcherServlet] - Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Handler dispatch failed; nested exception is java.lang.NoClassDefFoundError: javax/xml/bind/DatatypeConverter] with root cause
java.lang.ClassNotFoundException: javax.xml.bind.DatatypeConverter

添加 JAXB 依赖：
为了解决这个问题，你需要在 pom.xml 中添加 JAXB 相关的依赖。可以使用以下依赖来引入 javax.xml.bind：

```xml 
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.1</version>
</dependency>
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>2.3.1</version>
</dependency>
```

### 跑起来都没报错后 就可以在移回去bootstrap.yml 试试能不能请求 Nacos 并收到返回

localhost:51801/api/v1/login//login_auth

ok
```json
{"phone":"13511223456","password":"admin"}
```
游客模式
```json
{"phone":"","password":""} 
```
#密码错误
```json
{"phone":"13511223456","password":"abc123"}
```
用户信息不存在
```json
{"phone":"123","password":"abc123"}
```
