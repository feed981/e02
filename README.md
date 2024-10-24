# 使用 Vagrant 和 Docker 通过 docker-compose.yml 文件快速启动 Nacos

# Vagrant
https://developer.hashicorp.com/vagrant/install
```bash
# 初始化
D:\VirtualBox VMs\vagrant02>vagrant init centos/7

# 启动
D:\VirtualBox VMs\vagrant02>vagrant up

# 连线
D:\VirtualBox VMs\vagrant02>vagrant ssh
```
本地ping
```bash
C:\Users\feed06>ping 192.168.33.11

Ping 192.168.33.11 (使用 32 位元組的資料):
回覆自 192.168.33.11: 位元組=32 時間<1ms TTL=64
回覆自 192.168.33.11: 位元組=32 時間<1ms TTL=64
回覆自 192.168.33.11: 位元組=32 時間<1ms TTL=64
回覆自 192.168.33.11: 位元組=32 時間<1ms TTL=64

192.168.33.11 的 Ping 統計資料:
    封包: 已傳送 = 4，已收到 = 4, 已遺失 = 0 (0% 遺失)，
大約的來回時間 (毫秒):
    最小值 = 0ms，最大值 = 0ms，平均 = 0ms
```
# Docker

https://docs.docker.com/engine/install/centos

```bash
# 切换到root用户
[vagrant@localhost ~]$ sudo su

# 安装yum-utils，以便管理yum仓库
[root@localhost vagrant]# yum install -y yum-utils
Loaded plugins: fastestmirror
Determining fastest mirrors
Could not retrieve mirrorlist http://mirrorlist.centos.org/?release=7&arch=x86_64&repo=os&infra=vag error was
14: curl#6 - "Could not resolve host: mirrorlist.centos.org; Unknown error"


 One of the configured repositories failed (Unknown),
 and yum doesn't have enough cached data to continue. At this point the only
 safe thing yum can do is fail. There are a few ways to work "fix" this:
...
Cannot find a valid baseurl for repo: base/7/x86_64
```


## Cannot find a valid baseurl for repo: base/7/x86_64
可能原因 : yum源有问题，这边是换源链接

download "wget-1.14-15.el7_4.1.x86_64.rpm"
http://buildlogs-seed.centos.org/c7.1708.u/wget/20171026164447/1.14-15.el7_4.1.x86_64

```bash
# 上傳 vagrant
D:\VirtualBox VMs\vagrant02>vagrant upload "D:\VirtualBox VMs\vagrant02\wget-1.14-15.el7_4.1.x86_64.rpm" /home/vagrant/
Uploading D:\VirtualBox VMs\vagrant02\wget-1.14-15.el7_4.1.x86_64.rpm to /home/vagrant/
Upload has completed successfully!

  Source: D:\VirtualBox VMs\vagrant02\wget-1.14-15.el7_4.1.x86_64.rpm
  Destination: /home/vagrant/

D:\VirtualBox VMs\vagrant02>vagrant ssh
Last login: Tue Oct 22 07:17:32 2024 from 10.0.2.2

# 确认下刚刚上传的档案
[vagrant@localhost ~]$ ls
wget-1.14-15.el7_4.1.x86_64.rpm

# 切换到root用户
[vagrant@localhost ~]$ sudo su

# 安裝包
[root@localhost vagrant]#  rpm -ivh wget-1.14-15.el7_4.1.x86_64.rpm
Preparing...                          ################################# [100%]
Updating / installing...
   1:wget-1.14-15.el7_4.1             ################################# [100%]
   
# 备份
[root@localhost vagrant]# mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup

# 查看备份
[root@localhost vagrant]# cd /etc/yum.repos.d/

[root@localhost yum.repos.d]# ls
CentOS-Base.repo.backup  CentOS-Debuginfo.repo  CentOS-Media.repo    CentOS-Vault.repo
CentOS-CR.repo           CentOS-fasttrack.repo  CentOS-Sources.repo  CentOS-x86_64-kernel.repo


[root@localhost yum.repos.d]# cd

# yum源下载
[root@localhost ~]# wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
--2024-10-22 07:33:30--  http://mirrors.aliyun.com/repo/Centos-7.repo
Resolving mirrors.aliyun.com (mirrors.aliyun.com)... 163.181.140.237, 163.181.140.233, 163.181.140.231, ...
Connecting to mirrors.aliyun.com (mirrors.aliyun.com)|163.181.140.237|:80... connected.
HTTP request sent, awaiting response... 200 OK
Length: 2523 (2.5K) [application/octet-stream]
Saving to: ‘/etc/yum.repos.d/CentOS-Base.repo’

100%[=====================================================================================>] 2,523       --.-K/s   in 0s

2024-10-22 07:33:30 (207 MB/s) - ‘/etc/yum.repos.d/CentOS-Base.repo’ saved [2523/2523]

# 清理 YUM 缓存。执行该命令后，YUM 会删除所有已下载的包和头文件缓存，
# 以及任何已启用的插件数据和旧的内核数据。
# 执行这个命令可以释放磁盘空间并删除旧的缓存数据。
[root@localhost ~]# yum clean all
Loaded plugins: fastestmirror
Cleaning repos: base extras updates
Cleaning up list of fastest mirrors

# 重新生成 YUM 缓存。执行该命令后，YUM 会下载软件包清单和元数据，
# 并将其缓存到本地。这样做可以加快后续的软件包查询和安装速度。
# 执行 yum makecache 可以确保你使用的 YUM 源中的最新软件包信息
# 被正确地缓存，以便快速查找和安装软件包。
[root@localhost ~]# yum makecache
Loaded plugins: fastestmirror
Determining fastest mirrors
```
解决Cannot find a valid baseurl for repo: base/7/x86_64后

## 启动 docker

```bash
# 启动Docker服务
[root@localhost ~]# systemctl start docker

# 设置为开机自启
[root@localhost ~]# systemctl enable docker
Created symlink from /etc/systemd/system/multi-user.target.wants/docker.service to /usr/lib/systemd/system/docker.service.

# 验证Docker是否正确安装
[root@localhost ~]# docker run hello-world

[root@localhost ~]# docker ps -a
CONTAINER ID   IMAGE         COMMAND    CREATED         STATUS                     PORTS     NAMES
34e4c663689f   hello-world   "/hello"   6 seconds ago   Exited (0) 4 seconds ago   
```
## docker-compose

```bash
# 启动所有容器
[root@localhost docker-apps]# docker-compose up -d
bash: docker-compose: command not found

# 确认文件
[root@localhost docker-apps]# ls
docker-compose.yml  

# 检查 Docker-Compose 是否安装
[root@localhost ~]# docker-compose --version
bash: docker-compose: command not found

# 安装 Docker Compose
[root@localhost ~]# curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0
100 12.1M  100 12.1M    0     0  2182k      0  0:00:05  0:00:05 --:--:-- 2652k

# 设置执行权限
[root@localhost ~]#  chmod +x /usr/local/bin/docker-compose

# 检查你的 PATH 环境变量
[root@localhost ~]# echo $PATH
/sbin:/bin:/usr/sbin:/usr/bin

# 如果输出中没有 /usr/local/bin，你可以暂时将它添加到 PATH
[root@localhost ~]# export PATH=$PATH:/usr/local/bin

# 永久添加到 PATH
[root@localhost ~]# echo 'export PATH=$PATH:/usr/local/bin' >> ~/.bashrc

[root@localhost ~]# docker-compose --version
docker-compose version 1.29.2, build 5becea4c

[root@localhost ~]# cd ~/docker-apps

# 拉取镜像
[root@localhost docker-apps]# docker-compose pull
Pulling nacos         ... done

# 启动所有服务
[root@localhost docker-apps]# docker-compose up -d

# 启动特定服务
[root@localhost docker-apps]# docker-compose up -d nacos
Creating nacos         ... done

# 在修改完Docker Compose配置后，重新启动Docker服务以应用更改
[root@localhost docker-apps]# docker-compose down
Stopping nacos         ... done
Removing nacos             ... done
```
# 本地无法连上Nacos
http://192.168.33.11:8848/nacos/
1. 服务起了吗
```bash
[root@localhost docker-apps]# docker ps
CONTAINER ID   IMAGE                      COMMAND                  CREATED          STATUS          PORTS
                 NAMES
89382aea8484   nacos/nacos-server:1.4.1   "bin/docker-startup.…"   20 minutes ago   Up 20 minutes   0.0.0.0:8848->8848/tcp, :::8848->8848/tcp   nacos
```
2. virtual box NAT 要增加连线埠转送规则 ，nacos 主机IP 是192.168.33.11 8848，打开Vagrantfile 查看IP
```bash
config.vm.network "private_network", ip: "192.168.33.11"
```
3. 本地ping
```bash
C:\Users>ping 192.168.33.11

Ping 192.168.33.11 (使用 32 位元組的資料):
回覆自 192.168.33.11: 位元組=32 時間<1ms TTL=64
回覆自 192.168.33.11: 位元組=32 時間<1ms TTL=64
回覆自 192.168.33.11: 位元組=32 時間<1ms TTL=64
回覆自 192.168.33.11: 位元組=32 時間<1ms TTL=64

192.168.33.11 的 Ping 統計資料:
    封包: 已傳送 = 4，已收到 = 4, 已遺失 = 0 (0% 遺失)，
大約的來回時間 (毫秒):
    最小值 = 0ms，最大值 = 0ms，平均 = 0ms
```
4. 虚拟机防火墙、查看容器的网络设置
```bash
# 检查和修改防火墙规则，确保主机防火墙没有阻止 8848 端口
[root@localhost docker-apps]# firewall-cmd --zone=public --add-port=8848/tcp --permanent
FirewallD is not running
[root@localhost docker-apps]# firewall-cmd --zone=public --add-port=8848/tcp --permanent
FirewallD is not running
[root@localhost docker-apps]# firewall-cmd --reload
FirewallD is not running

# 查看容器的网络设置
[root@localhost docker-apps]# docker inspect nacos
```
