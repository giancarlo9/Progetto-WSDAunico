# Progetto WSDA - Workshop Services Management Platform

A robust, enterprise-grade full-stack web application designed for comprehensive auto repair shop and workshop operations. The platform provides secure, role-based workflows for managing customer records, vehicle tracking, maintenance interventions, and staff authentication.

## 🚀 Key Features

* **Role-Based Access Control (RBAC):** Secure authentication and authorization powered by Spring Security, featuring custom login handling (`SecurityConfig`, `CustomLoginSuccessHandler`) to manage permissions across different staff roles.
* **Customer & Vehicle Lifecycle Management:** Complete backend services and frontend interfaces to register, track, and update client profiles and associated vehicle records.
* **Maintenance Interventions Tracking:** Detailed logging, tracking, and status updates (`Stato`) for workshop repairs and servicing tasks.
* **RESTful Architecture:** Clean separation of concerns with a Spring Boot backend exposing well-defined controllers and DTO payloads.
* **Interactive Frontend Dashboards:** Modular HTML5 dashboards and Vanilla JavaScript client interfaces communicating asynchronously with backend endpoints via the `fetch` API.

## 🏗️ Tech Stack & Architecture

### Backend
* **Core Framework:** Spring Boot (Java)
* **Security & Auth:** Spring Security
* **Persistence & ORM:** Spring Data JPA / Hibernate
* **Error Handling:** Global exception handler (`ExceptionsHandler`) with custom exceptions (`BadRequestException`, `NotFoundException`, `UnauthorizedException`)
* **Data Transfer Objects (DTOs):** Structured request/response payloads for entities (`Cliente`, `Veicolo`, `Intervento`, `Utente`)

### Frontend & UI
* **Entry Page:** `index.html`
* **Role Dashboards:** Dedicated operational views including Acceptance (`accettazione-dashboard.html`), Mechanic (`meccanico-dashboard.html`), Warehouse (`magazziniere-dashboard.html`), and Cashier (`cassiere-dashboard.html`)
* **Styling & Client Logic:** CSS3 (`styles.css`) and modular JavaScript scripts

### Testing & Tools
* **API Testing:** Complete Postman collection included (`WSDA.postman_collection.json`)
* **Build Tool:** Maven

---

## 📁 Project Structure

```text
Progetto-WSDAunico-main/
├── src/main/java/giannonegiancarlo/Progetto/WSDA/
│   ├── controllers/      # REST Controllers (Auth, Clienti, Interventi, Veicoli, Utenti)
│   ├── entities/         # JPA Domain Models (Cliente, Intervento, Utente, Veicolo)
│   ├── enums/            # System Enums (Ruolo, Stato)
│   ├── exceptions/       # Global Exception Handler & Custom Exceptions
│   ├── payloads/         # DTOs and Request/Response Payloads
│   ├── repositories/     # Spring Data JPA DAOs
│   ├── security/         # Security Configuration and Custom Success Handlers
│   └── services/         # Business Logic Layer
├── src/main/resources/
│   ├── static/           # Frontend assets, index.html, role dashboards, and JS scripts
│   └── application.properties
├── pom.xml               # Maven configuration
└── WSDA.postman_collection.json # Postman API collection
```

---

## 🗄️ Core Domain Entities

1. **Cliente (Customer):** Stores client contact details and links to multiple vehicles.
2. **Veicolo (Vehicle):** Tracks license plates, models, and specifications linked to specific clients.
3. **Intervento (Intervention):** Records maintenance descriptions, notes, and progress states (`Stato`).
4. **Utente (User):** Manages staff credentials and role assignments (`Ruolo`).

---

## ⚙️ Getting Started & Local Setup

### Prerequisites
* Java JDK (17 or higher)
* Maven
* Relational Database (e.g., MySQL / PostgreSQL)

### Installation & Execution
1. Clone the repository:
   ```bash
   git clone https://github.com/giancarlo9/Progetto-WSDAunico-main.git
   ```
2. Configure your database connection properties in `src/main/resources/application.properties`.
3. Build and run the project using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Access the application locally in your browser at `http://localhost:8080`.

---

## 🧪 API Testing
You can test backend REST endpoints, payload validations, and security workflows directly using the provided Postman collection located at the root of the repository: `WSDA.postman_collection.json`.

---

## 👨‍💻 Author
* **Giancarlo Giannone** — *Computer Engineering Graduate*