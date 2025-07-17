
-- ==========================
-- Step 0: Create DB
-- ==========================
CREATE DATABASE IF NOT EXISTS foodappdb;
USE foodappdb;

-- ==========================
-- USERS
-- ==========================
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'CUSTOMER', 'SELLER', 'COURIER') NOT NULL,
    status ENUM('PENDING', 'ACTIVE', 'BLOCKED') DEFAULT 'PENDING',
    available BOOLEAN DEFAULT FALSE
);

-- ==========================
-- RESTAURANTS
-- ==========================
CREATE TABLE restaurants (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(20),
    status ENUM('PENDING', 'ACTIVE', 'BLOCKED') DEFAULT 'PENDING',
    seller_id INT NOT NULL,
    FOREIGN KEY (seller_id) REFERENCES users(id)
);

-- ==========================
-- MENU ITEMS
-- ==========================
CREATE TABLE menu_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DOUBLE NOT NULL,
    quantity INT DEFAULT 0,
    image_url VARCHAR(255),
    category VARCHAR(50),
    restaurant_id INT NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

-- ==========================
-- ORDERS
-- ==========================
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PLACED', 'PREPARING', 'READY_FOR_PICKUP', 'IN_TRANSIT', 'DELIVERED', 'CANCELLED') DEFAULT 'PLACED',
    total DOUBLE DEFAULT 0,
    customer_id INT NOT NULL,
    courier_id INT,
    restaurant_id INT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (courier_id) REFERENCES users(id),
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

-- ==========================
-- ORDER_MENU_ITEMS (Join table)
-- ==========================
CREATE TABLE order_menu_items (
    order_id INT,
    menu_item_id INT,
    quantity INT DEFAULT 1,
    PRIMARY KEY (order_id, menu_item_id),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);
