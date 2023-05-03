# # FROM gradle:jdk11 AS builder
# # WORKDIR /workspace/app
# # COPY . /workspace/app
# # RUN --mount=type=cache,target=/root/.gradle ./gradlew clean build -x test
# # RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*-SNAPSHOT.jar)

# # FROM openjdk:11
# # VOLUME /tmp
# # ARG DEPENDENCY=/workspace/app/build/dependency
# # COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
# # COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
# # COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app
# # ENTRYPOINT ["java","-cp","app:app/lib/*","com.ssafy.mmart.MmartApplication"]


# FROM openjdk:gradle11
# VOLUME /tmp
# ARG DEPENDENCY=target/dependency
# COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
# COPY ${DEPENDENCY}/META-INF /app/META-INF
# COPY ${DEPENDENCY}/BOOT-INF/classes /app
# ENTRYPOINT ["java","-cp","app:app/lib/*","com.ssafy.mmart.MmartApplication"]

# FROM gradle:7.6.1-jdk17-alpine AS builder
# WORKDIR /builder/

# COPY build.gradle.kts settings.gradle.kts /builder/
# RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

# COPY src /builder/src

# RUN gradle clean bootJar


FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} app/app.jar
# COPY --from=builder /builder/build/libs/*-SNAPSHOT.jar app/app.jar
# COPY --from=builder /builder/build/resources app/resources
ENTRYPOINT ["java", "-jar", "app/app.jar"]
