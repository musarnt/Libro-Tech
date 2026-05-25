# LibroTech

Backend learning project built with Spring Boot as part of the Riwi backend bootcamp.

## About

Library management system evolved progressively across labs:

- **Lab 1:** In-memory CRUD with MVC architecture
- **Lab 2:** Layered architecture with Spring Data JPA and H2 persistence
- **Lab 3:** Complete CRUD with PUT, DELETE, ResponseEntity and 404 handling
- **Lab 4:** Pagination, sorting, CommandLineRunner seeder and Swagger UI
- **Lab 5:** Thymeleaf UI — server-side rendered views for book management
- **Lab 6:** REST API refinement with PATCH, HEAD and improved error responses
- **Lab 7:** Thymeleaf fragments, reusable layout components and form validations
- **Lab 8:** Relational model with @ManyToOne/@ManyToMany, Soft Delete via @SQLRestriction, Flyway migrations, DTOs and Mappers

## Tech Stack

- Java 21
- Spring Boot 4.x
- Spring Data JPA + Hibernate
- PostgreSQL 16 (Docker)
- Flyway (database migrations)
- Thymeleaf (server-side UI)
- Springdoc OpenAPI (Swagger)
- Lombok
- Maven

## Architecture

    controller/
      ├── api/        → REST controllers (JSON)
      └── ui/         → Thymeleaf controllers (HTML)
    service/          → business logic and validations
    repository/       → data access via JpaRepository
    model/            → JPA entities (Book, Category, Genre, Publisher)
    dto/              → request and response DTOs
    mapper/           → entity ↔ DTO converters

## Entities

| Entity | Relations |
|--------|-----------|
| Book | @ManyToOne Category, @ManyToOne Publisher, @ManyToMany Genre |
| Category | — |
| Genre | — |
| Publisher | @OneToMany Book |

## API Endpoints

### Books
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/books | List books (paginated) |
| GET | /api/books/{id} | Get book by ID |
| POST | /api/books | Create book |
| PUT | /api/books/{id} | Full update |
| DELETE | /api/books/{id} | Soft delete |

### Categories
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/categories | List categories (paginated) |
| GET | /api/categories/{id} | Get category by ID |
| POST | /api/categories | Create category |
| PUT | /api/categories/{id} | Full update |
| DELETE | /api/categories/{id} | Soft delete |

### Genres
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/genres | List genres (paginated) |
| GET | /api/genres/{id} | Get genre by ID |
| POST | /api/genres | Create genre |
| PUT | /api/genres/{id} | Full update |
| DELETE | /api/genres/{id} | Soft delete |

### Publishers
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/publishers | List publishers (paginated) |
| GET | /api/publishers/{id} | Get publisher by ID |
| POST | /api/publishers | Create publisher |
| PUT | /api/publishers/{id} | Full update |
| DELETE | /api/publishers/{id} | Soft delete |

## UI Routes

| URL | Description |
|-----|-------------|
| /admin/books | Book catalog |
| /admin/categories | Category catalog |
| /admin/genres | Genre catalog |
| /admin/publishers | Publisher catalog |

## Running Locally

### Requirements
- Java 21
- Docker

### Setup

1. Clone the repo
2. Create `.env` in the project root (use `.env.example` as reference)
3. Start the database:

```bash
docker compose up -d
```

4. Run the app from IntelliJ or:

```bash
./mvnw spring-boot:run
```

> ⚠️ Spring Boot reads the `.env` variables via IntelliJ Run Configuration → Environment Variables.

## URLs

| Service | URL |
|---------|-----|
| Admin UI | http://localhost:8080/admin/books |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |