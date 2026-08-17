# 📝 Todo List API

Uma API RESTful robusta e escalável para gerenciamento de tarefas (*To-Do List*), desenvolvida em **Java 25** com **Spring Boot**. A aplicação conta com autenticação e autorização via **JWT (JSON Web Token)**, persistência em banco de dados **PostgreSQL**, validações de entrada e documentação interativa via **Swagger / OpenAPI**.

---

## 📌 Sumário

- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura do Projeto](#-arquitetura-do-projeto)
- [Pré-requisitos](#-pré-requisitos)
- [Variáveis de Ambiente](#-variáveis-de-ambiente)
- [Como Executar o Projeto](#-como-executar-o-projeto)
  - [1. Execução Local](#1-execução-local)
  - [2. Execução com Docker](#2-execução-com-docker)
  - [3. Execução dos Testes](#3-execução-dos-testes)
- [Documentação da API (Swagger/OpenAPI)](#-documentação-da-api-swaggeropenapi)
- [Autenticação JWT](#-autenticação-jwt)
- [Endpoints da API & Exemplos](#-endpoints-da-api--exemplos)
  - [Autenticação](#autenticação)
  - [Tarefas (Todos)](#tarefas-todos)

---

## 🚀 Tecnologias Utilizadas

- **Linguagem:** [Java 25](https://openjdk.org/projects/jdk/25/)
- **Framework:** [Spring Boot 4.1.0](https://spring.io/projects/spring-boot)
- **Segurança:** [Spring Security](https://spring.io/projects/spring-security) & [JJWT (io.jsonwebtoken)](https://github.com/jwtk/jjwt)
- **Persistência de Dados:** [Spring Data JPA](https://spring.io/projects/spring-data-jpa) & [Hibernate](https://hibernate.org/)
- **Bancos de Dados:**
  - [PostgreSQL](https://www.postgresql.org/) (Ambiente de Produção e Desenvolvimento)
  - [H2 Database](https://www.h2database.com/) (Banco em memória para testes automatizados)
- **Validação:** [Jakarta Validation / Hibernate Validator](https://beanvalidation.org/)
- **Documentação da API:** [SpringDoc OpenAPI UI / Swagger](https://springdoc.org/)
- **Gerenciador de Dependências:** [Apache Maven](https://maven.apache.org/)
- **Containerização:** [Docker](https://www.docker.com/)

---

## 🏗️ Arquitetura do Projeto

O projeto segue o padrão de **Arquitetura em Camadas (Layered Architecture)**, garantindo separação clara de responsabilidades, alta coesão e baixo acoplamento:

```text
src/main/java/com/cleoaguiar/todolistapi/
│
├── config/              # Configurações de segurança, filtros JWT e OpenAPI/Swagger
│   ├── JwtAuthenticationFilter.java
│   ├── OpenApiConfig.java
│   └── SecurityConfig.java
│
├── controller/          # Controladores REST e interfaces de documentação
│   ├── api/             # Interfaces com anotações Swagger/OpenAPI
│   │   ├── AuthApi.java
│   │   └── TodoApi.java
│   ├── AuthController.java
│   └── TodoController.java
│
├── dto/                 # Records DTO (Data Transfer Objects) para requisição e resposta
│   ├── AuthRequest.java
│   ├── AuthResponse.java
│   ├── TodoRequest.java
│   ├── TodoResponse.java
│   ├── UserRegisterRequest.java
│   └── UserResponse.java
│
├── entity/              # Entidades mapeadas para o banco de dados (JPA)
│   ├── Todo.java
│   └── User.java
│
├── enums/               # Enumerações (ex: status das tarefas)
│   └── TodoStatus.java
│
├── exception/           # Tratamento global de erros e exceções personalizadas
│   ├── ErrorResponse.java
│   ├── ForbiddenException.java
│   ├── GlobalExceptionHandler.java
│   └── TodoNotFoundException.java
│
├── repository/          # Interfaces de acesso a dados (Spring Data JPA)
│   ├── TodoRepository.java
│   └── UserRepository.java
│
└── service/             # Camada de regras de negócio e geração/validação JWT
    ├── AuthService.java
    ├── JwtService.java
    └── TodoService.java
```

---

## 📋 Pré-requisitos

Antes de iniciar, certifique-se de ter instalado em sua máquina:

- **JDK 25** ou superior ([Eclipse Temurin](https://adoptium.net/) recomendado)
- **Git**
- **Docker** (opcional, para rodar o banco de dados ou a aplicação completa em container)
- **Maven 3.9+** (opcional, pois o projeto inclui o Maven Wrapper `./mvnw`)

---

## 🔐 Variáveis de Ambiente

A aplicação necessita das seguintes variáveis de ambiente para conectar-se ao banco de dados e assinar tokens JWT:

| Variável | Descrição | Exemplo | Obrigatório |
| --- | --- | --- | --- |
| `DB_URL` | URL JDBC de conexão com o PostgreSQL | `jdbc:postgresql://localhost:5432/todolist` | Sim |
| `DB_USERNAME` | Usuário do banco de dados | `postgres` | Sim |
| `DB_PASSWORD` | Senha do banco de dados | `postgres` | Sim |
| `JWT_SECRET` | Chave secreta usada para assinar/validar tokens JWT (mín. 256 bits / 32 caracteres) | `sua-chave-secreta-super-segura-com-minimo-de-32-chars` | Sim |

---

## ⚙️ Como Executar o Projeto

### 1. Execução Local

#### Passo 1: Clonar o repositório
```bash
git clone https://github.com/CleoAguiar/todo-list-api.git
cd todo-list-api
```

#### Passo 2: Subir uma instância do PostgreSQL
Você pode iniciar rapidamente um container PostgreSQL via Docker:

```bash
docker run --name postgres-todo \
  -e POSTGRES_DB=todolist \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:alpine
```

#### Passo 3: Definir as variáveis de ambiente e rodar a aplicação

- **Linux / macOS (Bash):**
  ```bash
  export DB_URL="jdbc:postgresql://localhost:5432/todolist"
  export DB_USERNAME="postgres"
  export DB_PASSWORD="postgres"
  export JWT_SECRET="minha-chave-secreta-jwt-super-segura-com-32-bytes-ou-mais"

  ./mvnw spring-boot:run
  ```

- **Windows (PowerShell):**
  ```powershell
  $env:DB_URL="jdbc:postgresql://localhost:5432/todolist"
  $env:DB_USERNAME="postgres"
  $env:DB_PASSWORD="postgres"
  $env:JWT_SECRET="minha-chave-secreta-jwt-super-segura-com-32-bytes-ou-mais"

  .\mvnw.cmd spring-boot:run
  ```

- **Windows (CMD):**
  ```cmd
  set DB_URL=jdbc:postgresql://localhost:5432/todolist
  set DB_USERNAME=postgres
  set DB_PASSWORD=postgres
  set JWT_SECRET=minha-chave-secreta-jwt-super-segura-com-32-bytes-ou-mais

  mvnw.cmd spring-boot:run
  ```

---

### 2. Execução com Docker

O projeto possui um `Dockerfile` multi-stage pronto para produção.

#### Opção A: Criando uma rede Docker para integrar App e Banco

1. **Crie a rede Docker:**
   ```bash
   docker network create todo-network
   ```

2. **Inicie o banco PostgreSQL na rede:**
   ```bash
   docker run --name postgres-todo \
     --network todo-network \
     -e POSTGRES_DB=todolist \
     -e POSTGRES_USER=postgres \
     -e POSTGRES_PASSWORD=postgres \
     -p 5432:5432 \
     -d postgres:alpine
   ```

3. **Construa a imagem da aplicação:**
   ```bash
   docker build -t todo-list-api .
   ```

4. **Inicie o container da aplicação:**
   - **Linux / macOS (Bash):**
     ```bash
     docker run -d \
       --name todo-list-api-app \
       --network todo-network \
       -p 8080:8080 \
       -e DB_URL="jdbc:postgresql://postgres-todo:5432/todolist" \
       -e DB_USERNAME="postgres" \
       -e DB_PASSWORD="postgres" \
       -e JWT_SECRET="minha-chave-secreta-jwt-super-segura-com-32-bytes-ou-mais" \
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
       -e JWT_SECRET="minha-chave-secreta-jwt-super-segura-com-32-bytes-ou-mais" `
       todo-list-api
     ```

A API estará disponível em: `http://localhost:8080`

---

### 3. Execução dos Testes

Os testes automatizados utilizam banco de dados **H2 em memória** e não necessitam de configuração externa de banco:

- **Linux / macOS:**
  ```bash
  ./mvnw test
  ```
- **Windows:**
  ```powershell
  .\mvnw.cmd test
  ```

---

## 📖 Documentação da API (Swagger/OpenAPI)

A documentação interativa com Swagger UI é gerada automaticamente pelo SpringDoc:

- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) (ou `http://localhost:8080/swagger-ui.html`)
- **Especificação OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🔑 Autenticação JWT

A API utiliza autenticação baseada em tokens **JWT (JSON Web Token)**:

1. Registre um novo usuário através do endpoint `POST /auth/register`.
2. Autentique-se pelo endpoint `POST /auth/login` informando `email` e `password`.
3. A resposta conterá um token de autenticação:
   ```json
   {
     "token": "eyJhbGciOiJIUzI1NiJ9..."
   }
   ```
4. Para acessar endpoints protegidos (`/todos/**`), adicione o cabeçalho HTTP:
   ```http
   Authorization: Bearer <SEU_TOKEN_JWT>
   ```

> ℹ️ **Nota de Segurança:** Cada usuário tem acesso estrito e exclusivo apenas às suas próprias tarefas. Tentativas de acessar tarefas de terceiros retornarão `403 Forbidden`.

---

## 📡 Endpoints da API & Exemplos

### Autenticação

#### 1. Registrar Usuário
- **Rota:** `POST /auth/register`
- **Autenticação:** Pública

**Requisição:**
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "Jose da Silva",
    "email": "jose@email.com",
    "password": "senhaSegura123"
  }'
```

**Resposta:**
- **Status:** `201 Created`

---

#### 2. Autenticar (Login)
- **Rota:** `POST /auth/login`
- **Autenticação:** Pública

**Requisição:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "jose@email.com",
    "password": "senhaSegura123"
  }'
```

**Resposta:**
- **Status:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb3NlQGVtYWlsLmNvbSIsImlhdCI6MTcyMzg0MzIwMCwiZXhwIjoxNzIzODQ2ODAwfQ.xyz..."
}
```

---

### Tarefas (Todos)

> ⚠️ Todos os endpoints abaixo exigem o cabeçalho `Authorization: Bearer <TOKEN>`.

---

#### 1. Listar Tarefas (Paginado)
- **Rota:** `GET /todos?page=0&limit=10`
- **Parâmetros de Consulta (Query Params):**
  - `page` *(opcional, padrão `0`)*: Número da página (base zero).
  - `limit` *(opcional, padrão `10`)*: Quantidade de registros por página.

**Requisição:**
```bash
curl -X GET "http://localhost:8080/todos?page=0&limit=10" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

**Resposta:**
- **Status:** `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "title": "Estudar Spring Boot",
      "description": "Revisar documentação OpenAPI e Spring Security",
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

#### 2. Criar Tarefa
- **Rota:** `POST /todos`
- **Valores permitidos para `status`:** `TODO`, `IN_PROGRESS`, `DONE`

**Requisição:**
```bash
curl -X POST http://localhost:8080/todos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{
    "title": "Estudar Spring Boot",
    "description": "Revisar anotações OpenAPI e testes unitários",
    "status": "TODO"
  }'
```

**Resposta:**
- **Status:** `201 Created`
```json
{
  "id": 1,
  "title": "Estudar Spring Boot",
  "description": "Revisar anotações OpenAPI e testes unitários",
  "status": "TODO",
  "createdAt": "2026-08-17T10:00:00.000",
  "updatedAt": "2026-08-17T10:00:00.000"
}
```

---

#### 3. Buscar Tarefa por ID
- **Rota:** `GET /todos/{id}`

**Requisição:**
```bash
curl -X GET http://localhost:8080/todos/1 \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

**Resposta:**
- **Status:** `200 OK`
```json
{
  "id": 1,
  "title": "Estudar Spring Boot",
  "description": "Revisar anotações OpenAPI e testes unitários",
  "status": "TODO",
  "createdAt": "2026-08-17T10:00:00.000",
  "updatedAt": "2026-08-17T10:00:00.000"
}
```

---

#### 4. Atualizar Tarefa
- **Rota:** `PUT /todos/{id}`

**Requisição:**
```bash
curl -X PUT http://localhost:8080/todos/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{
    "title": "Estudar Spring Boot e Docker",
    "description": "Concluído o estudo de OpenAPI, agora testando com Docker",
    "status": "IN_PROGRESS"
  }'
```

**Resposta:**
- **Status:** `200 OK`
```json
{
  "id": 1,
  "title": "Estudar Spring Boot e Docker",
  "description": "Concluído o estudo de OpenAPI, agora testando com Docker",
  "status": "IN_PROGRESS",
  "createdAt": "2026-08-17T10:00:00.000",
  "updatedAt": "2026-08-17T10:35:00.000"
}
```

---

#### 5. Excluir Tarefa
- **Rota:** `DELETE /todos/{id}`

**Requisição:**
```bash
curl -X DELETE http://localhost:8080/todos/1 \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

**Resposta:**
- **Status:** `204 No Content`

---

## ⚠️ Estrutura de Erros

Em caso de erros na requisição (validação, autenticação, recurso não encontrado), a API retorna o seguinte formato padronizado:

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
