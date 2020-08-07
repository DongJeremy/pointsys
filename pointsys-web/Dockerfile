FROM woahbase/alpine-openjdk11:latest
VOLUME /tmp
ENV url=""
ADD target/pointsys-web-1.0.0.jar app.jar
ENTRYPOINT ["sh","-c","java -jar /app.jar --https.httpclient.url=$url --proxy.target_url=$url/api"]