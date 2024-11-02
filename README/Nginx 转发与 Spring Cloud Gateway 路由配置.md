# Nginx 转发与 Spring Cloud Gateway 路由配置
1. Nginx 转发：请求先到达 Nginx，Nginx 通过配置转发到上游服务器（如 Spring Cloud Gateway）。
2. Spring Cloud Gateway 路由和服务发现：Gateway 通过 Nacos 进行服务发现和负载均衡，将请求转发到特定的微服务实例。

## 配置文件

- vagrant-ubuntu/Vagrantfile
- vagrant-ubuntu/docker-compose.yml
- local/nginx/conf/leadnews.conf/heima-leadnews-app.conf
- nacos/nacos_config_export_20241029235658.zip

- heima-leadnews-gateway/heima-leadnews-app-gateway/src/main/resources/bootstrap.yml
- heima-leadnews-service/heima-leadnews-user/src/main/resources/bootstrap.yml
- heima-leadnews-service/heima-leadnews-article/src/main/resources/bootstrap.yml

## Nginx 转发 
nginx.conf 中，配置了将以 /app/ 开头的请求转发到 heima-app-gateway 这个上游服务器，并通过 rewrite 指令去掉 /app 前缀后再进行代理转发

1. 上游服务器定义： upstream 

定义了 heima-app-gateway 的上游服务器，指向 192.168.182.218:51601。Nginx 会将所有代理请求发送到该地址。

2. 路径匹配和前缀去除

location /app/ 会匹配所有以 /app/ 开头的请求。

rewrite ^/app/(.*)$ /$1 break; 去掉 /app 前缀

比如：请求 http://localhost:8801/app/article/api/v1/article/load 被重写为 /article/api/v1/article/load。

proxy_pass http://heima-app-gateway; 将重写后的请求转发到 heima-app-gateway 

http://192.168.182.218:51601/article/api/v1/article/load

![image](https://github.com/feed981/e02/blob/hmtt-day2-05/README/images/wel4qw2v515efz4362.png)

## Spring Cloud Gateway 路由和服务发现
在 Nacos 中，uri: lb://leadnews-user 的确是通过 Spring Cloud 的负载均衡机制（即 lb:// 前缀）指向一个服务名为 leadnews-user 的服务实例。它的工作流程如下：

1. 服务发现：

spring.application.name 定义了应用的服务名称（在这个例子中为 leadnews-user）。

当 autoRegister: true 时，该服务会自动注册到 Nacos 服务注册中心。Nacos 使用 spring.application.name 作为服务标识，供其他服务发现和调用。

2. 服务调用：

在其他微服务中，通过 uri: lb://leadnews-user 表示调用服务名为 leadnews-user 的实例。

这种写法（lb://服务名）会借助 Spring Cloud 负载均衡，在 Nacos 中找到所有名为 leadnews-user 的可用实例，并根据负载均衡策略选择一个实例进行请求。

3. 配置文件读取：

spring.cloud.nacos.config 配置用于从 Nacos 配置中心读取配置文件。

file-extension: yml 表示要读取的配置文件是 YAML 格式。

Nacos 会根据 spring.application.name 自动匹配同名配置文件，如 leadnews-user.yml 或者 leadnews-user-dev.yml（具体环境可由其他配置决定）进行加载。

总结来说，通过这种配置方式，服务可以实现自动注册、服务发现以及从 Nacos 配置中心读取配置文件。

![image](https://github.com/feed981/e02/blob/hmtt-day2-05/README/images/15efz4wel4qw362v52.png)
