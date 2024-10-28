# vagrant-ubuntu
vagrantfile

在文件写端口映射等同virtualbox 添加转送规则


```ruby
# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure("2") do |config|
  
  config.vm.box = "ubuntu/bionic64"

  config.vm.network "private_network", ip: "192.168.33.11"

  # 端口映射
  config.vm.network "forwarded_port", guest: 80, host: 8801
  config.vm.network "forwarded_port", guest: 8848, host: 8848

  # 添加一个共享文件夹配置，将 Windows 的本地路径映射到虚拟机内的路径
  config.vm.synced_folder "D:/local/nginx", "/vagrant/nginx"

  config.vm.provider "virtualbox" do |vb|
    # Display the VirtualBox GUI when booting the machine
    vb.gui = true
  
    # Customize the amount of memory on the VM:
    vb.memory = "2048" # 将内存大小设置为 2GB，可以根据需要增加
  end

end

```

## config.vm.synced_folder "D:/local/nginx", "/vagrant/nginx" 
添加一个共享文件夹配置，将 Windows 的本地路径映射到虚拟机内的路径

本地路径
```text
D:\local\nginx\conf\nginx.conf
D:\local\nginx\html\index.html
```

在 Vagrantfile 中，添加一个共享文件夹配置，将 Windows 的本地路径映射到虚拟机内的路径
设置共享文件夹
```ruby
config.vm.synced_folder "D:/local/nginx", "/vagrant/nginx"
```
本地
``` 
# D:\local\nginx\conf\nginx.conf
server {
    listen 80;
    server_name localhost;

    location / {
        root /vagrant/nginx/html;
        index index.html index.htm;
    }
}
# D:\local\nginx\html\index.html
<h1>hello nginx!</h1>
```

在虚拟机中的 Nginx 配置文件（/etc/nginx/nginx.conf）
```
http {
    include /vagrant/nginx/conf/*.conf;
}
```

测试并重启 Nginx： 修改完成后，在虚拟机内测试配置并重启 Nginx：
```bash
sudo nginx -t  # 检查配置是否正确
sudo systemctl restart nginx  # 重启 Nginx 服务
```

nginx: [emerg] "http" directive is not allowed here 错误表示 http 指令在当前的配置文件结构中使用位置不正确。在 Nginx 中，http 块只能出现在主配置文件（通常是 /etc/nginx/nginx.conf）的顶层，而不是在子配置文件中。

```
# D:\local\nginx\conf\nginx.conf
# 如果子配置文件有http 记得拿掉
#http {
#    include /vagrant/nginx/conf/*.conf;
#}

server {
    listen 80;
    server_name localhost;

    location / {
        root /vagrant/nginx/html;
        index index.html index.htm;
    }
}
```

## 相对路径

本地档案同步到虚拟机

config.vm.synced_folder "D:/local/nginx", "/vagrant/nginx" 

| Column 1 | 本地主机 | 虚拟机 |
| -------- | -------- | -------- |
| conf     | D:\local\nginx\conf\leadnews.conf\heima-leadnews-app.conf     | /vagrant/nginx/conf/leadnews.conf/*.conf     |
| html     |  D:\local\nginx\html\app-web\index.html     | /vagrant/nginx/html/app-web     |


虚拟机 /etc/nginx/nginx.conf

```
http {
    include  /vagrant/nginx/conf/leadnews.conf/*.conf;
}
```

/leadnews.conf/*.conf
```
server {
    listen 80;
    server_name localhost;

    location / {
        root /vagrant/nginx/html/app-web;
        index index.html index.htm;
    }
}

```

## fatal error: out of memory allocating heap arena map 
```bash
sudo docker compose up -d nacos
```

错误信息表示系统内存不足，无法为 Nacos 容器分配所需的内存。这个错误通常在虚拟机或容器的内存资源有限时出现。要解决这个问题，可以尝试：
增加虚拟机内存，修改 Vagrantfile 来分配更多内存给虚拟机。例如：
```ruby
config.vm.provider "virtualbox" do |vb|
  vb.memory = "2048" # 将内存大小设置为 2GB，可以根据需要增加
end
```

# 请求流向

http://localhost:8801/app/user/api/v1/login/login_auth/
1. 请求到达 Nginx 的 /app/user/api/v1/login/login_auth/
2. Nginx 将请求转发到 http://localhost:51601/user/api/v1/login/login_auth/
3. leadnews-app-gateway 应该根据 Nacos 的服务发现，进一步将请求转发到 leadnews-user 服务（端口 51801）

# 配置分析

## Nginx 配置

```
upstream heima-app-gateway {
    server localhost:51601;
    #server 127.0.0.1:51601;
    #server 192.168.33.11:51601;     # 使用虚拟机的实际 IP 地址
}

server {
    listen 80;
    server_name localhost;

    location / {
        root /vagrant/nginx/html/app-web;
        index index.html index.htm;
    }

  location /app/ {
		rewrite ^/app/(.*)$ /$1 break;  # 去掉 /app 前缀
		proxy_pass http://heima-app-gateway;
        proxy_set_header HOST $host;  
        proxy_pass_request_body on;  
        proxy_pass_request_headers on;  
        proxy_set_header X-Real-IP $remote_addr;   
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;  
    }
}
```


在虚拟机的 Nginx 配置中，将 localhost 或 127.0.0.1 替换为主机的实际 IP 地址


```
upstream heima-app-gateway {
    server <主机的实际 IP>:51601;
}
```
```bash
sudo systemctl restart nginx
curl http://<主机的实际 IP>:51601/user/api/v1/login/login_auth/
```

## Spring Boot 配置

leadnews-app-gateway (端口 51601)
```yml
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




leadnews-user (端口 51801)

```yml
server:
  port: 51801
spring:
  application:
    name: leadnews-user
  cloud:
    nacos:
      discovery:
        autoRegister: true 
        server-addr: 192.168.33.11:8848
      config:
        server-addr: 192.168.33.11:8848
        file-extension: yml
```

## Nacos 配置

leadnews-app-gateway  (端口 51601)
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
1. 配置中的 uri: lb://leadnews-user 表示网关会将请求负载均衡（lb://）转发到名为 leadnews-user 的服务实例。
2. StripPrefix=1 表示会移除路径中的第一个路径前缀 /user
leadnews-user

```yaml
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

## 监听所有网络接口
在 leadnews-app-gateway (端口 51601)的 bootstrap.yml 文件中，添加以下配置，让服务监听所有网络接口：
```yaml
server:
  address: 0.0.0.0
  port: 51601
```


# 确认本地的服务监听

可以直接访问 http://localhost:51601/user/api/v1/login/login_auth/ 并得到正常响应，
但通过 Nginx 访问 http://localhost:8801/app/user/api/v1/login/login_auth/ 
和 http://localhost:80/app/user/api/v1/login/login_auth/ 时都返回了502错误。这表明 Nginx 无法成功连接到上游服务。

虽然你的服务在直接访问时正常，但为了确保 Nginx 能够连接到上游服务，仍然确认服务是否在监听

```bash 
netstat -ano | findstr ":51601"
```

```bash
netstat -tuln | grep 51601
'grep' 不是內部或外部命令、可執行的程式或批次檔。

netstat -ano | findstr ":51601"
  TCP    0.0.0.0:51601          0.0.0.0:0              LISTENING       5920
  TCP    [::]:51601             [::]:0                 LISTENING       5920
  TCP    [::1]:51601            [::1]:52164            ESTABLISHED     5920
  TCP    [::1]:52164            [::1]:51601            ESTABLISHED     17648

tasklist | findstr "5920"
java.exe                      5920 Console                    2    159,856 K
```

从 netstat 的输出来看，51601 端口已被 java.exe 进程（PID 5920）占用，并且状态显示为 LISTENING，这意味着该端口正在由 java 应用监听中。在您的环境中，这通常表示 Spring Boot 服务（leadnews-app-gateway）已成功启动并在 51601 端口上监听。

# 解决502

1. 端口 51601 加上服务监听所有网络接口 address: 0.0.0.0
2. Nginx 配置中，将 localhost 或 127.0.0.1 替换为主机的实际 IP 地址

# 其他

重启时nacos config 没了 访问8848时就捞不到配置
```
***************************
APPLICATION FAILED TO START
***************************

Description:

Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.

Reason: Failed to determine a suitable driver class


Action:

Consider the following:
	If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.
	If you have database settings to be loaded from a particular profile you may need to activate it (no profiles are currently active).

Disconnected from the target VM, address: '127.0.0.1:51479', transport: 'socket'
```

# 再次来看下请求流向

0. 起Nginx后 引用教程现成的前端画面(详见上面的"共享文件夹配置")我门点击登陆就有送这个请求 http://localhost:8801/app/user/api/v1/login/login_auth/
1. 请求到达 Nginx 的 /app/user/api/v1/login/login_auth/
2. 从Nginx配置中看到 /app/ 会有这个 rewrite ^/app/(.*)$ /$1 break;  # 去掉 /app 前缀
3. Nginx 将请求转发到 http://localhost:51601/user/api/v1/login/login_auth/
4. 可以看到Nacos配置中 uri: lb://leadnews-user  转发到名为 leadnews-user 的服务实例 有filter: StripPrefix=1 表示会移除路径中的第一个路径前缀 /user
5. 根据 Nacos 的服务发现，进一步将请求转发到 leadnews-user 服务（端口 51801） http://localhost:51801/api/v1/login/login_auth/




