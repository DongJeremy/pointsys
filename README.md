* 环境准备 `docker-ce` `docker-compose`

* 修改 /usr/lib/systemd/system/docker.service 文件，加入如下内容：

```
-H tcp://0.0.0.0:2375  -H unix:///var/run/docker.sock
systemctl daemon-reload 
systemctl restart docker  
```

* 部署postgresql数据库

```
mkdir -p /opt/database && cd /opt/database/

cat << EOF > /opt/database/init.sql
CREATE DATABASE mydb;
CREATE USER admin WITH PASSWORD 'password';
alter user admin superuser createrole createdb replication;
GRANT ALL PRIVILEGES ON DATABASE mydb TO admin;
EOF

mkdir -p /opt/database/pgdata

cat << EOF > /opt/database/docker-compose.yml
version: '3.3'
services:
  postgres1:
    restart: always
    image: postgres:9.6.18-alpine
    privileged: true
    container_name: postgres
    ports:
      - 5432:5432
    environment:
      POSTGRES_PASSWORD: password
      PGDATA: /var/lib/postgresql/data/pgdata
    volumes:
      - /opt/database/pgdata:/var/lib/postgresql/data/pgdata
      - /opt/database/init.sql:/docker-entrypoint-initdb.d/init.sql
EOF

wget http://192.168.1.254:8090/common/postgres-9.6.18.tar.gz
docker load -i postgres-9.6.18.tar.gz
docker-compose up -d
```

4. 导入数据库：`point.backup`

5. 部署服务

```
mkdir -p /opt/nginx/{conf.d,log,cert}
cat << 'EOF' > /opt/nginx/conf.d/default.conf
server {
    listen      80;
    server_name localhost.local;

    rewrite ^(.*)$ https://$host$1 permanent;
    add_header Content-Security-Policy upgrade-insecure-requests;
}
EOF
cat << 'EOF' > /opt/nginx/conf.d/ssl.conf
server {
    listen      443 ssl;
    server_name localhost.local;

    ssl_certificate         cert/pointsys.pem;
    ssl_certificate_key     cert/pointsys.key;

    ssl_session_timeout 5m;
    ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
    ssl_ciphers ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:DHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:DHE-RSA-AES256-SHA:DHE-RSA-AES128-SHA;
    ssl_prefer_server_ciphers on;
    location / {
        proxy_pass http://192.168.1.94:8081;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header X-Forwarded-Port $server_port;
        proxy_set_header Host $host;
        proxy_set_header X-Forwarded-SSL on;
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

在 Linux 上创建这个镜像的容器，然后启动，执行如下命令即可：

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
      - 8082:8082
    environment:
      url: jdbc:postgresql://192.168.1.94:5432/mydb?characterEncoding=utf8&useSSL=true
    volumes: 
      - /opt/nginx/uploads:/uploads
  pointsys-web:
    restart: always
    image: javaboy/pointsys-web:1.0.0
    privileged: true
    container_name: pointsys-web
    ports:
      - 8081:8081
    environment:
      url: http://192.168.1.94:8082
  nginx:
    restart: always
    container_name: nginx
    image: nginx:alpine
    ports:
      - 80:80
      - 443:443
    volumes: 
      - /opt/nginx/conf.d:/etc/nginx/conf.d
      - /opt/nginx/log:/var/log/nginx
      - /opt/nginx/cert:/etc/nginx/cert
EOF
```

