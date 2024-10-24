FROM openjdk:17-jdk-alpine
LABEL authors="Jack Kayle"

ENV JAV_NAME=nozma.jar
ENV JAV_PATH=target/nozma.jar

COPY ${JAV_PATH} ${JAV_NAME}

EXPOSE 8080

ENTRYPOINT ["java","-jar","nozma.jar"]