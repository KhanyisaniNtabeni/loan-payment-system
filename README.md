# Loan Payment System

A Spring Boot application to manage loans and payments, including creating loans, making payments, and tracking loan status.

---

## Table of Contents

- [Prerequisites](#prerequisites)
- [Building and Running the Application](#building-and-running-the-application)
- [API Endpoints](#api-endpoints)
- [Testing the APIs](#testing-the-apis)
- [Swagger API Documentation](#swagger-api-documentation)
- [Configuration](#configuration)
- [Author](#author)

---

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- (Optional) Docker (if you want to containerize the app)
- An IDE such as IntelliJ IDEA or Eclipse for development

---

## Building and Running the Application

1. **Clone the repository**

   ```bash
   git clone https://github.com/your-repo/loan-payment-system.git
   cd loan-payment-system
   
2. **Build with Maven**

    ```bash
   mvn clean install

3. **Run the application**
    ```bash
   mvn spring-boot:run

4. **Application will start on:**
  - http://localhost:8080


5. **API Endpoints**
   
| Method | URL           | Description              |
| ------ | ------------- | ------------------------ |
| POST   | `/loans`      | Create a new loan        |
| GET    | `/loans/{id}` | Get loan details by ID   |
| POST   | `/payments`   | Make a payment on a loan |

6. **Create a Loan**

      - request:
   ```bash
      curl -X POST http://localhost:8080/loans \
      -H "Content-Type: application/json" \
      -d '{
      "loanAmount": 1000.00,
      "term": 12
      }'

7. **get a Loan**

   - request:
   ```bash
   curl http://localhost:8080/loans/{loanId}

7. **Make a Payment**

   - request:
   ```bash
   curl -X POST http://localhost:8080/payments \
   -H "Content-Type: application/json" \
   -d '{
   "loanId": 1,
   "paymentAmount": 100.00
   }'


**Swagger API Documentation**

This project includes Swagger UI for interactive API documentation.

    Once the application is running, access the Swagger UI at:
    http://localhost:8080/swagger-ui.html
    or
    http://localhost:8080/swagger-ui/index.html

    The OpenAPI JSON specification is available at:
    http://localhost:8080/v3/api-docs

    Use the Swagger UI to explore, test, and understand all API endpoints interactively.

**Configuration**

    Database:
    The application uses an embedded H2 database for testing by default. For production, configure your preferred RDBMS (e.g., PostgreSQL, MySQL) in application.properties or application.yml.

    Liquibase:
    Database schema and initial data are managed with Liquibase changelogs.

    Profiles:
    Use spring.profiles.active to switch between environments, e.g., dev, test, prod.

    Port:
    Default port is 8080; can be changed via server.port property.

**Author**

Khanyisani Luyanda Ntabeni