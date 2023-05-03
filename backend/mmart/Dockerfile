FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} app/app.jar
ENTRYPOINT ["java", "-jar", "app/app.jar"]
