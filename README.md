# Food Delivery App — AP Project (Spring 2025)

This project is a Java-based backend system for a food delivery app, built as a final course project using Object-Oriented Programming principles.

## 🗂️ Structure

* `backend/`: Core server logic — layered design (model, DAO, service, controller)
* `database/sql/`: SQL schema and sample data (maintained by DB developer)
* `frontend/`: JavaFX client interface (WIP)

## 🔧 Tech Stack

* Java 23 (Amazon Corretto)
* Maven
* Hibernate / JPA
* MySQL
* Docker (optional)
* IntelliJ IDEA

## 📌 Features

* User registration & role-based logic
* Restaurant & menu management
* Order placement & delivery assignment
* Admin approval workflows
* Courier delivery lifecycle
* Order rating system
* Coupon creation and validation

## 🧲 Running the Project

1. Clone the repo
2. Set up the MySQL database using `/database/sql/foodapp-schema.sql`
3. Update DB credentials in `persistence.xml`
4. Run tests or main service classes inside `backend/`

> Built with OOP design patterns, clean structure, and REST-ready services.

