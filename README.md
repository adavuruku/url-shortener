# Senior Java Role Assessment

## Project 1: URL Shortener Service

```bash
Sherif Abdulraheem A.
https://www.linkedin.com/in/abdulraheem-sherif-adavuruku-729a77107
```
## 📌 Project Goal

The goal of this project is to build a **scalable, production-ready URL shortening service** that:

* Converts long URLs into short, unique identifiers
* Redirects short URLs to their original destinations
* Supports high read traffic efficiently
* Ensures data consistency and reliability
* Is easy to run locally and in containers
* Rate limit to control the use of create /put endpoints

The service is designed with **Spring Boot**, **PostgreSQL**, **Redis**, **Flyway**, and **Docker**, following modern backend best practices.

---

## 🏗️ Tech Stack

* **Java 22 (LTS)**
* **Spring Boot 3.5.9**
* **PostgreSQL** – persistent storage
* **Redis** – caching & rate limiting
* **Flyway** – database migrations
* **bucket4j** – for rate limiting
* **Docker & Docker Compose**
* **Maven**

---

## 🚀 Build & Run Instructions

### ✅ Prerequisites

* Java **22**
* Maven **3.9+**
* Docker & Docker Compose

Verify versions:

```bash
java -version
mvn -version
docker --version
```

---

### 🟢 Run with Docker (Recommended)

This is the **simplest and preferred** way.

```bash
docker compose down -v
docker compose build --no-cache
docker compose up
```

Services started:

* API → `http://localhost:9090`
* PostgreSQL → `localhost:5432`
* Redis → `localhost:6379`


**Note:** docker is run using **prod** profile **(application-prod.xml)**

---

### 🟡 Run Locally (Without Docker)

#### 1. Start dependencies

* PostgreSQL running on `localhost:5432`
* Redis running on `localhost:6379`

#### 2. Build

```bash
cd url-shortener
./mvnw clean package → `# compile, runs tests and package jar`
```

#### 3. Run

```bash
java -jar target/url-shortener-0.0.1-SNAPSHOT.jar -Dspring-boot.run.profiles=local
```

#### OR just build and execute
```bash
//1. change to project directory 
cd url-shortener

//2. execute the command to run jar
./mvnw spring-boot:run -Dspring-boot.run.profiles=local → `# compile, package jar and execute the jar using local profile`
```

---

## ⚙️ Configuration & Profiles

| Profile | Usage               |
| ------- | ------------------- |
| `local` | Local development   |
| `prod`  | Docker / production |

Docker uses:

```bash
SPRING_PROFILES_ACTIVE=prod

PORT = 9090
```

---

### 📡 **OpenAPI / Swagger docs**

```bash
http://localhost:9090/swagger-ui/index.html
```
---
## 📡 API Documentation

### 🔹 Create Short URL

**POST** `/api/v1/urls`

**Request**

```json
{
  "longUrl": "https://example.com/some/very/long/url"
}
```

**Response**

```json
{
  "shortUrl": "http://localhost:9090/r/abc123",
  "code": "abc123"
}
```

---

### 🔹 Redirect to Original URL

**GET** `/r/{code}`

Example:

```http
GET /r/abc123
```

➡️ Responds with **HTTP 302 Redirect**

---

### 🔹 Health Check

**GET** `/actuator/health`

**Response**

```json
{
  "status": "UP"
}
```
---

### 🔹 Redirect total metric Check

**GET** `/actuator/metrics/shortener_redirect_total`

**Response**

```json
{
  "name": "shortener_redirect_total",
  "measurements": [
    {
      "statistic": "COUNT",
      "value": 1.0
    }
  ],
  "availableTags": []
}
```
---

## 🖥️ CLI Usage (Optional)

You can test via `curl`:
#### Create
```bash
curl -X POST http://localhost:9090/api/v1/urls \
  -H "Content-Type: application/json" \
  -d '{"longUrl":"https://example.com"}'
```

---
#### Redirect by code (wer20)
```bash
curl -X GET http://localhost:9090/r/wer20 \
  -H "Content-Type: application/json"
```

---

#### Metadata by code (wer20)
```bash
curl -X GET http://localhost:9090/api/urls/wer20 \
  -H "Content-Type: application/json"
```

---

#### Redirect total metric test
```bash
curl -X GET http://localhost:9090/actuator/metrics/shortener_redirect_total \
  -H "Content-Type: application/json"
```

---


## 🧠 Assumptions

* Short URL codes are **globally unique**
* Redirects are **read-heavy** compared to writes
* PostgreSQL is the source of truth
* Redis cache entries may be evicted at any time (default config ttl 10 minutes)
* URL expiration is **auto-configured** (60 minutes (1 hr) expiration time)
* Rate limit for create endpoint is set to five (5) threshold capacity.
* Maximum retry on create endpoint (scope) for code collision exception.
* Authentication is **out of scope**

---

## ⚖️ Trade-offs & Design Decisions

### ✅ PostgreSQL vs NoSQL

* Chosen for **strong consistency** and easy migrations
* Slightly higher write latency accepted
* Suitable for **structured data and rellationship**

```bash
  datasource:
    url: jdbc:postgresql://localhost:5432/url_shortener
    username: postgres
    password: sherif
    driver-class-name: org.postgresql.Driver
    hikari:
      jdbc-url:  ${spring.datasource.url}
      pool-name: url-shortener-pool
      connection-timeout: 30000
      pool-size: 10
      idle-timeout: 30
      max-lifetime: 1800000
      username: ${spring.datasource.username}
      password: ${spring.datasource.password}
      leak-detection-threshold: 60000
      minimum-idle: 10
      schema: public
      
    jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: true
    database-platform: org.hibernate.dialect.PostgreSQLDialect

  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    url:  ${spring.datasource.url}
    user: ${spring.datasource.username}
    password: ${spring.datasource.password}
    schemas: public
    out-of-order: true
```

### ✅ Redis Cache

* Improves redirect latency significantly
* Cache misses fall back safely to DB
```bash
    data:
    redis:
      host: localhost
      port: 6379
      timeout: 2s

  cache:
    type: redis
    redis:
      time-to-live: 10m
      cache-null-values: false
```


### ✅ Rate Limit

* Improves redirect latency significantly
* Cache misses fall back safely to DB
```bash
app-config:
  short-url:
    expiry-minutes: 60
    max-create-retry: 5
  thread-config:
    core-pool-size: 5
    max-pool-size: 10
    queue-capacity: 100
  cache-config:
    ttl-minutes: 10
  rate-limit-config:
    limit-period-minutes: 1
    limit-token: 10
    limit-capacity: 10
```

### ❌ No User defined URL Expiration

* System automatically compute url expiration (configured to 1hr)
* Simplicity over feature completeness

### ❌ No Authentication

* Keeps API simple
* Assumed to be internal or fronted by a gateway

### ❌ No Custom Domains

* Out of scope for MVP
* Would require DNS + domain ownership verification

---

## 🛠️ Future Improvements

* User defined Custom aliases/short codes
* User defined URL expiration time
* Analytics (click counts per geo, user or referrer)
* Rate limiting per user (if authentication is present)
* CRON Job to clean expired code.
* Authentication & API keys

---

## 🧪 Testing

Run all tests: (unit and integration test)

```bash
//1. cd url-shortener

//2. execute the command to run test
./mvn test

```

Code coverage is generated via **JaCoCo**.

---

## 📄 License

This project is provided for **educational and demonstration purposes**.

---


## Project 2: Log Cli
```bash
Sherif Abdulraheem A.
https://www.linkedin.com/in/abdulraheem-sherif-adavuruku-729a77107
```

## 📌 Project Goal

The goal of this project is to build a **scalable, production-ready CSV CLI log passer java application ** that:

* CLI application that reads a csv file.
* Transform the csv file.
* Generate statistic and percentile report.
* Ensures data reliability and efficiency by running parallel stream

The service is designed with **Java**, and **Docker**, following modern backend best practices.


## 🏗️ Tech Stack

* **Java 22 (LTS)**
* **Docker & Docker Compose**
* **Maven**
* **Maven shades**

---

## 🚀 Build & Run Instructions

### ✅ Prerequisites

* Java **22**
* Maven **3.9+**
* Docker & Docker Compose

Verify versions:

```bash
java -version
mvn -version
docker --version
```

---

### 🟢 Run with Docker (Recommended)

This is the **simplest and preferred** way.

```bash
docker compose down -v
docker compose build --no-cache
docker compose up

//. In container cli run.
docker run log-cli --file /app/samples/access.csv --topN 5
```
---

## 🟡 Run Locally (Without Docker)

#### 1. Build

```bash
cd log-cli
./mvnw clean package → `# compile, runs tests and package jar`
```

#### 3. Run

```bash
java -jar target/log-cli-0.0.1-SNAPSHOT.jar --file samples/access.csv --topN 5
```
```bash
## sample output on cli
 C:\Users\USER\Documents\GitHub\url-shortener\log-cli> java -jar target/log-cli-0.0.1-SNAPSHOT.jar --file samples/access.csv --topN 5
Top 5 endpoints since 1970-01-01T00:00:00Z
/profile -> 189 hits
/home -> 184 hits
/api/data -> 177 hits
/login -> 166 hits
/api/r/url -> 158 hits

Response time percentiles:
p10 = 37 ms
p20 = 44 ms
p30 = 53 ms
p40 = 60 ms
p50 = 68 ms
p60 = 73 ms
p70 = 80 ms
p80 = 87 ms
p90 = 94 ms
p100 = 210 ms
```

#### Sample data in samples/access.csv
```bash
timestamp,endpoint,responseTime,status
2025-12-20T08:15:30Z,/login,120,200
2025-12-20T08:16:05Z,/home,50,200
2025-12-20T08:17:10Z,/api/data,200,500
2025-12-20T08:17:45Z,/login,110,200
2025-12-20T08:18:00Z,/profile,80,200
2025-12-20T08:18:30Z,/api/data,190,200
```
---

## ⚖️ Trade-offs & Design Decisions

### ✅ No CSV Parser Library

* Pure java code for extracting, transforming (parser) since we're certain of data.

## 🛠️ Future Improvements

* Integrate csv library parser that can handle complex and huge amount of data.
* DLQ (Dead letter que), to store and report data that fail to pass transformation.

## 🧪 Testing

Run all tests: (unit)

```bash
//1. cd log-cli

//2. execute the command to run test
./mvn test

```

---

## 📄 License

This project is provided for **educational and demonstration purposes**.

---