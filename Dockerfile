# Stage 1: Build the application
FROM gradle:8.5-jdk17 AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN chmod +x ./gradlew
RUN ./gradlew build -x test --no-daemon
# Stage 2: Run the application
FROM amazoncorretto:17-alpine
EXPOSE 9090
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]