FROM openjdk:11.0.6-jre
VOLUME /tmp
ENV url=""
ADD target/pointsys-web-1.0.0.jar app.jar
ENTRYPOINT ["sh","-c","java -jar /app.jar --https.httpclient.url=$url --proxy.target_url=$url/api"]