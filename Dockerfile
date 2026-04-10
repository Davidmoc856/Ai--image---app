FROM gradle:8.5-jdk17 AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN ls -la gradlew && chmod +x ./gradlew && ./gradlew --version
RUN ./gradlew build -x test --no-daemon
FROM amazoncorretto:17-alpine
EXPOSE 9090
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]