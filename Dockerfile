FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y curl

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

