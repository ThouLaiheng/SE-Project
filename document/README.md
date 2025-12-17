# Library Management System - Documentation Index

Welcome to the Library Management System documentation! This directory contains comprehensive documentation for the LMS project.

## 📚 Documentation Files

### 1. [QUICK_START.md](QUICK_START.md) ⚡ NEW!
**Get started in 5 minutes**

Contains:
- Prerequisites checklist
- 5-minute setup guide
- First API test examples
- Common troubleshooting
- Quick reference for all endpoints

**Who should read this**: Everyone! Start here for the fastest setup experience.

---

### 2. [SETUPANDDB.md](SETUPANDDB.md)
**Complete setup and database configuration guide**

Contains detailed information about:
- Project overview and technology stack
- Prerequisites and system requirements
- Project structure
- Database setup and configuration
- Application properties
- Build and deployment instructions
- Database schema and relationships
- Migration scripts

**Who should read this**: Developers setting up the project for the first time, DevOps engineers, System administrators

---

### 3. [ERROR_HANDLING.md](ERROR_HANDLING.md)
**Comprehensive error handling documentation**

Contains detailed information about:
- Error response structure
- Exception types and handling
- HTTP status codes
- Error examples for all scenarios
- Best practices for error handling
- Testing error scenarios
- Implementation details

**Who should read this**: Backend developers, API consumers, QA engineers, Frontend developers

---

### 4. [USER_API.md](USER_API.md)
**Complete User API reference guide**

Contains detailed information about:
- API endpoints (Create, Read, List users)
- Request/response formats
- Validation rules
- Error responses
- Code examples in multiple languages (JavaScript, Python, Java)
- Postman test collection
- Integration examples

**Who should read this**: Frontend developers, API consumers, Mobile app developers, Integration partners

---

### 5. [HELP.md](HELP.md)
**Quick reference and external resources**

Contains:
- Spring Boot official documentation links
- Maven references
- Technology stack guides
- External learning resources

**Who should read this**: Everyone, especially those new to Spring Boot

---

## 🚀 Quick Start Guide

### New to the Project? Start Here! ⚡
**[QUICK_START.md](QUICK_START.md)** - Get up and running in 5 minutes!

### For Developers Setting Up the Project
1. Follow the [QUICK_START.md](QUICK_START.md) for fastest setup
2. Or read [SETUPANDDB.md](SETUPANDDB.md) for detailed setup instructions
3. Configure your database according to the setup guide
4. Run the application and test the setup

### For API Consumers
1. Review [USER_API.md](USER_API.md) for API endpoint documentation
2. Check [ERROR_HANDLING.md](ERROR_HANDLING.md) to understand error responses
3. Use the provided code examples for integration

### For QA/Testing Teams
1. Read [USER_API.md](USER_API.md) for API specifications
2. Study [ERROR_HANDLING.md](ERROR_HANDLING.md) for test cases
3. Use the Postman collection from the User API documentation

---

## 📋 Project Information

### Current Features
- ✅ User management (Create, Read, List)
- ✅ Global error handling
- ✅ Input validation
- ✅ Database integration with MySQL
- ✅ Role-based user system (STUDENT role by default)
- ✅ Password encryption
- ✅ Audit logging (createdAt, updatedAt)

### Technology Stack
- **Backend**: Spring Boot 3.5.0
- **Java**: 21
- **Database**: MySQL 8.0.43
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Maven
- **Security**: Spring Security (password encryption)
- **Validation**: Jakarta Bean Validation

---

## 🔧 Key Components

### Controllers
- **UserController**: Manages user-related HTTP requests
  - Location: `src/main/java/com/demo/lms/controller/user/`

### Services
- **UserService**: Business logic for user operations
  - Location: `src/main/java/com/demo/lms/service/user/`

### Exception Handling
- **GlobalExceptionHandler**: Centralized error handling
  - Location: `src/main/java/com/demo/lms/exception/`

### DTOs
- **Request DTOs**: Data transfer objects for incoming requests
  - Location: `src/main/java/com/demo/lms/dto/request/`
- **Response DTOs**: Data transfer objects for responses
  - Location: `src/main/java/com/demo/lms/dto/response/`

### Entities
- **User**: User entity with JPA mappings
- **Role**: Role entity for authorization
- **UserRole**: Many-to-many relationship between users and roles
  - Location: `src/main/java/com/demo/lms/model/entity/`

---

## 📖 API Endpoints Summary

### User Management
| Method | Endpoint | Description | Status Code |
|--------|----------|-------------|-------------|
| POST | `/api/users` | Create new user | 201 CREATED |
| GET | `/api/users` | Get all users | 200 OK |
| GET | `/api/users/{id}` | Get user by ID | 200 OK |

For detailed API documentation, see [USER_API.md](USER_API.md)

---

## ❌ Error Handling Summary

### HTTP Status Codes
| Code | Status | Description |
|------|--------|-------------|
| 200 | OK | Successful GET request |
| 201 | CREATED | Resource created successfully |
| 400 | BAD REQUEST | Invalid request or validation failure |
| 404 | NOT FOUND | Resource not found |
| 409 | CONFLICT | Resource already exists |
| 500 | INTERNAL SERVER ERROR | Unexpected server error |

For detailed error handling documentation, see [ERROR_HANDLING.md](ERROR_HANDLING.md)

---

## 🗄️ Database Schema Overview

### Tables
1. **users**: Stores user account information
2. **roles**: Stores available roles (ADMIN, LIBRARIAN, STUDENT)
3. **user_roles**: Junction table for user-role relationships

### Entity Relationships
- User ↔ Role: Many-to-Many (through user_roles)
- All entities extend BaseEntity (id, createdAt, updatedAt)

For complete database documentation, see [SETUPANDDB.md](SETUPANDDB.md)

---

## 🧪 Testing

### Manual Testing
Use tools like:
- **Postman**: Import collection from [USER_API.md](USER_API.md)
- **cURL**: Copy commands from API documentation
- **Browser**: For GET endpoints

### Automated Testing
```bash
# Run all tests
mvnw test

# Run specific test class
mvnw test -Dtest=UserControllerTest
```

---

## 🔍 Common Issues and Solutions

### Issue: Database Connection Failed
**Solution**: Check `application.properties` for correct database credentials and ensure MySQL is running

### Issue: Port 8080 Already in Use
**Solution**: Either stop the process using port 8080 or change the port in `application.properties`:
```properties
server.port=8081
```

### Issue: Build Failed
**Solution**: Clean and rebuild:
```bash
mvnw clean install
```

### Issue: Validation Errors
**Solution**: Check [USER_API.md](USER_API.md) for validation rules and ensure request body meets requirements

---

## 📦 Dependencies

### Core Dependencies
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-validation
- mysql-connector-java
- lombok
- mapstruct

For complete list, see `pom.xml` or [SETUPANDDB.md](SETUPANDDB.md)

---

## 🔐 Security Notes

### Current Implementation
- Passwords are encrypted using BCrypt
- Default role: STUDENT
- JWT authentication: Not yet implemented

### Future Enhancements
- JWT-based authentication
- Role-based access control (RBAC)
- OAuth2 integration
- Session management

---

## 📝 Development Guidelines

### Code Style
- Follow Java naming conventions
- Use Lombok to reduce boilerplate
- Document public APIs with JavaDoc
- Write meaningful commit messages

### Error Handling
- Use custom exceptions for domain errors
- Never expose sensitive information in error messages
- Log errors appropriately
- See [ERROR_HANDLING.md](ERROR_HANDLING.md) for best practices

### API Design
- Follow RESTful principles
- Use appropriate HTTP methods
- Return proper status codes
- Validate all inputs
- See [USER_API.md](USER_API.md) for examples

---

## 🚧 Future Features (Roadmap)

### Phase 1: Core Features
- [x] User management
- [x] Error handling
- [ ] JWT authentication
- [ ] Book management
- [ ] Borrowing system

### Phase 2: Advanced Features
- [ ] Fine management
- [ ] Reservation system
- [ ] Notification system
- [ ] Reporting and analytics

### Phase 3: Enhancements
- [ ] Email notifications
- [ ] SMS alerts
- [ ] Mobile app support
- [ ] Admin dashboard

---

## 🤝 Contributing

### Getting Started
1. Read [SETUPANDDB.md](SETUPANDDB.md) for environment setup
2. Familiarize yourself with existing code structure
3. Follow coding standards and best practices

### Before Committing
1. Run all tests: `mvnw test`
2. Ensure code compiles: `mvnw clean compile`
3. Update documentation if adding new features
4. Write meaningful commit messages

---

## 📞 Support and Contact

### Documentation Issues
If you find any issues with the documentation:
1. Check if the information is outdated
2. Verify against the actual codebase
3. Report issues to the development team

### Technical Support
For technical issues:
1. Check relevant documentation file
2. Review error messages and logs
3. Search for similar issues
4. Contact the development team

---

## 📚 External Resources

### Official Documentation
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Maven Documentation](https://maven.apache.org/guides/)

### Learning Resources
- [Spring Boot Guides](https://spring.io/guides)
- [REST API Best Practices](https://restfulapi.net/)
- [Java Coding Standards](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html)

---

## 📄 License

This project is for educational purposes as part of the Software Engineering course.

---

## 🎓 Academic Information

**Course**: Software Engineering  
**Project**: Library Management System  
**Academic Year**: 2024-2025 (Semester I, Year 4)

---

## 📋 Document Changelog

| Date | Version | Changes | Author |
|------|---------|---------|--------|
| 2025-12-17 | 1.0.0 | Initial documentation | Development Team |
| 2025-12-17 | 1.1.0 | Added error handling documentation | Development Team |
| 2025-12-17 | 1.2.0 | Added User API documentation | Development Team |
| 2025-12-17 | 1.3.0 | Added README index | Development Team |

---

## ✅ Documentation Checklist

- [x] Setup and database configuration
- [x] Error handling guide
- [x] User API documentation
- [x] Code examples in multiple languages
- [x] Postman test collection
- [x] Common issues and solutions
- [ ] Authentication documentation (pending JWT implementation)
- [ ] Book API documentation (pending implementation)
- [ ] Borrowing API documentation (pending implementation)

---

**Last Updated**: December 17, 2025  
**Version**: 1.3.0  
**Maintained by**: LMS Development Team

---

## Quick Navigation

- [Quick Start](QUICK_START.md) - ⚡ 5-minute setup guide
- [Setup Guide](SETUPANDDB.md) - Detailed project setup
- [Error Handling](ERROR_HANDLING.md) - Understand error responses
- [User API](USER_API.md) - Complete API reference
- [Help & Resources](HELP.md) - External learning resources

