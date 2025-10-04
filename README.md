# Grade - Assessment Management System (Backend)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 🎯 Overview

Grade is a comprehensive assessment management system designed to streamline the process of creating, managing, and evaluating student assessments. This repository contains the **backend API** built with Spring Boot that powers the Grade platform.

> **Note**: This is the backend repository. The frontend application is available in a separate repository: [Grade Frontend Repository](https://github.com/tiago-ferrer/grade-frontend)

## 🚀 Live Application

**Access the application online**: [https://grade.use3w.com](https://grade.use3w.com)

## ✨ Features

- **Class Management**: Create and manage educational classes
- **Student Administration**: Handle student enrollment and data
- **Assessment Creation**: Design and configure assessments with multiple question types
- **AI-Powered Feedback**: Integrated OpenAI for intelligent assessment feedback
- **Real-time Grading**: Automated scoring and manual review capabilities
- **Secure Authentication**: OAuth2 authorization server implementation
- **Database Migration**: Flyway-managed database versioning

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.4.0
- **Language**: Java 17
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA with Hibernate
- **Migration**: Flyway
- **Security**: Spring Security with OAuth2
- **AI Integration**: Spring AI with OpenAI
- **Build Tool**: Gradle
- **Containerization**: Docker & Docker Compose
- **Monitoring**: New Relic APM

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **Java 17** or higher
- **MySQL 8.0** or higher
- **Docker & Docker Compose** (optional, for containerized deployment)
- **Gradle** (or use the included Gradle wrapper)

## 🔧 Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/tiago-ferrer/grade.git grade-backend
cd grade-backend
```

### 2. Environment Configuration

Create the following environment variables or update the application configuration:

```bash
export GRADE_DB_URL=jdbc:mysql://localhost:3306/grade
export GRADE_DB_USER=your_db_username
export GRADE_DB_PASSWORD=your_db_password
export GRADE_OPENAI_KEY=your_openai_api_key # or set "none" if not using OpenAI
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

```bash
docker-compose up -d
```

The application will start on `http://localhost:3010`

## 📊 Database Schema

The application uses Flyway for database migrations. Key entities include:

- **Classes**: Educational class management
- **Students**: Student information and enrollment
- **Assessments**: Assessment configuration and metadata
- **Assessment Questions**: Individual questions within assessments
- **Assessment Students**: Student participation tracking
- **Assessment Answers**: Student responses and scoring

## 🔐 API Authentication

The application implements OAuth2 authorization. Ensure proper authentication headers are included in API requests:

```bash
Authorization: Bearer <your-access-token>
```

## 📝 API Documentation

API endpoints are organized by domain:

- `/classes` - Class management operations
- `/students` - Student administration
- `/assessments` - Assessment operations
- `/evaluate` - Evaluate Assessment operations
- `/dashboard` - Dashboard endpoints

## 🗺️ Roadmap
- [x] Phase 1 - Initial Release
  - [x] Class Management
  - [x] Student Administration
  - [x] Assessment Creation
  - [x] Basic Grading System
  - [x] OAuth2 Authentication
  - [x] Database Migration with Flyway
- [ ] Phase 2 - Architecture & Foundations
  - [ ] Adjust to **Clean Architecture**

*This roadmap will be updated with specific features and milestones.*

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

If you encounter any issues or have questions:

1. Check the [Issues](https://github.com/tiago-ferrer/grade/issues) page
2. Create a new issue with detailed information about your problem
3. Include relevant error messages and system information