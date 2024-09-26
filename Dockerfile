FROM openjdk:17-jdk-alpine
LABEL authors="Jack Kayle"

ENV JAV_NAME=*.jar
ENV JAV_PATH=target/*.jar

COPY ${JAV_PATH} ${JAV_NAME}

ENTRYPOINT ["java","-jar","*.jar"]