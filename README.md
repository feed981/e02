# 运营端微服务搭建

引导类

```java
package com.heima.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.heima.user.mapper")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }
}
```

bootstrap.yml

```yaml
server:
  port: 51801
spring:
  application:
    name: leadnews-user
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.33.11:8848
      config:
        server-addr: 192.168.33.11:8848
        file-extension: yml
```

在nacos中创建配置文件


```yaml
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/leadnews_user?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
    username: root
    password: root
# 设置Mapper接口所对应的XML文件位置，如果你在Mapper接口中有自定义方法，需要进行该配置
mybatis-plus:
  mapper-locations: classpath*:mapper/*.xml
  # 设置别名包扫描路径，通过该属性可以给包中的类注册别名
  type-aliases-package: com.heima.model.user.pojos
```

logback.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>

<configuration>
    <!--定义日志文件的存储地址,使用绝对路径-->
    <property name="LOG_HOME" value="e:/logs"/>

    <!-- Console 输出设置 -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
            <charset>utf8</charset>
        </encoder>
    </appender>

    <!-- 按照每天生成日志文件 -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <fileNamePattern>${LOG_HOME}/leadnews.%d{yyyy-MM-dd}.log</fileNamePattern>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- 异步输出 -->
    <appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
        <!-- 不丢失日志.默认的,如果队列的80%已满,则会丢弃TRACT、DEBUG、INFO级别的日志 -->
        <discardingThreshold>0</discardingThreshold>
        <!-- 更改默认的队列的深度,该值会影响性能.默认值为256 -->
        <queueSize>512</queueSize>
        <!-- 添加附加的appender,最多只能添加一个 -->
        <appender-ref ref="FILE"/>
    </appender>


    <logger name="org.apache.ibatis.cache.decorators.LoggingCache" level="DEBUG" additivity="false">
        <appender-ref ref="CONSOLE"/>
    </logger>
    <logger name="org.springframework.boot" level="debug"/>
    <root level="info">
        <!--<appender-ref ref="ASYNC"/>-->
        <appender-ref ref="FILE"/>
        <appender-ref ref="CONSOLE"/>
    </root>
</configuration>
```

# 问题
## 父工程 pom.xml <modules> 标签用于定义该父项目包含的子模块

父工程
```xml
    <modules>
        <module>heima-leadnews-model</module>
        <module>heima-leadnews-service</module>
    </modules>
```

标注完还是无效的话
- maven clean 
- 删除 .idea 文件夹和 *.iml 文件，然后重新导入项目

## 父工程没相关的依赖导致子工程要用这个依赖时后找不到，就报错


## 子工程依赖另个子工程时，父工程也要提供内部依赖的版本

父工程
```xml
 <!--内部依赖工程 -->

            <dependency>
                <artifactId>heima-leadnews-model</artifactId>
                <groupId>com.feed02</groupId>
                <version>${project.version}</version>
            </dependency>
```

## 无法导入 org.springframework.cloud.client.discovery.EnableDiscoveryClient 

父工程 heima-leadnews
```xml
     <modules>
        <module>heima-leadnews-model</module>
        <module>heima-leadnews-service</module>
    </modules>

     <properties>
        <spring.cloud.version>Hoxton.SR10</spring.cloud.version>
        <com.alibaba.cloud>2.2.5.RELEASE</com.alibaba.cloud>
    </properties>

     <dependencyManagement>

        <dependencies>

            <!-- Spring Cloud Dependencies -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring.cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <!-- Spring Cloud Alibaba Dependencies -->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${com.alibaba.cloud}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

        </dependencies>
    </dependencyManagement>
```
子工程 heima-leadnews-service
```xml
    <parent>
        <artifactId>heima-leadnews</artifactId>
        <groupId>com.feed02</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <packaging>pom</packaging>
     <modules>
        <module>heima-leadnews-user</module>
    </modules>

    <artifactId>heima-leadnews-service</artifactId>

    <dependencies>
        <!-- 引入依赖模块 -->

        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>

        <!-- 添加此依赖以使用 EnableDiscoveryClient -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter</artifactId>
        </dependency>
    </dependencies>
```
子子工程 heima-leadnews-user 确认它正确引用了父模块
```xml
    <parent>
        <groupId>com.feed02</groupId>
        <artifactId>heima-leadnews-service</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <artifactId>heima-leadnews-user</artifactId>

```
标注完还是无效的话
- maven clean 
- 删除 .idea 文件夹和 *.iml 文件，然后重新导入项目

## com.sun.tools.javac.tree.JCTree

Lombok 与 JDK 21 的兼容性：在 JDK 21 中，com.sun.tools.javac.tree.JCTree$JCImport 类的 qualid 字段类型发生了变化。之前它的类型是 JCTree，而在 JDK 21 中变为 JCFieldAccess。这个变化导致了 Lombok 在尝试访问该字段时出现错误。

将 Lombok 更新到 1.18.30 或更高版本
