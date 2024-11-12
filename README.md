# 自媒体后台搭建

## 1. 基础环境和数据准备
执行资料中的leadnews-wemedia.sql脚本

- sql/ad_channel.sql
- sql/leadnews_wemedia.sql

拷贝wemedia文件夹到heima-leadnews-model模块下的com.heima.model

- heima-leadnews-model/src/main/java/com/feed02/model/wemedia/dtos/WmLoginDto.java
- heima-leadnews-model/src/main/java/com/feed02/model/wemedia/pojos/WmUser.java

## 3. 资料中找到heima-leadnews-wemedia.zip解压
拷贝到heima-leadnews-service工程下,并指定子模块

- heima-leadnews-service/heima-leadnews-wemedia/pom.xml
- heima-leadnews-service/heima-leadnews-wemedia/src/main/java/com/feed02/wemedia/WemediaApplication.java
- heima-leadnews-service/heima-leadnews-wemedia/src/main/java/com/feed02/wemedia/controller/v1/LoginController.java
- heima-leadnews-service/heima-leadnews-wemedia/src/main/java/com/feed02/wemedia/mapper/WmUserMapper.java
- heima-leadnews-service/heima-leadnews-wemedia/src/main/java/com/feed02/wemedia/service/WmUserService.java
- heima-leadnews-service/heima-leadnews-wemedia/src/main/java/com/feed02/wemedia/service/impl/WmUserServiceImpl.java
- heima-leadnews-service/heima-leadnews-wemedia/src/main/resources/bootstrap.yml
- heima-leadnews-service/heima-leadnews-wemedia/src/main/resources/logback.xml

添加对应的nacos配置
- nacos/nacos.sql

## 4. 资料中找到heima-leadnews-wemedia-gateway.zip解压

拷贝到heima-leadnews-gateway工程下,并指定子模块

- heima-leadnews-gateway/heima-leadnews-wemedia-gateway/pom.xml
- heima-leadnews-gateway/heima-leadnews-wemedia-gateway/src/main/java/com/feed02/wemedia/gateway/WemediaGatewayAplication.java
- heima-leadnews-gateway/heima-leadnews-wemedia-gateway/src/main/java/com/feed02/wemedia/gateway/filter/AuthorizeFilter.java
- heima-leadnews-gateway/heima-leadnews-wemedia-gateway/src/main/java/com/feed02/wemedia/gateway/util/AppJwtUtil.java
- heima-leadnews-gateway/heima-leadnews-wemedia-gateway/src/main/resources/bootstrap.yml

添加对应的nacos配置
- nacos/nacos.sql

# 前台搭建

## 1. 资料中找到wemedia-web.zip解压
- local/nginx/html/wemedia-web.zip

## 2. 在nginx中leadnews.conf目录中新增heima-leadnews-wemedia.conf文件
- local/nginx/conf/nginx.conf

## 3. 启动nginx，启动自媒体微服务和对应网关

## 4. 联调测试登录功能

Caused by: com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Access denied for user 'hmtt_user'@'%' to database 'leadnews_wemdeia'

检查1. nacos leadnews-wemedia 看起来数据库信息都正确
检查2. 本地Postman发一笔请求 http://localhost:51803/login/in

```json
{
  "name": "admin",
  "password": "admin"
}
```
检查3. 角色权限

```shell
sudo docker exec -it mysql mysql -u root -p
mysql> SHOW GRANTS FOR 'hmtt_user'@'%';
mysql> SELECT * FROM information_schema.USER_PRIVILEGES WHERE GRANTEE = "'hmtt_user'@'%'";
```

关SpringBoot后台服务再重启就能正常访问了
