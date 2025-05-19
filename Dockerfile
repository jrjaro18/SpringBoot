FROM gradle:8.14.0-jdk24-alpine AS build
LABEL authors="rohan"

COPY --chown=gradle:gradle . /app
WORKDIR /app

RUN gradle build -x test

FROM openjdk:24

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
EXPOSE 27017
EXPOSE 9092

ENTRYPOINT ["java", "-jar", "app.jar"]