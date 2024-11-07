vagrantfile 端口映射

```ruby
  config.vm.network "forwarded_port", guest: 9000, host: 9000 # minio API的端口
  config.vm.network "forwarded_port", guest: 9001, host: 9001 # minio 控制台的端口
```

docker-compose.yml

```yml
  minio:
    image: minio/minio:latest
    container_name: minio
    ports:
      - "9000:9000"  # API 端口
      - "9001:9001"  # 控制台端口
    volumes:
      - minio_data:/data # 存储数据的卷
    environment:
      MINIO_ROOT_USER: "minioadmin"       # 访问的用户名
      MINIO_ROOT_PASSWORD: "minioadmin"   # 访问的密码（生产环境应修改）
    command: server /data --console-address ":9001" # 设置控制台端口
```

