<<<<<<< HEAD
FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y curl

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
=======
FROM openjdk:17
ARG JAR_FILE=*.jar
COPY build/libs/snapmeal-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
>>>>>>> 2961f0b (refactor: kakao login)

