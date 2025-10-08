
# 🍽️ ZoodBite — Food Delivery System (Java Full Stack)

**ZoodBite** is a complete **Java-based food delivery application**, supporting role-based functionality for **customers, sellers, couriers, and admins**.
Originally built for the **Spring 2025 Advanced Programming curriculum**, now upgraded to a **production-ready full-stack system**.

---

## 🧩 Modules

* [`/Server`](./Server): Backend API with database, authentication, and business logic
* [`/Client`](./Client): JavaFX desktop client with modern UI and role-based dashboards
* [`/Database`](./Database): SQL schema, Docker Compose config, and sample data

---

## 🎯 Key Features

* **Multi-role Access**

    * Customer, Seller, Courier, and Admin dashboards
* **Restaurant & Menu Management**

    * Sellers manage restaurants, menus, and menu items
* **Order Lifecycle**

    * Customers place orders
    * Sellers approve & manage them
    * Couriers pick up & deliver
* **Coupons & Discounts**

    * With usage limits and expiration dates
* **Ratings & Feedback**

    * Customers can rate restaurants (1–5 stars, optional comments)
* **Admin Control Panel**

    * Approve new sellers/couriers
    * View and manage all orders in the system
* **Swagger/OpenAPI Documentation**

---

## ⚙️ Tech Stack

| Layer    | Technology                       |
| -------- | -------------------------------- |
| Language | Java 23 (Amazon Corretto)        |
| Build    | Maven + Maven Wrapper            |
| Backend  | JAX-RS (Jersey), Hibernate/JPA   |
| Database | MySQL (via Docker Compose)       |
| Client   | JavaFX (FXML + CSS)              |
| Auth     | JWT (stateless authentication)   |
| Docs     | Swagger / OpenAPI 3.1            |
| Testing  | JUnit 5, Mockito, TestContainers |

---

## 🚀 Quick Start

### 1) Start MySQL (Docker)

```bash
cd Database
docker compose up -d
```

By default the app uses:
- DB: `foodapp`
- URL: `jdbc:mysql://localhost:3306/foodapp`
- User: `root`  Password: `foodpass`

You can override via env vars: `JDBC_URL`, `JDBC_USER`, `JDBC_PASSWORD`.

### 2) Build All Modules

```bash
./mvnw -q -DskipTests clean install
```

### 3) Run the Backend Server

```bash
cd Server
../mvnw -q compile exec:java
```

On start the server will:
- Ensure the `foodapp` database exists
- Run Flyway migrations (V1…V6)
- Seed a default admin if missing (phone: `admin`, password: `admin`)

Environment variables that the server honors:
- `JDBC_URL`, `JDBC_USER`, `JDBC_PASSWORD`
- `JWT_SECRET` (recommended; raw or `base64:` prefixed)

API base: `http://localhost:8080`

### 4) Run the JavaFX Client

```bash
cd Client
../mvnw -q clean javafx:run
```

Optionally configure the server URL via:
- System property: `-DAPI_BASE_URL=http://localhost:8080`
- or env var: `API_BASE_URL=http://localhost:8080`

---

## 📖 Documentation

* [Server Documentation](./Server/README.md) → DB config, migrations, endpoints, testing
* [Client Documentation](./Client/README.md) → JavaFX setup, configuration, usage

---

## 📌 Version

* Current Release: **v2.2**
* Status: **Stable But In Development**

---

## 👨‍💻 Contributors

Mohammad Ali Naim | Hiba Assaf | Mohammad Al Misri

---

> Built with ❤️ by the **ZoodBite Team**, following **clean OOP principles** and **layered architecture**.
