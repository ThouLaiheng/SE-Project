# Quick Start Guide - Library Management System

Get up and running with the Library Management System in minutes!

## ⚡ Prerequisites Checklist

Before you begin, ensure you have:

- [ ] **Java 21** installed ([Download](https://www.oracle.com/java/technologies/downloads/))
- [ ] **MySQL 8.0.43+** installed and running ([Download](https://dev.mysql.com/downloads/mysql/))
- [ ] **Maven 3.6+** installed (or use included `mvnw`)
- [ ] **IDE** installed (IntelliJ IDEA recommended)

---

## 🚀 5-Minute Setup

### Step 1: Clone or Open the Project (30 seconds)

```bash
cd "D:\YEAR_4_Documents\YEAR_4_Documents\Semester_I\SE (Software Engineering)\Library Management System\LMS"
```

### Step 2: Create Database (1 minute)

Open MySQL command line or MySQL Workbench and run:

```sql
CREATE DATABASE lms_db;
```

### Step 3: Configure Database Connection (1 minute)

Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/lms_db
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Step 4: Build the Project (2 minutes)

**Windows (PowerShell)**:
```powershell
.\mvnw.cmd clean install
```

**Linux/Mac**:
```bash
./mvnw clean install
```

### Step 5: Run the Application (30 seconds)

**Windows (PowerShell)**:
```powershell
.\mvnw.cmd spring-boot:run
```

**Linux/Mac**:
```bash
./mvnw spring-boot:run
```

### Step 6: Verify It's Running

Open your browser and go to:
```
http://localhost:8080/api/users
```

You should see an empty array `[]` - this means it's working! 🎉

---

## 📝 First API Test

### Create Your First User

**Using cURL (Windows PowerShell)**:
```powershell
curl -X POST http://localhost:8080/api/users `
  -H "Content-Type: application/json" `
  -d '{\"name\": \"John Doe\", \"email\": \"john@example.com\", \"password\": \"password123\"}'
```

**Using cURL (Linux/Mac)**:
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "email": "john@example.com", "password": "password123"}'
```

**Expected Response** (Status 201):
```json
{
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "createdAt": "2025-12-17T07:35:00.123",
    "updatedAt": "2025-12-17T07:35:00.123"
}
```

### Get All Users

```bash
curl http://localhost:8080/api/users
```

### Get User by ID

```bash
curl http://localhost:8080/api/users/1
```

---

## 🛠️ Troubleshooting

### Problem: Port 8080 Already in Use

**Solution 1**: Stop the process using port 8080

**Windows**:
```powershell
netstat -ano | findstr :8080
taskkill /PID <PID_NUMBER> /F
```

**Solution 2**: Change the port in `application.properties`:
```properties
server.port=8081
```

### Problem: Database Connection Failed

**Check these**:
1. MySQL is running
2. Database `lms_db` exists
3. Username and password are correct in `application.properties`
4. MySQL is listening on port 3306

### Problem: Build Failed

**Solution**:
```bash
.\mvnw.cmd clean install -U
```

### Problem: Java Version Error

**Check Java version**:
```bash
java -version
```

Should show version 21 or higher. If not, install Java 21.

---

## 📚 Next Steps

Now that you're up and running:

1. **Read the API Documentation**: [USER_API.md](USER_API.md)
2. **Understand Error Handling**: [ERROR_HANDLING.md](ERROR_HANDLING.md)
3. **Learn About the Database**: [SETUPANDDB.md](SETUPANDDB.md)
4. **Explore the Code**: Start with `UserController.java`

---

## 🧪 Testing with Postman

### Import This Collection

Create a new Postman collection with these requests:

#### 1. Create User
- **Method**: POST
- **URL**: `http://localhost:8080/api/users`
- **Headers**: `Content-Type: application/json`
- **Body** (raw JSON):
```json
{
    "name": "Jane Doe",
    "email": "jane@example.com",
    "password": "password456"
}
```

#### 2. Get All Users
- **Method**: GET
- **URL**: `http://localhost:8080/api/users`

#### 3. Get User by ID
- **Method**: GET
- **URL**: `http://localhost:8080/api/users/1`

---

## 🎯 Common Tasks

### Stop the Application

Press `Ctrl + C` in the terminal where the application is running.

### View Logs

Logs are written to:
- Console (standard output)
- File: `logs/lms.log`

### Clear Database and Start Fresh

```sql
DROP DATABASE lms_db;
CREATE DATABASE lms_db;
```

Then restart the application.

### Run in Development Mode

The application automatically runs in development mode with:
- Hot reload (with Spring DevTools)
- Detailed error messages
- Debug logging enabled

---

## 💡 Pro Tips

1. **Use IntelliJ IDEA**: It has excellent Spring Boot support
2. **Enable Lombok Plugin**: Required for `@Data`, `@Builder`, etc.
3. **Use Postman Collections**: Save time testing APIs
4. **Check Logs**: Most issues can be diagnosed from logs
5. **Read Error Messages**: Our error responses are detailed and helpful

---

## 📖 API Endpoints Quick Reference

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users` | Create user |
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |

---

## 🔒 Security Notes

**Current Status**:
- ✅ Passwords are encrypted (BCrypt)
- ⚠️ No authentication required (development mode)
- ⚠️ All endpoints are public

**In Production**:
- Add JWT authentication
- Protect sensitive endpoints
- Enable HTTPS
- Add rate limiting

---

## 📱 Project Structure at a Glance

```
src/main/java/com/demo/lms/
├── controller/          # HTTP request handlers
│   └── user/           # User endpoints
├── service/            # Business logic
│   └── user/           # User operations
├── repository/         # Database access
├── model/              # Data entities
├── dto/                # Request/Response objects
├── exception/          # Error handling
├── mapper/             # Object mapping
└── config/             # Configuration
```

---

## ✅ Verification Checklist

After setup, verify:

- [ ] Application starts without errors
- [ ] Can access `http://localhost:8080/api/users`
- [ ] Can create a new user
- [ ] Can retrieve users
- [ ] Database tables are created automatically
- [ ] Passwords are encrypted in database

---

## 🆘 Getting Help

1. **Check Documentation**:
   - [README.md](README.md) - Documentation index
   - [ERROR_HANDLING.md](ERROR_HANDLING.md) - Error solutions
   - [USER_API.md](USER_API.md) - API reference

2. **Check Logs**: `logs/lms.log`

3. **Common Issues**: See [Troubleshooting](#troubleshooting) above

4. **Contact Team**: Reach out to the development team

---

## 🎓 Learning Path

### Beginner
1. Run the application
2. Test with Postman
3. Read the API documentation
4. Understand basic endpoints

### Intermediate
1. Explore the code structure
2. Understand error handling
3. Learn about JPA entities
4. Study the service layer

### Advanced
1. Add new features
2. Write tests
3. Implement authentication
4. Optimize performance

---

## 📊 Default Configuration

| Setting | Value |
|---------|-------|
| Server Port | 8080 |
| Context Path | / |
| Database | MySQL |
| Default Role | STUDENT |
| Password Encoding | BCrypt |
| Log Level | INFO |

---

**Ready to build something amazing? Let's go! 🚀**

---

**Last Updated**: December 17, 2025  
**Version**: 1.0.0

