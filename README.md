# ZoodBite — Food Delivery System (Java Full Stack)

ZoodBite is a complete food delivery application built in Java, supporting role-based functionality across multiple user types (customers, sellers, couriers, admins). Developed as part of the Spring 2025 Advanced Programming curriculum — now extended to a full-stack production-ready system.

## 🧩 Modules

- [`/Server`](./Server): Backend API with full database & order management
- [`/Client`](./Client): WIP JavaFX desktop client interface
- [`/Database`](./Database): SQL schema, Docker Compose, sample data

## 🎯 Features

- Role-based registration (customer, seller, courier, admin)
- Restaurant and food item management
- Real-time order lifecycle (placement → approval → delivery)
- Coupon engine with usage rules and time windows
- Ratings with averages and comments
- Admin panels for approvals and analytics (server-side)
- OpenAPI 3 docs for all endpoints

## ⚙️ Stack

| Layer     | Technology            |
|-----------|------------------------|
| Language  | Java 23 (Corretto)     |
| Build     | Maven + Maven Wrapper  |
| Backend   | Hibernate, JPA, Jetty  |
| DB        | MySQL (via Docker)     |
| UI (WIP)  | JavaFX                 |
| Auth      | JWT                    |
| Docs      | Swagger / OpenAPI 3.1  |

## 🚀 Quick Start

```bash
git clone https://github.com/your-org/zoodbite.git
cd zoodbite
./mvnw clean install
```

### Run Server
```bash
cd Server
../mvnw compile exec:java
```

### View Swagger Docs
[http://localhost:8080/swagger](http://localhost:8080/swagger)

## 📦 Server Documentation

- See [Server/README.md](./Server/README.md) for full API details, setup instructions, and tech specs.
- See [Client/README.md](./Client/README.md) for full Client app details, setup instructions, and tech specs.

---

> Built with ❤️ by the ZoodBite team, with full adherence to modern Java architecture and robust OOP principles.
