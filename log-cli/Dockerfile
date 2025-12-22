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
COPY ../log-cli/src ./log-cli/src
COPY ../url-shortener/src ./url-shortener/src
COPY ../log-cli/samples ./log-cli/samples

# Build only (log-cli)
RUN mvn clean package -pl log-cli -am # compile, runs tests and package jar (log-cli)

# Stage 2: Runtime
FROM eclipse-temurin:22-jre
WORKDIR /app

COPY --from=build /app/log-cli/target/log-cli-0.0.1-SNAPSHOT.jar app.jar

# Mount CSV files via volume in docker-compose -> samples/access.csv
ENTRYPOINT ["java", "-jar", "app.jar"]