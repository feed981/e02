# knife4j-demo

快速集成Knife4j

Spring Boot版本2.2.1.RELEASE、Knife4j版本2.0.9

在maven项目的pom.xml中引入Knife4j的依赖包
```xml 
    <knife4j.version>2.0.9</knife4j.version>

    <dependency>
      <groupId>com.github.xiaoymin</groupId>
      <artifactId>knife4j-spring-boot-starter</artifactId>
      <version>${knife4j.version}</version>
    </dependency>
```

创建Swagger配置依赖

```java 
package org.example.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket buildDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInfo())
                .select()
                // 要扫描的API(Controller)基础包
                .apis(RequestHandlerSelectors.basePackage("org.example"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo buildApiInfo() {
        Contact contact = new Contact("feed01","","");
        return new ApiInfoBuilder()
                .title("feed01-平台管理API文档")
                .description("feed01-后台api")
                .contact(contact)
                .version("1.0.0").build();
    }
}
```

控制器
```java 
package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Api(tags = "首页模块")
public class IndexController {

    @ApiImplicitParam(name = "name",value = "姓名",required = true)
    @ApiOperation(value = "向客人问好")
    @GetMapping("/sayHi")
    public ResponseEntity<String> sayHi(@RequestParam(value = "name")String name){
        return ResponseEntity.ok("Hi:"+name);
    }
}
```
入口
```java 
package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

```


http://localhost:51801/doc.html

## Knife4j文档请求异常

加上这个依赖
```xml 
    <swagger.version>3.0.0</swagger.version>

    <dependency>
      <groupId>io.springfox</groupId>
      <artifactId>springfox-boot-starter</artifactId>
      <version>${swagger.version}</version>
    </dependency>
```

# 功能实现


## 引入knife4j的依赖

父项目
```xml

        <knife4j.version>2.0.9</knife4j.version>
            <dependency>
                <groupId>com.github.xiaoymin</groupId>
                <artifactId>knife4j-spring-boot-starter</artifactId>
                <version>${knife4j.version}</version>
            </dependency>
```

子项目
在heima-leadnews-common、heima-leadnews-model模块中的pom.xml文件中

可能有用到的都要加，只加 heima-leadnews-common 网站没显示
```xml
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-boot-starter</artifactId>
        </dependency>

<!--        <dependency>-->
<!--            <groupId>io.springfox</groupId>-->
<!--            <artifactId>springfox-swagger-ui</artifactId>-->
<!--        </dependency>-->

        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-spring-boot-starter</artifactId>
        </dependency>
```
## 自动装配
在heima-leadnews-common模块中的resources目录中新增以下目录和文件

文件：resources/META-INF/Spring.factories
```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  com.feed02.common.swagger.SwaggerConfiguration
```

http://localhost:51801/doc.html
