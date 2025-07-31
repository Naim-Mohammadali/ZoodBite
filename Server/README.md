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

### Step 1: Setup MySQL

- Use `/database/docker-compose.yml` or your local setup.
- Import `/database/sql/foodapp-schema.sql` if needed.

### Step 2: Configure Database

Edit `src/main/resources/META-INF/persistence.xml`:
```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/foodapp"/>
<property name="jakarta.persistence.jdbc.user" value="root"/>
<property name="jakarta.persistence.jdbc.password" value="password"/>
```

### Step 3: Run the Server

```bash
./mvnw clean compile exec:java
```

Visit: [http://localhost:8080/api](http://localhost:8080/api)

### Step 4: Swagger UI

Docs available at: [http://localhost:8080/swagger](http://localhost:8080/swagger)

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