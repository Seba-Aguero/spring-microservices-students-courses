# Microservices System - Student and Course Management

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)

Microservices system built with Spring Boot for student and course management, implementing a modern distributed architecture.

## 📋 Table of Contents

- [Architecture](#-architecture)
- [Technologies](#-technologies)
- [Microservices](#-microservices)
- [Configuration](#-configuration)
- [Execution](#-execution)
- [Features](#-features)
- [Author](#-author)

## 🏗 Architecture

The system consists of the following microservices:

- **Config Server**: Centralizes configuration for all microservices
- **Eureka Server**: Service registration and discovery
- **API Gateway**: Single entry point for all requests
- **Auth Service**: Authentication and authorization management
- **Student Service**: Student management
- **Course Service**: Course management

## 🔧 Technologies

- **Spring Boot 3.2.4**
- **Spring Cloud**
- **Spring Security + JWT**
- **Spring Data JPA**
- **OpenAPI/Swagger**
- **RabbitMQ**
- **MySQL** (Students)
- **PostgreSQL** (Courses)
- **Docker**

## 🔍 Microservices

### Config Server (Port: 8888)
Centralizes configuration for all microservices.

### Eureka Server (Port: 8761)
Service registration and discovery.

### API Gateway (Port: 8080)
Request routing and filtering.

### Auth Service
Authentication management with:
- User registration
- Login
- JWT token management
- Roles and permissions

### Student Service
Student management with:
- Student CRUD operations
- Data validation
- RabbitMQ event integration

### Course Service
Course management with:
- Course CRUD operations
- Student assignment
- Student cache per course
- Asynchronous communication

## ⚙ Configuration

1. Clone the repository:
```bash
git clone https://github.com/Seba-Aguero/spring-microservices-students-courses
```

2. Create `.env` file in each microservice based on `.env.example`:
```properties
# Database configurations
DB_HOST=localhost
DB_PORT_MYSQL=3306
DB_NAME_STUDENT=msvc-students
DB_USERNAME_MYSQL=root
DB_PASSWORD_MYSQL=root

DB_PORT_POSTGRES=5432
DB_NAME_COURSE=msvc-courses
DB_USERNAME_POSTGRES=postgres
DB_PASSWORD_POSTGRES=admin

# RabbitMQ configuration
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest

# Eureka configuration
EUREKA_URL=http://localhost:8761/eureka
```

## 🚀 Execution

1. Start infrastructure services:
```bash
docker-compose up -d
```

2. Microservices startup order:
   1. Config Server
   2. Eureka Server
   3. Auth Service
   4. Student Service
   5. Course Service
   6. API Gateway


## ✨ Features

- **Microservices-based architecture**
- **Centralized configuration**
- **Service Discovery**
- **API Gateway**
- **JWT authentication and authorization**
- **OpenAPI/Swagger documentation**
- **Distributed cache**
- **Asynchronous communication with RabbitMQ**
- **Global error handling**
- **Data validation**
- **Containerization with Docker**

## Author

Sebastián Agüero