# URL Shortener Service
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
./mvnw clean package → `# compile, runs tests and package jar`
```

#### 3. Run

```bash
java -jar target/url-shortener-0.0.1-SNAPSHOT.jar -Dspring-boot.run.profiles=local
```

#### OR just build and execute
```bash
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

```bash
curl -X POST http://localhost:9090/api/v1/urls \
  -H "Content-Type: application/json" \
  -d '{"longUrl":"https://example.com"}'
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

### ✅ Redis Cache

* Improves redirect latency significantly
* Cache misses fall back safely to DB

### ❌ No User defined URL Expiration

* System automatically compute url expiration (configured to 1hr)
* Simplicity over feature completeness
* Can be added with TTL

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
./mvn test
```

Code coverage is generated via **JaCoCo**.

---

## 📄 License

This project is provided for **educational and demonstration purposes**.

---
