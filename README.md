# E-Com

E-Com is a Spring Boot REST API for an e-commerce application. The project currently
contains the product domain model, JPA relationships, repository queries, and the
initial product service layer.

## Current Features

- Product, category, and image entities
- JPA mappings between products, categories, and images
- PostgreSQL persistence through Spring Data JPA
- Product lookup by ID, name, brand, and category
- Product deletion with a not-found exception
- Lombok-powered getters, setters, and constructors

## Technology Stack

- Java 17
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven
- Lombok

## Project Structure

```text
src/
├── main/
│   ├── java/com/ecom/
│   │   ├── Model/              # JPA entities
│   │   ├── exceptions/         # Domain exceptions
│   │   ├── repository/         # Spring Data repositories
│   │   ├── service/product/    # Product service contracts and logic
│   │   └── EComApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/ecom/
```

## Prerequisites

- JDK 17 or newer
- PostgreSQL
- Maven, or the included Maven Wrapper

## Configuration

Create a PostgreSQL database named `MyShop_Db`, then set the database password in
the `DB_PASSWORD` environment variable. Optional variables are also available:

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

Run the test suite:

```powershell
.\mvnw.cmd test
```

The API will be available at `http://localhost:9090`.

## Development Status

This project is under active development. Product service and controller endpoints
are being built incrementally; additional validation, API documentation, security,
and testing will be added as development continues.

## Security

Never commit database passwords, API keys, or other secrets. Use environment
variables or a local, ignored configuration file for development credentials.
