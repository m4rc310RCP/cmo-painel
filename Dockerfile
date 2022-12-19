# syntax=docker/dockerfile:1
FROM openjdk:8-jdk-alpine

#FROM maven:3.6.0-jdk-11-slim 
#FROM openjdk:1.8-jdk-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve

COPY src ./src
#ARG JAR_FILE=target/*.jar
#COPY target/cmo-panel-spring-0.0.1-SNAPSHOT.jar  app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

#CMD ["./mvnw", "spring-boot:build-image"]
#CMD ["./mvnw", "spring-boot:run"]
CMD "./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=springio/gs-spring-boot-docker"
#CMD ["java","-jar","/home/smartling/flagship/repo-connector-1.5.4/repo-connector-1.5.4.jar -start&"]
