# E-Commerce Backend Platform

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)

A RESTful e-commerce backend platform built with **Spring Boot** and **PostgreSQL**, focusing on catalog management, shopping cart operations, transactional order processing, image persistence, and OpenAPI documentation.

---

## Workflow

`mermaid
flowchart LR
    Client["Client / Frontend\n(Web, Mobile, Postman)"]
    --> API["REST API Layer\n(/api/v1/*)"]
    --> Service["Business Logic Layer\n(Products, Carts, Orders)"]
    --> Checkout["Atomic Checkout\n(Stock Deduction & Order Creation)"]
    --> DB[("PostgreSQL\nDatabase")]
`

---

## Features

- **Product Catalog**: Multi-criteria querying (filter by brand, category, name, or combinations) and inventory tracking.
- **Shopping Cart**: Cart management with automated total aggregation, quantity updates, and orphan item cleanup.
- **Transactional Checkout**: Atomic @Transactional checkout that validates stock, creates order snapshots, deducts inventory, and clears the cart.
- **Media Storage**: Multipart image uploads with PostgreSQL binary persistence and download streaming.
- **DTO Projection Layer**: Decoupled domain models using ModelMapper and response DTOs (ProductDto, ImageDto, UserDto).
- **Standardized API Envelope**: Uniform ApiResponse<T> payload format across all endpoints.
- **Global Exception Handling**: Centralized exception handling (ResourceNotFoundException, AlreadyExistsException) with clean error payloads.

---

## Tech Stack

| Component | Technology | Description |
| :--- | :--- | :--- |
| **Language** | Java 17 | Core language platform |
| **Framework** | Spring Boot 4.1.1 | Web MVC, Dependency Injection & Validation |
| **Security** | Spring Security + JWT | Stateless authentication & authorization |
| **Persistence** | Spring Data JPA / Hibernate | Object-Relational Mapping & repositories |
| **Database** | PostgreSQL | Relational database storage |
| **Mapping** | ModelMapper 3.2.4 | Entity-to-DTO transformation |
| **Build Tool** | Apache Maven | Dependency and build lifecycle management |

---

## Security & Authentication

The API is secured using **Spring Security** with **JSON Web Tokens (JWT)**. The application enforces a stateless session management policy (SessionCreationPolicy.STATELESS).

### Authentication Flow
1. **Register**: User registers via POST /api/v1/users/add (Permitted to all).
2. **Login**: User authenticates via POST /api/v1/auth/login by providing their email and password.
3. **Token**: The backend issues a signed JWT containing the user's claims.
4. **Access**: For subsequent protected requests (Cart, Orders, etc.), the client must include the token in the HTTP Header:
   Authorization: Bearer <your_jwt_token>

### Endpoint Permissions
- **Public**: GET routes for Products, Categories, and Images, as well as Registration and Login paths, are publicly accessible.
- **Protected**: All mutating endpoints (Cart, Orders, User Profiles, etc.) require an authenticated JWT token and evaluate the authenticated principal against the requested resources.

---

## API Endpoints

Base URL: /api/v1

| Module | Method | Endpoint | Description |
| :--- | :--- | :--- | :--- |
| **Authentication** | POST | /auth/login | Authenticate user & generate JWT |
| **Products** | GET | /products/all | List all products *(Public)* |
| | GET | /products/product/{productId} | Get product by ID *(Public)* |
| | GET | /products/by/brand-and-name?brand={b}&name={n} | Filter by brand & name *(Public)* |
| | GET | /products/by/category-and-brand?category={c}&brand={b} | Filter by category & brand *(Public)* |
| | POST | /products/add | Add a new product *(Protected)* |
| | PUT | /products/product/{productId}/update | Update existing product *(Protected)* |
| | DELETE | /products/product/{productId}/delete | Delete product and images *(Protected)* |
| **Cart** | GET | /carts/mycart | Get current user's cart *(Protected)* |
| | DELETE | /carts/{cartId}/clear | Clear all items from cart *(Protected)* |
| | GET | /carts/{cartId}/total-price | Get total price of cart *(Protected)* |
| **Cart Items** | POST | /cartItems/item/add?productId={p}&quantity={q} | Add item to user cart *(Protected)* |
| | PUT | /cartItems/item/{productId}/quantity/{q}/update | Update item quantity *(Protected)* |
| | DELETE | /cartItems/item/{productId}/remove | Remove item from cart *(Protected)* |
| **Orders** | POST | /orders/order | Checkout current cart into order *(Protected)* |
| | GET | /orders/{orderId} | Get order details *(Protected)* |
| **Categories** | GET | /categories/all | List all categories *(Public)* |
| | POST | /categories/add | Create a new category *(Protected)* |
| | PUT | /categories/category/{id}/update | Update category name *(Protected)* |
| | DELETE | /categories/category/{id}/delete | Delete category *(Protected)* |
| **Images** | POST | /images/upload | Upload images *(Protected)* |
| | GET | /images/image/download/{imageId} | Stream image binary *(Public)* |
| | DELETE | /images/image/{imageId}/delete | Delete image *(Protected)* |
| **Users** | POST | /users/add | Register new user *(Public)* |
| | GET | /users/{id} | Get user profile *(Protected)* |
| | PUT | /users/{id}/update | Update user details *(Protected)* |
| | DELETE | /users/{id}/delete | Delete user account *(Protected)* |

---

## Project Structure

`	ext
src/main/java/com/ecom/
│   config/         # Configurations (ModelMapper, OpenAPI)
│   controller/     # REST Controllers (/api/v1)
│   dto/            # Data Transfer Objects
│   enums/          # Enumerations (OrderStatus)
│   exception/      # Custom exceptions and global handlers
│   model/          # JPA Entity models
│   repository/     # Spring Data JPA repositories
│   request/        # Request payload contracts
│   response/       # Standardized ApiResponse envelope
│   service/        # Business logic interfaces and implementations
`

---

## Getting Started

### Prerequisites
- **JDK 17** or higher
- **PostgreSQL 14+**
- **Maven 3.8+** (or use included mvnw)

### 1. Database Setup
Create the PostgreSQL database:
`sql
CREATE DATABASE "MyShop_Db";
`

### 2. Configure Credentials
Update src/main/resources/application-local.properties (or pplication.properties):
`properties
spring.datasource.url=jdbc:postgresql://localhost:5432/MyShop_Db
spring.datasource.username=postgres
spring.datasource.password=your_password
`

### 3. Run the Application
`ash
./mvnw spring-boot:run
`
*(On Windows: mvn spring-boot:run)*

The server starts at http://localhost:8080.

### 4. Testing with Postman
A Postman collection is included in the root directory (E-com.postman_collection.json). Import this file into Postman to easily test all available API endpoints.
