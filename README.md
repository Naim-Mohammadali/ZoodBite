
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

```bash
git clone https://github.com/your-org/zoodbite.git
cd zoodbite
./mvnw clean install
```

### Start the Backend Server

```bash
cd Server
../mvnw compile exec:java
```

Access the API at: [http://localhost:8080/api](http://localhost:8080/api)

### Run the JavaFX Client

```bash
cd Client
../mvnw clean javafx:run
```

---

## 📖 Documentation

* [Server Documentation](./Server/README.md) → API setup, endpoints, and testing
* [Client Documentation](./Client/README.md) → JavaFX setup, dashboard navigation, and usage

---

## 📌 Version

* Current Release: **v2.0**
* Status: **Stable But In Development**

---

## 👨‍💻 Contributors

Mohammad Ali Naim | Hiba Assaf | Mohammad Al Misri

---

> Built with ❤️ by the **ZoodBite Team**, following **clean OOP principles** and **layered architecture**.
