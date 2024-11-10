FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/basic.jar /app/basic.jar

CMD ["java", "-jar", "/app/basic.jar"]