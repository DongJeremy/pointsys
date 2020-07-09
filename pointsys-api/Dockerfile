FROM openjdk:11.0.6-jre
VOLUME /tmp
ENV url=""
ADD target/pointsys-api-1.0.0.jar app.jar
ENTRYPOINT ["sh","-c","java -jar /app.jar --spring.datasource.url=$url"]