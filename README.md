# Springfox 2.9.2  (建议升级 Swagger 到 Springfox 3.0.0 以上版本)

Spring Boot 和 Swagger 版本兼容性的问题上，特别是对于 Spring Boot 2.2.x 和 Springfox 2.9.2。在这种情况下，Swagger 的 swagger-ui.html 无法正确解析基础 URL，导致重复提示输入基础 URL

```xml
  <properties>
    <swagger.version>2.9.2</swagger.version>
  </properties>

    <dependency>
      <groupId>io.springfox</groupId>
      <artifactId>springfox-swagger2</artifactId>
      <version>${swagger.version}</version>
    </dependency>
    <dependency>
      <groupId>io.springfox</groupId>
      <artifactId>springfox-swagger-ui</artifactId>
      <version>${swagger.version}</version>
    </dependency>

```

升级 Swagger 到 Springfox 3.0.0 以上版本下面那些破问题就可以避免

https://github.com/springfox/springfox/issues/3001

1. 访问就跳这个
   
Unable to infer base url. This is common when using dynamic servlet registration or when the API is behind an API Gateway. The base url is the root of where all the swagger resources are served. For e.g. if the api is available at http://example.org/api/v2/api-docs then the base url is http://example.org/api/. Please enter the location manually: 

2. 如果加拦截器就会卡这个

Pre Handle: /error
Pre Handle: /null/swagger-resources/configuration/security
Pre Handle: /error
Pre Handle: /null/swagger-resources
Pre Handle: /error
Pre Handle: /null/swagger-resources/configuration/ui
Pre Handle: /error
Pre Handle: /null/swagger-resources/configuration/security
Pre Handle: /error

https://blog.csdn.net/qq_44717657/article/details/126968282

# Springfox 3.0.0

## 依赖

父工程
```xml
  <properties>
    <swagger.version>3.0.0</swagger.version>
  </properties>

           <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-boot-starter</artifactId>
                <version>${swagger.version}</version>
            </dependency>


```

子工程
在heima-leadnews-model和heima-leadnews-common模块中引入该依赖

```xml
           <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-boot-starter</artifactId>
            </dependency>

```

## 项目
只需要在heima-leadnews-common中进行配置即可，因为其他微服务工程都直接或间接依赖即可。
```java
package com.feed02.common.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
// 不再需要 @EnableSwagger2
public class SwaggerConfiguration {

   @Bean
   public Docket buildDocket() {
      return new Docket(DocumentationType.SWAGGER_2)
              .apiInfo(buildApiInfo())
              .select()
              // 要扫描的API(Controller)基础包
              .apis(RequestHandlerSelectors.basePackage("com.feed02"))
              .paths(PathSelectors.any())
              .build();
   }

   private ApiInfo buildApiInfo() {
      Contact contact = new Contact("黑马程序员", "", "");
      return new ApiInfoBuilder()
              .title("黑马头条-平台管理API文档")
              .description("黑马头条后台api")
              .contact(contact)
              .version("1.0.0")
              .build();
   }
}
```

在heima-leadnews-common模块中的resources目录中新增以下目录和文件

文件：resources/META-INF/Spring.factories

```java
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  com.feed02.common.exception.ExceptionCatch,\
  com.feed02.common.swagger.SwaggerConfiguration
```
将 SwaggerConfiguration 放在 heima-leadnews-common 模块中，其他模块也可以共享这个配置，而无需每个模块都手动引入它。只要模块在启动时包含 heima-leadnews-common 依赖，SwaggerConfiguration 配置就会自动生效。

```java
@RestController
@RequestMapping("/api/v1/login")
@Api(value = "app端用户登录", tags = "ap_user", description = "app端用户登录API")
public class ApUserLoginController {

    @Autowired
    private ApUserService apUserService;

    @PostMapping("/login_auth")
    @ApiOperation("用户登录")
    public ResponseResult login(@RequestBody LoginDto dto){
        return apUserService.login(dto);
    }
}
```

LoginDto

```java
@Data
public class LoginDto {

    /**
     * 手机号
     */
    @ApiModelProperty(value="手机号",required = true)
    private String phone;

    /**
     * 密码
     */
    @ApiModelProperty(value="密码",required = true)
    private String password;
}
```

启动user微服务访问 http://localhost:51801/swagger-ui/index.html

之前的 swagger2 路径为 http://localhost:51801/swagger-ui.html
