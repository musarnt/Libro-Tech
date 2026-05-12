# Libro Tech

Learning project built with Spring Boot as part of the Riwi backend bootcamp.

## About

REST API for managing books and categories, progressively evolved across labs:

- **Lab 1:** In-memory CRUD with MVC architecture (Controller + Model)
- **Lab 2:** Layered architecture with Spring Data JPA and H2 persistence (Controller → Service → Repository)

## Tech Stack

- Java 21
- Spring Boot 4.x
- Spring Data JPA + Hibernate
- H2 Database (file-based)
- Lombok
- Maven

## Architecture

```
controller/   → handles HTTP requests and responses
service/      → business logic and validations
repository/   → data access via JpaRepository
model/        → JPA entities (Book, Category)
```

## Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/books | List all books |
| GET | /api/books/{id} | Get book by ID |
| POST | /api/books | Create book |
| PUT | /api/books/{id} | Full update |
| PATCH | /api/books/{id} | Partial update |
| DELETE | /api/books/{id} | Delete book |
| HEAD | /api/books/{id} | Check existence |
| GET | /api/categories | List all categories |
| GET | /api/categories/{id} | Get category by ID |
| POST | /api/categories | Create category |
| PUT | /api/categories/{id} | Full update |
| DELETE | /api/categories/{id} | Delete category |

## Running Locally

```bash
./mvnw spring-boot:run
```

H2 Console available at `http://localhost:8080/h2-console`

JDBC URL: `jdbc:h2:file:./data/librotech_db`

Username: `sa` | Password: *(empty)*