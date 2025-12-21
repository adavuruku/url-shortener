# 1. Build + test stage

FROM maven:3.9.6-eclipse-temurin-22 AS build
LABEL authors="aabdulraheemsherif@gmail.com"

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package # compile, runs tests and package jar


#2. Runtime stage
FROM eclipse-temurin:22-jre
WORKDIR /app

COPY --from=build /app/target/url-shortener-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]
