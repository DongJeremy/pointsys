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
  nginx:
    restart: always
    container_name: nginx
    image: nginx:alpine
    ports:
      - 443:443
    volumes: 
      - /opt/nginx/conf.d:/etc/nginx/conf.d
      - /opt/nginx/log:/var/log/nginx
      - /opt/nginx/www:/var/www
      - /opt/nginx/cert:/etc/nginx/cert
EOF
```

生成配置文件

```
mkdir -p /opt/nginx/{conf.d,log,www,cert}
cat << 'EOF' > /opt/nginx/conf.d/default.conf
server {
    listen      443 ssl;
    server_name www.d05660.top;

    ssl_certificate         cert/localhost.pem;
    ssl_certificate_key     cert/localhost.key;

    ssl_session_timeout 5m;
    ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
    ssl_ciphers ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:DHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:DHE-RSA-AES256-SHA:DHE-RSA-AES128-SHA;
    ssl_prefer_server_ciphers on;
    location / {
        proxy_pass http://192.168.1.94:80;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header X-Forwarded-Port $server_port;
        proxy_set_header Host $host:$server_port;
        add_header Content-Security-Policy upgrade-insecure-requests;
    }
}
EOF
```

导出RootCA.crt

```
mkdir -p /opt/nginx/CA && cd /opt/nginx/CA

openssl req -x509 -nodes -new -sha256 -days 1024 -newkey rsa:2048 -keyout RootCA.key -out RootCA.pem -subj "/C=US/O = JLZM 2.0/CN=JLZM Trust Root CA"
openssl x509 -outform pem -in RootCA.pem -out RootCA.crt

cat << EOF > http.ext
keyUsage = nonRepudiation, digitalSignature, keyEncipherment
extendedKeyUsage = serverAuth, clientAuth
subjectAltName=@SubjectAlternativeName

[ SubjectAlternativeName ]
IP.1=192.168.1.94
DNS.1=localhost.local
EOF

openssl req -new -nodes -newkey rsa:2048 -keyout /opt/nginx/cert/localhost.key -out localhost.csr -subj "/C=CN/ST=JiLin/L=JiLin/O=JLZM/CN=localhost.local"
openssl x509 -req -in localhost.csr -CA RootCA.pem -CAkey RootCA.key -CAcreateserial -days 3650 -sha256 -extfile http.ext -out /opt/nginx/cert/localhost.pem -outform PEM
```



