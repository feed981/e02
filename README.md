# sql

leadnews_article.sql

添加article 的表

# app端文章列表 功能实现

需要在nacos中添加对应的配置

Data ID: leadnews-app-gateway

添加
```yaml
        # 文章管理
        - id: article
          uri: lb://leadnews-article
          predicates:
            - Path=/article/**
          filters:
            - StripPrefix= 1
```


通过Nacos 转发到各个微服务

localhost:51601/user/api/v1/article/load

localhost:51601/article/api/v1/article/load 对应 id



查询是带入的常量type ,tag
- heima-leadnews-common/src/main/java/com/feed02/common/constants/ArticleConstants.java

pojos
- heima-leadnews-model/src/main/java/com/feed02/model/article/pojos/ApArticle.java
- heima-leadnews-model/src/main/java/com/feed02/model/article/pojos/ApArticleConfig.java
- heima-leadnews-model/src/main/java/com/feed02/model/article/pojos/ApArticleContent.java

dtos
- heima-leadnews-model/src/main/java/com/feed02/model/article/dtos/ArticleHomeDto.java



编写mapper文件
- heima-leadnews-service/heima-leadnews-article/src/main/java/com/feed02/article/mapper/ApArticleMapper.java

文章表与文章配置表多表查询
- heima-leadnews-service/heima-leadnews-article/src/main/resources/mapper/ApArticleMapper.xml

配置
- heima-leadnews-service/heima-leadnews-article/src/main/resources/bootstrap.yml

log存放路径
- heima-leadnews-service/heima-leadnews-user/src/main/resources/logback.xml
- heima-leadnews-service/heima-leadnews-article/src/main/resources/logback.xml

编写业务层代码
- heima-leadnews-service/heima-leadnews-article/src/main/java/com/feed02/article/service/IApArticleService.java
- heima-leadnews-service/heima-leadnews-article/src/main/java/com/feed02/article/service/impl/ApArticleServiceImpl.java

编写控制器代码
- heima-leadnews-service/heima-leadnews-article/src/main/java/com/feed02/article/controller/vl/ArticleHomeController.java

## swagger测试或前后端联调测试

1. ok 网关(直接放行可以) localhost:51601/article/api/v1/article/load
2. ok 直接请求 localhost:51802//api/v1/article/load

## TODO:
没放行文章请求都是502，猜测是前端没带token

网关过滤器/article 先放行
- heima-leadnews-gateway/heima-leadnews-app-gateway/src/main/java/com/feed02/app/gateway/filter/AuthorizeFilter.java


