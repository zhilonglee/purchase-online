FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD ./target/spring-cloud-microservice-monitor.jar /app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 8040