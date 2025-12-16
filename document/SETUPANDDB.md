# Library Management System - Setup and Database Documentation

## 📋 Table of Contents
- [Project Overview](#project-overview)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Database Setup](#database-setup)
- [Application Configuration](#application-configuration)
- [Build and Run](#build-and-run)
- [Database Schema](#database-schema)
- [Entity Relationships](#entity-relationships)

---

## 🎯 Project Overview

**Library Management System (LMS)** is a comprehensive Spring Boot application designed to manage library operations including:
- User authentication and authorization with JWT
- Book catalog management
- Book borrowing and tracking
- Reservation system
- Fine management
- Notification system
- Audit logging

---

## 🛠 Technology Stack

### Backend Framework
- **Spring Boot**: 3.5.0
- **Java Version**: 21

### Core Dependencies
| Dependency | Version | Purpose |
|------------|---------|---------|
| Spring Web | 3.5.0 | REST API & MVC |
| Spring Data JPA | 3.5.0 | Database ORM |
| Spring Security | 3.5.0 | Authentication & Authorization |
| Hibernate | 6.6.15.Final | JPA Implementation |
| JWT (jjwt) | 0.11.5 | JSON Web Token |
| MapStruct | 1.5.5.Final | Object Mapping |
| Lombok | Latest | Boilerplate Code Reduction |
| Flyway | Latest | Database Migration |
| MySQL Connector | Latest | MySQL Driver |
| Thymeleaf | Latest | Template Engine (Optional UI) |
| HikariCP | Built-in | Connection Pooling |

### Build Tool
- **Apache Maven**: 3.x

---

## 📦 Prerequisites

Before setting up the project, ensure you have the following installed:

### Required Software
1. **Java Development Kit (JDK)**
   - Version: 21 or higher
   - Download: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)

2. **Apache Maven**
   - Version: 3.6.x or higher
   - Download: [Maven](https://maven.apache.org/download.cgi)

3. **MySQL Server**
   - Version: 8.0.43 or higher
   - Download: [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)

4. **IDE (Recommended)**
   - IntelliJ IDEA (Ultimate or Community)
   - Eclipse with Spring Tools
   - VS Code with Java Extensions

### System Requirements
- **OS**: Windows, Linux, or macOS
- **RAM**: Minimum 4GB (8GB recommended)
- **Disk Space**: 500MB for application + database storage

---

## 📁 Project Structure

```
LMS/
│
├── src/
│   ├── main/
│   │   ├── java/com/demo/lms/
│   │   │   ├── LmsApplication.java          # Main Application Entry Point
│   │   │   ├── base/
│   │   │   │   └── BaseEntity.java          # Base Entity with common fields
│   │   │   ├── config/                      # Configuration classes
│   │   │   ├── controller/                  # REST Controllers
│   │   │   │   ├── auth/                    # Authentication endpoints
│   │   │   │   ├── book/                    # Book management endpoints
│   │   │   │   └── user/                    # User management endpoints
│   │   │   ├── dto/                         # Data Transfer Objects
│   │   │   │   ├── request/                 # Request DTOs
│   │   │   │   └── response/                # Response DTOs
│   │   │   ├── exception/                   # Exception handling
│   │   │   ├── mapper/                      # MapStruct mappers
│   │   │   ├── model/
│   │   │   │   ├── entity/                  # JPA Entities
│   │   │   │   │   ├── AuditLog.java
│   │   │   │   │   ├── Book.java
│   │   │   │   │   ├── BookCategory.java
│   │   │   │   │   ├── BookCopy.java
│   │   │   │   │   ├── BorrowRecord.java
│   │   │   │   │   ├── Fine.java
│   │   │   │   │   ├── Notification.java
│   │   │   │   │   ├── RefreshToken.java
│   │   │   │   │   ├── Reservation.java
│   │   │   │   │   ├── Role.java
│   │   │   │   │   ├── User.java
│   │   │   │   │   └── UserRole.java
│   │   │   │   └── enums/                   # Enumeration classes
│   │   │   │       ├── BorrowStatus.java
│   │   │   │       ├── NotificationType.java
│   │   │   │       ├── ReservationStatus.java
│   │   │   │       └── UserRoleType.java
│   │   │   ├── repository/                  # Spring Data JPA Repositories
│   │   │   ├── security/
│   │   │   │   ├── jwt/                     # JWT utilities and filters
│   │   │   │   └── test.java
│   │   │   ├── service/                     # Business logic layer
│   │   │   │   ├── auth/                    # Authentication services
│   │   │   │   ├── book/                    # Book services
│   │   │   │   └── user/                    # User services
│   │   │   └── util/                        # Utility classes
│   │   │
│   │   └── resources/
│   │       ├── application.properties       # Application configuration
│   │       ├── banner.txt                   # Spring Boot banner
│   │       ├── db/migration/                # Flyway migration scripts
│   │       │   └── V1__init_schema.sql      # Initial database schema
│   │       ├── static/                      # Static resources
│   │       └── templates/                   # Thymeleaf templates
│   │
│   └── test/
│       └── java/com/demo/lms/
│           └── LmsApplicationTests.java     # Test classes
│
├── logs/
│   └── lms.log                              # Application log file
├── document/                                # Documentation folder
├── Project Report/                          # Project report documents
├── target/                                  # Compiled classes (generated)
├── pom.xml                                  # Maven configuration
├── mvnw                                     # Maven wrapper (Unix)
├── mvnw.cmd                                 # Maven wrapper (Windows)
└── HELP.md                                  # Spring Boot help guide
```

---

## 🗄 Database Setup

### Step 1: Install MySQL

1. Download and install MySQL Server 8.0.43 or higher
2. During installation, set a root password (e.g., `Iloveyouforever@096`)
3. Ensure MySQL service is running

### Step 2: Create Database

Open MySQL command line or MySQL Workbench and execute:

```sql
-- Create the database
CREATE DATABASE IF NOT EXISTS lms_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Verify database creation
SHOW DATABASES LIKE 'lms_db';

-- Use the database
USE lms_db;
```

### Step 3: Verify Database Connection

```sql
-- Check MySQL version
SELECT VERSION();

-- Show current database
SELECT DATABASE();
```

### Step 4: Configure Database User (Optional)

For production environments, create a dedicated database user:

```sql
-- Create a dedicated user for LMS
CREATE USER 'lms_user'@'localhost' IDENTIFIED BY 'secure_password';

-- Grant privileges
GRANT ALL PRIVILEGES ON lms_db.* TO 'lms_user'@'localhost';

-- Apply changes
FLUSH PRIVILEGES;
```

---

## ⚙ Application Configuration

### application.properties

Located at: `src/main/resources/application.properties`

#### Database Configuration

```properties
# Database URL
spring.datasource.url=jdbc:mysql://localhost:3306/lms_db?useSSL=false&serverTimezone=UTC

# Database Credentials
spring.datasource.username=root
spring.datasource.password=Iloveyouforever@096

# JDBC Driver
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

#### JPA/Hibernate Configuration

```properties
# Database Dialect
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

# DDL Mode (update = auto-create/update tables)
spring.jpa.hibernate.ddl-auto=update

# Show SQL queries in console
spring.jpa.show-sql=true

# Disable Open Session In View
spring.jpa.open-in-view=false

# Format SQL output for readability
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true
```

#### Connection Pool (HikariCP)

```properties
# Pool name
spring.datasource.hikari.pool-name=LMS-HikariPool

# Maximum pool size
spring.datasource.hikari.maximum-pool-size=10

# Minimum idle connections
spring.datasource.hikari.minimum-idle=2

# Connection timeouts
spring.datasource.hikari.idle-timeout=30000
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.max-lifetime=1800000
```

#### Flyway Migration

```properties
# Enable Flyway
spring.flyway.enabled=true

# Migration scripts location
spring.flyway.locations=classpath:db/migration

# Baseline on migrate (for existing databases)
spring.flyway.baseline-on-migrate=true
```

#### JWT Security

```properties
# JWT Secret Key
security.jwt.secret=Iloveyouforever@096_youaremypassword

# Token expiration (24 hours in milliseconds)
security.jwt.expiration=86400000
```

#### Server Configuration

```properties
# Application name
spring.application.name=LMS

# Server port
server.port=8080
```

#### Logging Configuration

```properties
# Log levels
logging.level.root=INFO
logging.level.org.springframework.web=INFO
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG

# Log file location
logging.file.name=logs/lms.log

# Log patterns
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

---

## 🚀 Build and Run

### Method 1: Using Maven Wrapper (Recommended)

#### For Windows:
```powershell
# Clean and build the project
.\mvnw.cmd clean install

# Run the application
.\mvnw.cmd spring-boot:run
```

#### For Linux/Mac:
```bash
# Clean and build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

### Method 2: Using Installed Maven

```bash
# Clean and build
mvn clean install

# Run application
mvn spring-boot:run
```

### Method 3: Using IDE

1. **IntelliJ IDEA**
   - Open the project
   - Navigate to `LmsApplication.java`
   - Right-click and select "Run 'LmsApplication'"

2. **Eclipse**
   - Import as Maven project
   - Right-click on project → Run As → Spring Boot App

### Method 4: Running JAR File

```bash
# Build the JAR
mvn clean package

# Run the JAR
java -jar target/LMS-0.0.1-SNAPSHOT.jar
```

### Verify Application is Running

Once started, you should see:
```
Started LmsApplication in X.XXX seconds (process running for X.XXX)
Tomcat started on port 8080 (http) with context path '/'
```

Access the application at: `http://localhost:8080`

---

## 📊 Database Schema

### Overview

The LMS database consists of 12 tables organized into three main domains:

1. **Users & Security**: User authentication and authorization
2. **Library Domain**: Books, categories, and copies management
3. **Borrow & Tracking**: Borrowing records, fines, and notifications

### Entity-Relationship Diagram (Textual)

```
┌─────────────┐         ┌──────────────┐         ┌─────────────┐
│    users    │────────<│  user_roles  │>────────│    roles    │
└─────────────┘         └──────────────┘         └─────────────┘
      │                                                  
      │                                                  
      ├─────────────────────────────────────────┐      
      │                                         │      
      ▼                                         ▼      
┌─────────────────┐                    ┌──────────────┐
│ refresh_tokens  │                    │ audit_logs   │
└─────────────────┘                    └──────────────┘
      
      
┌─────────────────┐         ┌─────────────┐         ┌──────────────┐
│ book_categories │<────────│    books    │────────<│ book_copies  │
└─────────────────┘         └─────────────┘         └──────────────┘
                                   │                        │
                                   │                        │
                                   ▼                        ▼
                            ┌──────────────┐      ┌────────────────┐
                            │ reservations │      │ borrow_records │
                            └──────────────┘      └────────────────┘
                                                          │
                                                          ▼
                                                   ┌─────────┐
                                                   │  fines  │
                                                   └─────────┘
                                                   
┌─────────────────┐
│ notifications   │
└─────────────────┘
```

---

## 📋 Database Tables Details

### 1. Users & Security Tables

#### `users`
Stores user account information.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique user identifier |
| name | VARCHAR(100) | NOT NULL | User's full name |
| email | VARCHAR(150) | NOT NULL, UNIQUE | User's email address |
| password | VARCHAR(255) | NOT NULL | Encrypted password |
| enabled | BOOLEAN | DEFAULT TRUE | Account status |
| created_at | DATETIME | | Account creation timestamp |
| updated_at | DATETIME | | Last update timestamp |

**Indexes:**
- Primary Key on `id`
- Unique Index on `email`

---

#### `roles`
Defines system roles (e.g., ADMIN, LIBRARIAN, MEMBER).

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique role identifier |
| name | VARCHAR(50) | NOT NULL, UNIQUE | Role name |

**Indexes:**
- Primary Key on `id`
- Unique Index on `name`

---

#### `user_roles`
Junction table for many-to-many relationship between users and roles.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier |
| user_id | BIGINT | NOT NULL, FK → users(id) | Reference to user |
| role_id | BIGINT | NOT NULL, FK → roles(id) | Reference to role |

**Constraints:**
- Foreign Key: `user_id` → `users(id)`
- Foreign Key: `role_id` → `roles(id)`
- Unique Constraint: `(user_id, role_id)`

---

#### `refresh_tokens`
Stores JWT refresh tokens for user sessions.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique token identifier |
| token | VARCHAR(255) | NOT NULL, UNIQUE | Refresh token value |
| user_id | BIGINT | NOT NULL, UNIQUE, FK → users(id) | Reference to user |
| expiry_date | DATETIME | NOT NULL | Token expiration date |

**Constraints:**
- Foreign Key: `user_id` → `users(id)`
- Unique Constraint on `token`
- Unique Constraint on `user_id` (one token per user)

---

### 2. Library Domain Tables

#### `book_categories`
Categorizes books (e.g., Fiction, Science, History).

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique category identifier |
| name | VARCHAR(100) | NOT NULL, UNIQUE | Category name |
| description | VARCHAR(255) | | Category description |

**Indexes:**
- Primary Key on `id`
- Unique Index on `name`

---

#### `books`
Master book information (title, author, ISBN).

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique book identifier |
| title | VARCHAR(200) | NOT NULL | Book title |
| author | VARCHAR(150) | NOT NULL | Author name |
| isbn | VARCHAR(50) | UNIQUE | International Standard Book Number |
| category_id | BIGINT | FK → book_categories(id) | Reference to category |
| created_at | DATETIME | | Record creation timestamp |
| updated_at | DATETIME | | Last update timestamp |

**Constraints:**
- Foreign Key: `category_id` → `book_categories(id)`
- Unique Constraint on `isbn`

---

#### `book_copies`
Physical copies of books (a book can have multiple copies).

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique copy identifier |
| book_id | BIGINT | NOT NULL, FK → books(id) | Reference to book |
| copy_code | VARCHAR(100) | UNIQUE | Unique copy barcode/code |
| available | BOOLEAN | DEFAULT TRUE | Availability status |

**Constraints:**
- Foreign Key: `book_id` → `books(id)`
- Unique Constraint on `copy_code`

---

#### `reservations`
Book reservation records when copies are unavailable.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique reservation identifier |
| user_id | BIGINT | NOT NULL, FK → users(id) | Reference to user |
| book_id | BIGINT | NOT NULL, FK → books(id) | Reference to book |
| reservation_date | DATETIME | | Reservation timestamp |
| status | VARCHAR(30) | | Status (PENDING, FULFILLED, CANCELLED) |

**Constraints:**
- Foreign Key: `user_id` → `users(id)`
- Foreign Key: `book_id` → `books(id)`

---

### 3. Borrow & Tracking Tables

#### `borrow_records`
Tracks book borrowing transactions.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique record identifier |
| user_id | BIGINT | NOT NULL, FK → users(id) | Reference to borrower |
| book_copy_id | BIGINT | NOT NULL, FK → book_copies(id) | Reference to book copy |
| borrow_date | DATETIME | | Date book was borrowed |
| due_date | DATETIME | | Date book should be returned |
| return_date | DATETIME | | Actual return date (NULL if not returned) |
| status | VARCHAR(30) | | Status (BORROWED, RETURNED, OVERDUE) |

**Constraints:**
- Foreign Key: `user_id` → `users(id)`
- Foreign Key: `book_copy_id` → `book_copies(id)`

**Business Logic:**
- `return_date` IS NULL → Book still borrowed
- `return_date` > `due_date` → Late return (fine applicable)

---

#### `fines`
Monetary penalties for overdue books.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique fine identifier |
| borrow_record_id | BIGINT | UNIQUE, FK → borrow_records(id) | Reference to borrow record |
| amount | DECIMAL(10,2) | | Fine amount |
| paid | BOOLEAN | DEFAULT FALSE | Payment status |

**Constraints:**
- Foreign Key: `borrow_record_id` → `borrow_records(id)`
- Unique Constraint on `borrow_record_id` (one fine per borrow record)

---

### 4. System Tables

#### `notifications`
User notifications (due dates, reservations, etc.).

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique notification identifier |
| user_id | BIGINT | NOT NULL, FK → users(id) | Reference to user |
| message | VARCHAR(255) | | Notification message |
| read_flag | BOOLEAN | DEFAULT FALSE | Read status |
| created_at | DATETIME | | Creation timestamp |

**Constraints:**
- Foreign Key: `user_id` → `users(id)`

---

#### `audit_logs`
System audit trail for tracking user actions.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique log identifier |
| user_id | BIGINT | FK → users(id) | Reference to user |
| action | VARCHAR(255) | | Action description |
| created_at | DATETIME | | Action timestamp |

**Constraints:**
- Foreign Key: `user_id` → `users(id)`

---

## 🔗 Entity Relationships

### Key Relationships

1. **User ↔ Role** (Many-to-Many)
   - Junction Table: `user_roles`
   - A user can have multiple roles
   - A role can be assigned to multiple users

2. **User ↔ RefreshToken** (One-to-One)
   - One user has one refresh token
   - Token links back to single user

3. **BookCategory ↔ Book** (One-to-Many)
   - One category contains many books
   - Each book belongs to one category

4. **Book ↔ BookCopy** (One-to-Many)
   - One book can have multiple physical copies
   - Each copy belongs to one book

5. **User ↔ BorrowRecord** (One-to-Many)
   - One user can borrow multiple books
   - Each borrow record belongs to one user

6. **BookCopy ↔ BorrowRecord** (One-to-Many)
   - One copy can have multiple borrow records (over time)
   - Each borrow record is for one specific copy

7. **BorrowRecord ↔ Fine** (One-to-One)
   - One borrow record can have one fine
   - Each fine is associated with one borrow record

8. **User ↔ Reservation** (One-to-Many)
   - One user can make multiple reservations
   - Each reservation belongs to one user

9. **User ↔ Notification** (One-to-Many)
   - One user can receive multiple notifications
   - Each notification belongs to one user

10. **User ↔ AuditLog** (One-to-Many)
    - One user can have multiple audit entries
    - Each audit entry belongs to one user

---

## 🔧 Database Migration with Flyway

### Migration Strategy

The project uses **Flyway** for database version control and migrations.

#### Migration Files Location
```
src/main/resources/db/migration/
```

#### Naming Convention
```
V{version}__{description}.sql
```

Example: `V1__init_schema.sql`

#### Current Migrations

**V1__init_schema.sql** - Initial schema creation
- Creates all 12 tables
- Defines all relationships and constraints
- Sets up indexes

### Running Migrations

Migrations run automatically on application startup when:
```properties
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
```

#### Manual Migration Control

To disable automatic migration:
```properties
spring.flyway.enabled=false
```

To run migration manually:
```bash
mvn flyway:migrate
```

#### Flyway Commands

```bash
# View migration status
mvn flyway:info

# Validate migrations
mvn flyway:validate

# Repair migration history
mvn flyway:repair

# Clean database (⚠️ CAUTION: Drops all objects)
mvn flyway:clean
```

---

## 🔐 Security Configuration

### Authentication Flow

1. **User Registration**
   - User submits credentials
   - Password is encrypted (BCrypt)
   - User record created in `users` table
   - Default role assigned via `user_roles`

2. **User Login**
   - User submits email/password
   - Credentials validated against database
   - JWT access token generated
   - Refresh token created and stored

3. **Token Management**
   - Access Token: Short-lived (24 hours)
   - Refresh Token: Long-lived, stored in database
   - Token contains user ID and roles

4. **Authorization**
   - JWT token sent in Authorization header
   - Spring Security validates token
   - User roles checked for endpoint access

### Password Encryption

- Algorithm: **BCrypt**
- Managed by: Spring Security
- Strength: Default (10 rounds)

---

## 📈 Connection Pool Configuration

### HikariCP Settings

```properties
# Pool Configuration
spring.datasource.hikari.pool-name=LMS-HikariPool
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2

# Timeouts (in milliseconds)
spring.datasource.hikari.idle-timeout=30000       # 30 seconds
spring.datasource.hikari.connection-timeout=20000 # 20 seconds
spring.datasource.hikari.max-lifetime=1800000     # 30 minutes
```

### Performance Tuning

For production environments, adjust based on load:

```properties
# High traffic configuration
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
```

---

## 🧪 Testing Database Connection

### Using Application Logs

Check `logs/lms.log` for:
```
HikariPool - LMS-HikariPool - Start completed.
Database version: 8.0.43
```

### SQL Test Queries

After application starts, verify tables:

```sql
USE lms_db;

-- Show all tables
SHOW TABLES;

-- Verify table structure
DESCRIBE users;
DESCRIBE books;
DESCRIBE borrow_records;

-- Check Flyway migration history
SELECT * FROM flyway_schema_history;
```

---

## 🚨 Common Issues and Troubleshooting

### Issue 1: Database Connection Failed

**Error:**
```
Communications link failure
```

**Solution:**
- Verify MySQL service is running
- Check database credentials in `application.properties`
- Ensure database `lms_db` exists
- Verify port 3306 is not blocked

### Issue 2: Flyway Migration Failed

**Error:**
```
FlywayException: Validate failed
```

**Solution:**
```bash
# Repair Flyway history
mvn flyway:repair

# Or clean and rebuild
mvn flyway:clean
mvn flyway:migrate
```

### Issue 3: Port 8080 Already in Use

**Solution:**
Change port in `application.properties`:
```properties
server.port=8081
```

### Issue 4: JPA Entity Not Found

**Solution:**
- Ensure entity class has `@Entity` annotation
- Verify entity package is scanned:
  ```java
  @EntityScan("com.demo.lms.model.entity")
  ```

---

## 📝 Initial Data Setup (Optional)

To populate initial data, create a new migration file:

**V2__insert_initial_data.sql**

```sql
-- Insert default roles
INSERT INTO roles (id, name) VALUES
(1, 'ADMIN'),
(2, 'LIBRARIAN'),
(3, 'MEMBER');

-- Insert book categories
INSERT INTO book_categories (id, name, description) VALUES
(1, 'Fiction', 'Fiction books'),
(2, 'Science', 'Science and technology books'),
(3, 'History', 'Historical books'),
(4, 'Biography', 'Biographical works');

-- Insert sample admin user (password: admin123)
INSERT INTO users (id, name, email, password, enabled, created_at, updated_at) VALUES
(1, 'System Admin', 'admin@lms.com', 
 '$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 
 TRUE, NOW(), NOW());

-- Assign admin role
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
```

---

## 🔄 Backup and Restore

### Database Backup

```bash
# Full backup
mysqldump -u root -p lms_db > lms_backup_$(date +%Y%m%d).sql

# Backup structure only
mysqldump -u root -p --no-data lms_db > lms_structure.sql

# Backup data only
mysqldump -u root -p --no-create-info lms_db > lms_data.sql
```

### Database Restore

```bash
# Restore from backup
mysql -u root -p lms_db < lms_backup_20251217.sql
```

---

## 📊 Performance Monitoring

### Enable SQL Statistics

```properties
# Show SQL execution time
spring.jpa.properties.hibernate.generate_statistics=true

# Log slow queries (in milliseconds)
spring.jpa.properties.hibernate.session.events.log.LOG_QUERIES_SLOWER_THAN_MS=100
```

### Database Indexes

Recommended indexes for performance:

```sql
-- Index on frequently queried columns
CREATE INDEX idx_book_title ON books(title);
CREATE INDEX idx_book_author ON books(author);
CREATE INDEX idx_borrow_status ON borrow_records(status);
CREATE INDEX idx_borrow_user ON borrow_records(user_id);
CREATE INDEX idx_borrow_copy ON borrow_records(book_copy_id);
```

---

## 📚 Additional Resources

### Documentation
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [MySQL 8.0 Reference](https://dev.mysql.com/doc/refman/8.0/en/)

### Tools
- [MySQL Workbench](https://www.mysql.com/products/workbench/) - Database design and administration
- [DBeaver](https://dbeaver.io/) - Universal database tool
- [Postman](https://www.postman.com/) - API testing

---

## 📧 Support

For issues or questions:
- Check application logs: `logs/lms.log`
- Review database logs: MySQL error log
- Consult Spring Boot documentation

---

**Last Updated:** December 17, 2025  
**Version:** 1.0  
**Database Schema Version:** V1

