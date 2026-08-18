<p align="right">
  <b>🇺🇸 English</b> | <a href="./README.pt-br.md">🇧🇷 Português</a>
</p>

# 📝 Todo List API

A robust and scalable RESTful API for managing to-do lists, built with **Java 25** and **Spring Boot**. The application features authentication and authorization using **JWT (JSON Web Token)**, persistence with **PostgreSQL**, request validation, and interactive documentation via **Swagger / OpenAPI**.

---

## 📌 Project Reference

This project was developed based on the [Todo List API project from roadmap.sh](https://roadmap.sh/projects/todo-list-api).

---

## 📌 Table of Contents

- [Technologies Used](#-technologies-used)
- [Project Architecture](#-project-architecture)
- [Prerequisites](#-prerequisites)
- [Environment Variables](#-environment-variables)
- [How to Run the Project](#-how-to-run-the-project)
  - [1. Running Locally](#1-running-locally)
  - [2. Running with Docker](#2-running-with-docker)
  - [3. Running Tests](#3-running-tests)
- [API Documentation (Swagger/OpenAPI)](#-api-documentation-swaggeropenapi)
- [JWT Authentication](#-jwt-authentication)
- [API Endpoints & Examples](#-api-endpoints--examples)
  - [Authentication](#authentication)
  - [Tasks (Todos)](#tasks-todos)
- [Error Handling](#-error-handling)

---

## 🚀 Technologies Used

- **Language:** [Java 25](https://openjdk.org/projects/jdk/25/)
- **Framework:** [Spring Boot 4.1.0](https://spring.io/projects/spring-boot)
- **Security:** [Spring Security](https://spring.io/projects/spring-security) & [JJWT (io.jsonwebtoken)](https://github.com/jwtk/jjwt)
- **Data Persistence:** [Spring Data JPA](https://spring.io/projects/spring-data-jpa) & [Hibernate](https://hibernate.org/)
- **Databases:**
  - [PostgreSQL](https://www.postgresql.org/) (Production & Development runtime)
  - [H2 Database](https://www.h2database.com/) (In-memory database for automated tests)
- **Validation:** [Jakarta Validation / Hibernate Validator](https://beanvalidation.org/)
- **API Documentation:** [SpringDoc OpenAPI UI / Swagger](https://springdoc.org/)
- **Build Tool:** [Apache Maven](https://maven.apache.org/)
- **Containerization:** [Docker](https://www.docker.com/)

---

## 🏗️ Project Architecture

The project follows a **Layered Architecture**, ensuring clear separation of concerns, high cohesion, and low coupling:

```text
src/main/java/com/cleoaguiar/todolistapi/
│
├── config/              # Security settings, JWT filters, and OpenAPI/Swagger configuration
│   ├── JwtAuthenticationFilter.java
│   ├── OpenApiConfig.java
│   └── SecurityConfig.java
│
├── controller/          # REST controllers and documentation interfaces
│   ├── api/             # Interfaces with Swagger/OpenAPI annotations
│   │   ├── AuthApi.java
│   │   └── TodoApi.java
│   ├── AuthController.java
│   └── TodoController.java
│
├── dto/                 # DTOs (Data Transfer Objects / Records) for requests and responses
│   ├── AuthRequest.java
│   ├── AuthResponse.java
│   ├── TodoRequest.java
│   ├── TodoResponse.java
│   ├── UserRegisterRequest.java
│   └── UserResponse.java
│
├── entity/              # JPA database entities
│   ├── Todo.java
│   └── User.java
│
├── enums/               # Enums (e.g., task status)
│   └── TodoStatus.java
│
├── exception/           # Global exception handling and custom exceptions
│   ├── ErrorResponse.java
│   ├── ForbiddenException.java
│   ├── GlobalExceptionHandler.java
│   └── TodoNotFoundException.java
│
├── repository/          # Data access interfaces (Spring Data JPA)
│   ├── TodoRepository.java
│   └── UserRepository.java
│
└── service/             # Business logic layer and JWT generation/validation
    ├── AuthService.java
    ├── JwtService.java
    └── TodoService.java
```

---

## 📋 Prerequisites

Before running the application, make sure you have installed:

- **JDK 25** or higher ([Eclipse Temurin](https://adoptium.net/) recommended)
- **Git**
- **Docker** (optional, to run PostgreSQL or the entire application in a container)
- **Maven 3.9+** (optional, as the repository includes the Maven Wrapper `./mvnw`)

---

## 🔐 Environment Variables

The application requires the following environment variables to connect to the database and sign JWT tokens:

| Variable | Description | Example | Required |
| --- | --- | --- | --- |
| `DB_URL` | JDBC URL for PostgreSQL connection | `jdbc:postgresql://localhost:5432/todolist` | Yes |
| `DB_USERNAME` | Database username | `postgres` | Yes |
| `DB_PASSWORD` | Database password | `postgres` | Yes |
| `JWT_SECRET` | Secret key used to sign and validate JWT tokens (min. 256 bits / 32 characters) | `your-super-secret-jwt-key-with-at-least-32-chars` | Yes |

---

## ⚙️ How to Run the Project

### 1. Running Locally

#### Step 1: Clone the repository
```bash
git clone https://github.com/CleoAguiar/todo-list-api.git
cd todo-list-api
```

#### Step 2: Start a PostgreSQL database
You can start a PostgreSQL container using Docker:

```bash
docker run --name postgres-todo \
  -e POSTGRES_DB=todolist \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:alpine
```

#### Step 3: Set environment variables and start the application

- **Linux / macOS (Bash):**
  ```bash
  export DB_URL="jdbc:postgresql://localhost:5432/todolist"
  export DB_USERNAME="postgres"
  export DB_PASSWORD="postgres"
  export JWT_SECRET="your-super-secret-jwt-key-with-at-least-32-chars"

  ./mvnw spring-boot:run
  ```

- **Windows (PowerShell):**
  ```powershell
  $env:DB_URL="jdbc:postgresql://localhost:5432/todolist"
  $env:DB_USERNAME="postgres"
  $env:DB_PASSWORD="postgres"
  $env:JWT_SECRET="your-super-secret-jwt-key-with-at-least-32-chars"

  .\mvnw.cmd spring-boot:run
  ```

- **Windows (CMD):**
  ```cmd
  set DB_URL=jdbc:postgresql://localhost:5432/todolist
  set DB_USERNAME=postgres
  set DB_PASSWORD=postgres
  set JWT_SECRET=your-super-secret-jwt-key-with-at-least-32-chars

  mvnw.cmd spring-boot:run
  ```

---

### 2. Running with Docker

The project includes a production-ready multi-stage `Dockerfile`.

#### Option A: Creating a Docker network to connect App and Database

1. **Create a Docker network:**
   ```bash
   docker network create todo-network
   ```

2. **Start the PostgreSQL container inside the network:**
   ```bash
   docker run --name postgres-todo \
     --network todo-network \
     -e POSTGRES_DB=todolist \
     -e POSTGRES_USER=postgres \
     -e POSTGRES_PASSWORD=postgres \
     -p 5432:5432 \
     -d postgres:alpine
   ```

3. **Build the application image:**
   ```bash
   docker build -t todo-list-api .
   ```

4. **Run the application container:**
   - **Linux / macOS (Bash):**
     ```bash
     docker run -d \
       --name todo-list-api-app \
       --network todo-network \
       -p 8080:8080 \
       -e DB_URL="jdbc:postgresql://postgres-todo:5432/todolist" \
       -e DB_USERNAME="postgres" \
       -e DB_PASSWORD="postgres" \
       -e JWT_SECRET="your-super-secret-jwt-key-with-at-least-32-chars" \
       todo-list-api
     ```
   - **Windows (PowerShell):**
     ```powershell
     docker run -d `
       --name todo-list-api-app `
       --network todo-network `
       -p 8080:8080 `
       -e DB_URL="jdbc:postgresql://postgres-todo:5432/todolist" `
       -e DB_USERNAME="postgres" `
       -e DB_PASSWORD="postgres" `
       -e JWT_SECRET="your-super-secret-jwt-key-with-at-least-32-chars" `
       todo-list-api
     ```

The API will be available at: `http://localhost:8080`

---

### 3. Running Tests

Automated tests run against an **in-memory H2 database** and do not require external database setup:

- **Linux / macOS:**
  ```bash
  ./mvnw test
  ```
- **Windows:**
  ```powershell
  .\mvnw.cmd test
  ```

---

## 📖 API Documentation (Swagger/OpenAPI)

Interactive API documentation via Swagger UI is automatically generated with SpringDoc:

- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) (or `http://localhost:8080/swagger-ui.html`)
- **OpenAPI JSON Spec:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🔑 JWT Authentication

The API uses **JWT (JSON Web Token)** based authentication:

1. Register a new user at `POST /auth/register`.
2. Authenticate at `POST /auth/login` providing `email` and `password`.
3. The response will return an authentication token:
   ```json
   {
     "token": "eyJhbGciOiJIUzI1NiJ9..."
   }
   ```
4. To access protected endpoints (`/todos/**`), include the following HTTP header:
   ```http
   Authorization: Bearer <YOUR_JWT_TOKEN>
   ```

> ℹ️ **Security Note:** Each user has strictly isolated access to their own tasks. Attempting to access tasks belonging to another user will return `403 Forbidden`.

---

## 📡 API Endpoints & Examples

### Authentication

#### 1. Register User
- **Endpoint:** `POST /auth/register`
- **Authentication:** Public

**Request:**
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "Jose da Silva",
    "email": "jose@email.com",
    "password": "securePassword123"
  }'
```

**Response:**
- **Status:** `201 Created`

---

#### 2. Authenticate (Login)
- **Endpoint:** `POST /auth/login`
- **Authentication:** Public

**Request:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "jose@email.com",
    "password": "securePassword123"
  }'
```

**Response:**
- **Status:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb3NlQGVtYWlsLmNvbSIsImlhdCI6MTcyMzg0MzIwMCwiZXhwIjoxNzIzODQ2ODAwfQ.xyz..."
}
```

---

### Tasks (Todos)

> ⚠️ All endpoints below require the `Authorization: Bearer <TOKEN>` header.

---

#### 1. List Tasks (Paginated)
- **Endpoint:** `GET /todos?page=0&limit=10`
- **Query Parameters:**
  - `page` *(optional, default `0`)*: Page number (zero-based).
  - `limit` *(optional, default `10`)*: Number of items per page.

**Request:**
```bash
curl -X GET "http://localhost:8080/todos?page=0&limit=10" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Response:**
- **Status:** `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "title": "Study Spring Boot",
      "description": "Review OpenAPI documentation and Spring Security",
      "status": "TODO",
      "createdAt": "2026-08-17T10:00:00",
      "updatedAt": "2026-08-17T10:00:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 1,
  "last": true,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "numberOfElements": 1,
  "first": true,
  "empty": false
}
```

---

#### 2. Create Task
- **Endpoint:** `POST /todos`
- **Allowed values for `status`:** `TODO`, `IN_PROGRESS`, `DONE`

**Request:**
```bash
curl -X POST http://localhost:8080/todos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "title": "Study Spring Boot",
    "description": "Review OpenAPI annotations and unit tests",
    "status": "TODO"
  }'
```

**Response:**
- **Status:** `201 Created`
```json
{
  "id": 1,
  "title": "Study Spring Boot",
  "description": "Review OpenAPI annotations and unit tests",
  "status": "TODO",
  "createdAt": "2026-08-17T10:00:00.000",
  "updatedAt": "2026-08-17T10:00:00.000"
}
```

---

#### 3. Get Task by ID
- **Endpoint:** `GET /todos/{id}`

**Request:**
```bash
curl -X GET http://localhost:8080/todos/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Response:**
- **Status:** `200 OK`
```json
{
  "id": 1,
  "title": "Study Spring Boot",
  "description": "Review OpenAPI annotations and unit tests",
  "status": "TODO",
  "createdAt": "2026-08-17T10:00:00.000",
  "updatedAt": "2026-08-17T10:00:00.000"
}
```

---

#### 4. Update Task
- **Endpoint:** `PUT /todos/{id}`

**Request:**
```bash
curl -X PUT http://localhost:8080/todos/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "title": "Study Spring Boot and Docker",
    "description": "Completed OpenAPI study, now testing with Docker",
    "status": "IN_PROGRESS"
  }'
```

**Response:**
- **Status:** `200 OK`
```json
{
  "id": 1,
  "title": "Study Spring Boot and Docker",
  "description": "Completed OpenAPI study, now testing with Docker",
  "status": "IN_PROGRESS",
  "createdAt": "2026-08-17T10:00:00.000",
  "updatedAt": "2026-08-17T10:35:00.000"
}
```

---

#### 5. Delete Task
- **Endpoint:** `DELETE /todos/{id}`

**Request:**
```bash
curl -X DELETE http://localhost:8080/todos/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Response:**
- **Status:** `204 No Content`

---

## ⚠️ Error Handling

In case of errors (validation failures, authentication issues, resource not found), the API returns a standardized JSON error structure:

```json
{
  "timestamp": "2026-08-17T10:15:30.123456",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validação.",
  "errors": {
    "title": "O título é obrigatório.",
    "email": "E-mail inválido."
  }
}
```
