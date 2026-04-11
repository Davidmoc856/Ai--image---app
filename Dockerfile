FROM gradle:8.5-jdk17 AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN if [ -f gradlew ]; then chmod +x ./gradlew; else echo "gradlew not found"; fi
RUN ./gradlew build -x test --no-daemon
FROM amazoncorretto:17-alpine
EXPOSE 9090
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]