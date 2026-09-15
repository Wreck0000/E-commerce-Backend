# E-Com

E-Com is a Spring Boot REST API for an e-commerce application. The project contains the product domain model, shopping cart models, JPA relationships, repository queries, DTO mapping layers, and REST controller endpoints.

## Features

- **Product Management**: Full CRUD operations, product lookup by ID, name, brand, category, brand & category, brand & name.
- **DTO Layer**: ModelMapper-backed `ProductDto` and `ImageDto` data transfer object serialization preventing JSON recursion loops.
- **Cart & CartItem Domain**: JPA entities mapping shopping carts to cart items with auto-calculated total pricing.
- **Refactored Service Layer**: Java `Optional` functional pipelines for clean null-safe entity resolution and category lookup.
- **PostgreSQL Persistence**: Spring Data JPA with Lombok boilerplate reduction.

## Technology Stack

- **Java**: 17
- **Framework**: Spring Boot 4.1.1 (Spring Web, Spring Data JPA)
- **Database**: PostgreSQL
- **Mapping**: ModelMapper 3.2.4
- **Utilities**: Lombok
- **Build Tool**: Maven

## Project Structure

```text
src/
└── main/
    ├── java/com/ecom/
    │   ├── config/             # Spring configuration (ModelMapper Bean)
    │   ├── controller/         # REST API endpoints (Product, Image, Category)
    │   ├── dto/                # Data Transfer Objects (ProductDto, ImageDto)
    │   ├── exception/          # Custom exceptions
    │   ├── model/              # JPA entities (Product, Category, Image, Cart, CartItem)
    │   ├── repository/         # Spring Data JPA repositories
    │   ├── request/            # API request payload objects
    │   ├── response/           # Standardized API response wrapper
    │   └── service/            # Business logic contracts and implementations
    └── resources/
        └── application.properties
```

## Prerequisites

- JDK 17 or newer
- PostgreSQL
- Maven (or included Maven Wrapper)

## Configuration

Set up PostgreSQL database `MyShop_Db` and configure database environment variables:

```text
DB_URL=jdbc:postgresql://localhost:5432/MyShop_Db
DB_USERNAME=postgres
DB_PASSWORD=your-database-password
```

The application runs on port `9090` by default.

## Running Locally

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Run test suite:

```powershell
.\mvnw.cmd test
```

The API endpoints will be accessible under `http://localhost:9090/api/v1/...`.
