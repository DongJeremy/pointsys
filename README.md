修改 /usr/lib/systemd/system/docker.service 文件，加入如下内容：

```
-H tcp://0.0.0.0:2375  -H unix:///var/run/docker.sock
systemctl daemon-reload 
systemctl restart docker  
```

在 Linux 上像创建这个镜像的容器，然后启动，执行如下命令即可：

```
docker run -d --name pointsys-api -p 8080:8080 javaboy/pointsys-api:1.0.0
docker run -d --name pointsys-web -e url="http://192.168.1.94:8080" -p 80:80 javaboy/pointsys-web:1.0.0
```

```
cat << EOF > docker-compose.yml
version: '3.3'
services:
  pointsys-api:
    restart: always
    privileged: true
    container_name: pointsys-api
    image: javaboy/pointsys-api:1.0.0
    ports:
      - 8080:8080
    environment:
      url: jdbc:postgresql://192.168.1.94:5432/mydb?characterEncoding=utf8&useSSL=true
  pointsys-web:
    restart: always
    image: javaboy/pointsys-web:1.0.0
    privileged: true
    container_name: pointsys-web
    ports:
      - 80:80
    environment:
      url: http://192.168.1.94:8080
EOF
```