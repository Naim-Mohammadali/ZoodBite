

# 📌 ZoodBite Client — JavaFX Frontend for Food Delivery System

This module is the **JavaFX-based client application** of **ZoodBite**, a role-based food delivery system.
It provides an interactive GUI for customers, sellers, couriers, and admins to access the platform’s functionalities through the REST API.



## ⚙️ Technologies

* **Language:** Java 23 (Amazon Corretto)
* **Framework:** JavaFX (FXML-based UI)
* **Architecture:** MVC (Controller–View separation), DTO
* **Build Tool:** Maven (supports Maven Wrapper)
* **Networking:** REST API (via Java `HttpClient`)
* **Session Handling:** Custom `SessionManager`
* **Styling:** CSS (`theme.css`)
* **Persistence:** Session saved to temp file for re-login convenience



## 📁 Structure

```
Client/src/main/java
├── controller/            # JavaFX controllers for pages
│   ├── customer/          # Customer dashboards & order flow
│   ├── seller/            # Seller dashboards & restaurant management
│   ├── courier/           # Courier dashboards & delivery handling
│   └── admin/             # Admin dashboards & approval panels
├── model/                 # Client-side models (if needed)
├── network/               # REST API endpoints & DTOs
│   ├── dto/               # Data Transfer Objects (User, Restaurant, Order, etc.)
│   └── endpoint/          # Classes wrapping REST API calls
├── util/                  # Session manager & helper classes
└── Main.java              # Application entry point
Client/src/main/resources
├── view/                  # All FXML UI files
│   ├── login.fxml
│   ├── register.fxml
│   ├── customer/
│   ├── seller/
│   ├── courier/
│   └── admin/
└── style/theme.css        # Shared styling
```


## 🔐 Features

* ✅ **Authentication**

    * Login & Registration with role selection
    * Persistent session management via temp file
* ✅ **Customer**

    * Search & browse restaurants
    * View menus & add items to cart
    * Apply coupons and checkout
    * View order history
* ✅ **Seller**

    * Create & manage restaurant
    * Add menus & items
    * Approve customer orders
* ✅ **Courier**

    * Accept available deliveries
    * Mark orders as delivered
* ✅ **Admin**

    * Approve sellers & couriers
    * Manage all orders in the system


## 🧪 Run the Client

### Step 1: Ensure Backend is Running

The client connects to the ZoodBite server at `http://localhost:8080`.

Start the server first (see [server README](../server/README.md)).

### Step 2: Run the Client

```bash
./mvnw clean javafx:run
```
or, if you have Maven globally:

```bash
mvn clean javafx:run
```

## 🎨 Design & Styling

* **Fixed window size:** 600×800 px
* **Theme:** Configured in `/style/theme.css`
* **Branding Colors:**

    * Background: `#F4F4F4`
    * Primary: `#391DAA`, `#1E0E7C`
    * Buttons: `#E89E1C` (default), `#C97111` (hover)
* **Font:** IBM Plex Sans Arabic



## 🛠️ Notes

* All API requests are routed via `network.endpoint.*` classes.
* `SessionManager` keeps:

    * Current logged-in user
    * Current restaurant (if seller/customer)
    * Cart contents
    * Order history (post-checkout)


## 👨‍💻 Contributors

Mohammad Ali Naim | Hiba Assaf | Mohammad Al Misri

---
See [README.md](./README.md) for full documentation