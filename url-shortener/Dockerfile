# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-22 AS build
LABEL authors="aabdulraheemsherif@gmail.com"

WORKDIR /app

# Copy parent POM + module POMs
COPY ../../pom.xml ./
COPY ../url-shortener/pom.xml ./url-shortener/pom.xml
COPY ../log-cli/pom.xml ./log-cli/pom.xml

# Download dependencies offline
RUN mvn dependency:go-offline

# Copy source code
COPY ../url-shortener/src ./url-shortener/src
COPY ../log-cli/src ./log-cli/src

# Build only ( URL shortener)
RUN mvn clean package -pl url-shortener -am # compile, runs tests and package jar (url-shortener)


#2. Runtime stage
FROM eclipse-temurin:22-jre
WORKDIR /app

COPY --from=build /app/url-shortener/target/url-shortener-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]
