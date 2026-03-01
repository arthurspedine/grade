# Grade - Assessment Management System (Backend)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> **Note:** This is the **backend** repository. The frontend application is available in a separate repository: [Grade Frontend Repository](https://github.com/arthurspedine/grade-frontend)

---

## 🎯 Overview

**Grade** is a comprehensive assessment management system designed to streamline the process of creating, managing, and evaluating student assessments. This repository contains the **backend REST API** built with Spring Boot that powers the Grade platform.

### Key Features

- **Assessment Management** — Create, edit, and organize assessments across multiple classes
- **Multi-Category Evaluation** — Evaluate students across multiple competency categories per question
- **AI-Powered Feedback** — Generate personalized feedback using OpenAI integration
- **PDF Export** — Export individual student evaluations as professionally formatted PDF reports
- **Dashboard Analytics** — View comprehensive performance metrics and insights
- **Class Management** — Organize students into classes with CSV import support
- **Smart Student Sync** — Automatically syncs student rosters with not-started assessments when classes are edited
- **Secure Authentication** — OAuth2-based authentication and authorization

---

## 📄 PDF Export Feature

The application includes a robust PDF generation system that allows educators to export detailed student evaluation reports. Each PDF includes:

- **Student Information** — Name, RM (student ID), class, and assessment details
- **Performance Breakdown** — Question-by-question analysis with category scores
- **Visual Progress Indicators** — Color-coded progress bars showing performance levels
  - 🟢 **Green:** ≥70% (Good performance)
  - 🟠 **Orange:** 50–69% (Moderate performance)
  - 🔴 **Red:** <50% (Needs improvement)
- **AI-Generated Feedback** — Personalized feedback based on student performance
- **Multi-page Support** — Automatically handles content overflow across multiple pages

---

## 🛠️ Technology Stack

| Layer            | Technology                                |
|------------------|-------------------------------------------|
| Language         | Java 17                                   |
| Framework        | Spring Boot 3.4.0                         |
| Security         | Spring Security + OAuth2 Authorization    |
| Persistence      | Spring Data JPA + MySQL 8.0               |
| Migrations       | Flyway                                    |
| AI Integration   | Spring AI + OpenAI                        |
| PDF Generation   | Apache PDFBox 3.0.3                       |
| CSV Processing   | OpenCSV                                   |
| Monitoring       | New Relic                                 |
| Containerization | Docker & Docker Compose                   |

---

## 📦 Prerequisites

- Java 17 or higher
- MySQL 8.0+
- Docker and Docker Compose (optional)
- OpenAI API key (for AI feedback features)

---

## ⚙️ Installation

### 1. Clone the Repository

```bash
git clone https://github.com/arthurspedine/grade.git grade-backend
cd grade-backend
```

### 2. Environment Configuration

Create the following environment variables or update the application configuration:

```bash
export GRADE_DB_URL=jdbc:mysql://localhost:3306/grade
export GRADE_DB_USER=your_db_username
export GRADE_DB_PASSWORD=your_db_password
export GRADE_OPENAI_KEY=your_openai_api_key  # or set "none" if not using OpenAI
export PORT=3010
```

### 3. Database Setup

Ensure MySQL is running and create the database:

```sql
CREATE DATABASE grade CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 4. Run the Application

#### Using Gradle Wrapper (Recommended)

```bash
./gradlew bootRun
```

#### Using Docker Compose (Development)

```bash
docker-compose -f docker-compose.dev.yml up
```

#### Using Docker Compose (Production)

> **Important:** Make sure to set the frontend environment variables before starting the containers.

```bash
export GOOGLE_CLIENT_ID=your_google_client_id
export GOOGLE_CLIENT_SECRET=your_google_client_secret
export NEXTAUTH_SECRET=your_nextauth_secret
export NEXTAUTH_URL=http://localhost:3000
export BACKEND_URL=http://localhost:3010
```

Then start the Docker containers:

```bash
docker-compose up -d
```

The application will start on `http://localhost:3010`.

---

## 📝 API Documentation

API endpoints are organized by domain:

| Prefix         | Description                      |
|----------------|----------------------------------|
| `/classes`     | Class management operations      |
| `/students`    | Student administration           |
| `/assessments` | Assessment CRUD & edit-info      |
| `/evaluate`    | Student evaluation operations    |
| `/dashboard`   | Dashboard & analytics endpoints  |

---

## 🏗️ Project Structure

```
src/main/java/com/use3w/grade/
├── controller/        # REST controllers
├── dto/               # Data Transfer Objects (request/response)
├── infra/             # Infrastructure (security, exception handling)
├── model/             # JPA entities
├── projection/        # Spring Data projections
├── repository/        # Spring Data JPA repositories
├── service/           # Business logic layer
└── util/              # Utilities (CSV reader, PDF writer, auth)
```

---

## 🗺️ Roadmap

- [x] **Phase 1 — Initial Release**
  - [x] CSV Import
  - [x] Class Management
  - [x] Student Administration
  - [x] Assessment Creation
  - [x] Basic Grading System
  - [x] Database Migration with Flyway
- [x] **Phase 2 — Advanced Features**
  - [x] Multi-Category Evaluation
  - [x] OAuth2 Authentication
  - [x] AI-Powered Feedback with OpenAI
  - [x] PDF Export Functionality
- [ ] **Phase 3 — Architecture & Improvements**
  - [ ] Adjust to **Clean Architecture**
  
*This roadmap will be updated with specific features and milestones.*

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

## 🆘 Support

If you encounter any issues or have questions:

1. Check the [Issues](https://github.com/arthurspedine/grade/issues) page
2. Create a new issue with detailed information about your problem
3. Include relevant error messages and system information