# Order Management System

A relational database-driven application for managing clients, product inventories, and order processing, heavily utilizing Java Reflection to eliminate boilerplate code.

## Key Features
* **Dynamic Table Generation:** Uses Java Reflection to automatically generate UI tables at runtime based on the underlying object properties.
* **Layered Architecture:** Separated into Data Access (DAO), Business Logic (BLL), and Presentation layers for clean separation of concerns.
* **Database Integration:** Seamless integration with MySQL using JDBC, managing entities like Clients, Products, and Orders.
* **Automated Billing:** Automatically validates inventory stock and generates PDF receipts (Bills) for successfully processed orders.

## Technologies Built With
* Java
* MySQL & JDBC
* Java Reflection API
* Java Swing
* Maven
