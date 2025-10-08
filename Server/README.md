# ZoodBite Server — Backend for Food Delivery System

This module is the server-side backend of **ZoodBite**, a role-based food delivery application. It provides secure RESTful APIs for managing users, restaurants, menus, orders, and deliveries.

## ⚙️ Technologies

- **Language:** Java 23 (Amazon Corretto)
- **Architecture:** MVC, DAO, Service, DTO, Singleton
- **Build Tool:** Maven (supports Maven Wrapper)
- **Persistence:** Hibernate (JPA 3.1)
- **Database:** MySQL (Docker-ready)
- **Testing:** JUnit 5, Mockito, TestContainers
- **API Docs:** Swagger (OpenAPI 3.1)

## 📁 Structure

```
Server/src/main/java
├── config/             # API configuration (JAX-RS)
├── controller/         # API endpoints (JAX-RS)
├── dao/                # DB operations (Hibernate/JDBC)
├── dto/                # Data Transfer Objects
├── model/              # JPA entities
├── service/            # Business logic
├── util/               # Helpers, validators, token utils
└── Main.java           # API runner (Jetty + Jersey)
```

## 🔐 Features

- ✅ Role-based access (`customer`, `seller`, `courier`, `admin`)
- ✅ User registration/login/profile/update
- ✅ JWT-based authentication
- ✅ Admin approval workflows (sellers & couriers)
- ✅ Restaurant & item management
- ✅ Menu grouping by title (no separate Menu entity)
- ✅ Full order flow (place, approve, assign, deliver)
- ✅ Courier delivery tracking
- ✅ Rating system (1–5 stars, optional comments)
- ✅ Coupon engine with date + usage limits
- ✅ Swagger-powered OpenAPI documentation

## 🧪 Run & Test

### Step 1: Configure Database (env or system props preferred)

Server reads JDBC params from env or system properties and falls back to `persistence.xml`:

- `JDBC_URL` (default `jdbc:mysql://localhost:3306/foodapp`)
- `JDBC_USER` (default `root`)
- `JDBC_PASSWORD` (default `foodpass`)

Recommended to set `JWT_SECRET` too.

### Step 2: Run the Server

```bash
./mvnw -q clean compile exec:java
```

On startup the server:
- Ensures the database exists
- Runs Flyway migrations (V1…V6)
- Seeds default admin (phone: `admin`, password: `admin`) if missing

Base URL: `http://localhost:8080`

### Swagger / OpenAPI

- OpenAPI spec: `Server/src/main/resources/openapi.yaml`
- Swagger resources registered via Jersey; visit `/openapi` or integrate UI as needed

## 🔍 Testing

Run integration tests (in-memory + TestContainers):

```bash
./mvnw test
```

## 🏗️ Build Info

- Java Version: 23
- Maven Wrapper: supported
- Jetty + Jersey + Swagger: fully integrated

## 👨‍💻 Contributors

Mohammad Ali Naim | Hiba Assaf | Mohammad Al Misri

---
See [README.md](./README.md) for full documentation