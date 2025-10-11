# Grade - Assessment Management System (Backend)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
> **Note**: This is the backend repository. The frontend application is available in a separate repository: [Grade Frontend Repository](https://github.com/tiago-ferrer/grade-frontend)

## 🚀 Live Application

**Access the application online**: [https://grade.use3w.com](https://grade.use3w.com)

## 🎯 Overview

Grade is a comprehensive assessment management system designed to streamline the process of creating, managing, and evaluating student assessments. This repository contains the **backend API** built with Spring Boot that powers the Grade platform.


### Key Features
- **Assessment Management**: Create and organize assessments for different classes
- **Multi-Category Evaluation**: Evaluate students across multiple competency categories per question
- **AI-Powered Feedback**: Generate personalized feedback using OpenAI integration
- **PDF Export**: Export individual student evaluations as professionally formatted PDF reports
- **Dashboard Analytics**: View comprehensive performance metrics and insights
- **Class Management**: Organize students into classes and manage their assessments
- **Secure Authentication**: OAuth2-based authentication and authorization

## 🚀 PDF Export Feature

The application includes a robust PDF generation system that allows educators to export detailed student evaluation reports. Each PDF includes:

- **Student Information**: Name, RM (student ID), class, and assessment details
- **Performance Breakdown**: Question-by-question analysis with category scores
- **Visual Progress Indicators**: Color-coded progress bars showing performance levels
  - 🟢 Green: ≥70% (Good performance)
  - 🟠 Orange: 50-69% (Moderate performance)
  - 🔴 Red: <50% (Needs improvement)
- **AI-Generated Feedback**: Personalized feedback based on student performance
- **Multi-page Support**: Automatically handles content overflow across multiple pages
- 
## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.4.0**
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - OAuth2 Authorization Server
- **Spring AI** with OpenAI integration
- **MySQL** database
- **Flyway** for database migrations
- **Apache PDFBox 3.0.3** for PDF generation
- **OpenCSV** for CSV file processing
- **Docker** and Docker Compose for containerization

## 📦 Prerequisites

- Java 17 or higher
- MySQL 8.0+
- Docker and Docker Compose (optional)
- OpenAI API key (for AI feedback features)

## ⚙️ Installation

## 📝 API Documentation

API endpoints are organized by domain:

- `/classes` - Class management operations
- `/students` - Student administration
- `/assessments` - Assessment operations
- `/evaluate` - Evaluate Assessment operations
- `/dashboard` - Dashboard endpoints

## 🗺️ Roadmap
- [x] Phase 1 - Initial Release
  - [x] CSV Import
  - [x] Class Management
  - [x] Student Administration
  - [x] Assessment Creation
  - [x] Basic Grading System
  - [x] Database Migration with Flyway
- [x] Phase 2 - Advanced Features
  - [x] Multi-Category Evaluation
  - [x] OAuth2 Authentication
  - [x] AI-Powered Feedback with OpenAI
  - [x] PDF Export Functionality
- [ ] Phase 3 - Architecture & Foundations
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