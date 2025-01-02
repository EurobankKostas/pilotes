# Pilotes Order Manager

This project provides a **Spring Boot** application to manage orders of “Pilotes”, a Majorcan recipe popularized by 
Miquel Montoro.  
It demonstrates **hexagonal architecture** (ports & adapters), basic validations, and a secured endpoint for searching orders.

---

## Requirements

1. **Java 21** (Recommended)
2. **Docker & Docker Compose**

### Technical Stack
- **Spring Boot 3.x** (Web, Data JPA, Validation, Security)
- **H2 in-memory database** (via Flyway for migrations)
- **Lombok** (for boilerplate-free data classes)
- **SpringDoc/OpenAPI** (for Swagger UI documentation)

---

## How to Run Locally

Open a terminal and run the following commands:
   ```bash
   git clone <repo-url>
   cd <repo-folder>
   docker compose up
```

## Swagger UI for local
http://localhost:8080/pilotes/api/swagger-ui/index.html

## H2 Console for local
http://localhost:8080/pilotes/api/h2-console/login.do
jdbc url : jdbc:h2:mem:pilotesdb
user : sa
---

