# User API Documentation - Library Management System

## 📋 Table of Contents
- [Overview](#overview)
- [Base URL](#base-url)
- [Authentication](#authentication)
- [API Endpoints](#api-endpoints)
  - [Create User](#1-create-user)
  - [Get All Users](#2-get-all-users)
  - [Get User by ID](#3-get-user-by-id)
- [Data Models](#data-models)
- [Error Responses](#error-responses)
- [Code Examples](#code-examples)

---

## 🎯 Overview

The User API provides endpoints for managing user accounts in the Library Management System. It supports creating new users, retrieving user information, and listing all users.

**Features**:
- User registration
- User profile retrieval
- User listing
- Input validation
- Comprehensive error handling

---

## 🌐 Base URL

```
http://localhost:8080/api/users
```

**Note**: Replace `localhost:8080` with your actual server address and port.

---

## 🔐 Authentication

Currently, the User API endpoints are open for development purposes. In production, these endpoints should be protected with appropriate authentication mechanisms (JWT, OAuth2, etc.).

**Future Implementation**:
- `/api/users` (POST) - Public (for registration)
- `/api/users` (GET) - Protected (ADMIN role)
- `/api/users/{id}` (GET) - Protected (ADMIN or own profile)

---

## 📡 API Endpoints

### 1. Create User

Creates a new user account in the system.

#### Endpoint Details
```
POST /api/users
```

#### Request Headers
```
Content-Type: application/json
```

#### Request Body
```json
{
    "name": "string",      // Required, not blank
    "email": "string",     // Required, valid email format, unique
    "password": "string"   // Required, minimum 6 characters
}
```

#### Validation Rules
| Field | Rule | Error Message |
|-------|------|---------------|
| name | Not blank | "Name is required" |
| email | Not blank | "Email is required" |
| email | Valid email format | "Email must be valid" |
| email | Unique | "Email already exists" |
| password | Minimum 6 characters | "Password must be at least 6 characters" |

#### Success Response

**Status Code**: `201 CREATED`

**Response Body**:
```json
{
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "createdAt": "2025-12-17T07:35:00.123",
    "updatedAt": "2025-12-17T07:35:00.123"
}
```

#### Error Responses

**400 Bad Request** - Validation Failure
```json
{
    "timestamp": "2025-12-17T07:35:00.123",
    "status": 400,
    "error": "Validation Failed",
    "message": "Invalid request parameters",
    "fieldErrors": {
        "name": "Name is required",
        "email": "Email must be valid",
        "password": "Password must be at least 6 characters"
    },
    "path": "/api/users"
}
```

**409 Conflict** - Email Already Exists
```json
{
    "timestamp": "2025-12-17T07:35:00.123",
    "status": 409,
    "error": "Conflict",
    "message": "Email already exists",
    "path": "/api/users"
}
```

#### Example Requests

**cURL**:
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "password123"
  }'
```

**JavaScript (Fetch API)**:
```javascript
fetch('http://localhost:8080/api/users', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({
        name: 'John Doe',
        email: 'john.doe@example.com',
        password: 'password123'
    })
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console.error('Error:', error));
```

**Python (Requests)**:
```python
import requests

url = 'http://localhost:8080/api/users'
data = {
    'name': 'John Doe',
    'email': 'john.doe@example.com',
    'password': 'password123'
}

response = requests.post(url, json=data)
print(response.json())
```

---

### 2. Get All Users

Retrieves a list of all users in the system.

#### Endpoint Details
```
GET /api/users
```

#### Request Headers
```
None required
```

#### Request Parameters
None

#### Success Response

**Status Code**: `200 OK`

**Response Body**:
```json
[
    {
        "id": 1,
        "name": "John Doe",
        "email": "john.doe@example.com",
        "createdAt": "2025-12-17T07:35:00.123",
        "updatedAt": "2025-12-17T07:35:00.123"
    },
    {
        "id": 2,
        "name": "Jane Smith",
        "email": "jane.smith@example.com",
        "createdAt": "2025-12-17T08:00:00.456",
        "updatedAt": "2025-12-17T08:00:00.456"
    }
]
```

**Empty List**:
```json
[]
```

#### Error Responses

**500 Internal Server Error** - Unexpected Error
```json
{
    "timestamp": "2025-12-17T07:35:00.123",
    "status": 500,
    "error": "Internal Server Error",
    "message": "An unexpected error occurred. Please contact support if the problem persists.",
    "path": "/api/users"
}
```

#### Example Requests

**cURL**:
```bash
curl -X GET http://localhost:8080/api/users
```

**JavaScript (Fetch API)**:
```javascript
fetch('http://localhost:8080/api/users')
    .then(response => response.json())
    .then(data => console.log(data))
    .catch(error => console.error('Error:', error));
```

**Python (Requests)**:
```python
import requests

url = 'http://localhost:8080/api/users'
response = requests.get(url)
print(response.json())
```

---

### 3. Get User by ID

Retrieves a specific user by their unique ID.

#### Endpoint Details
```
GET /api/users/{id}
```

#### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Long | Yes | User ID (must be a valid number) |

#### Request Headers
```
None required
```

#### Success Response

**Status Code**: `200 OK`

**Response Body**:
```json
{
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "createdAt": "2025-12-17T07:35:00.123",
    "updatedAt": "2025-12-17T07:35:00.123"
}
```

#### Error Responses

**400 Bad Request** - Invalid ID Format
```json
{
    "timestamp": "2025-12-17T07:35:00.123",
    "status": 400,
    "error": "Bad Request",
    "message": "Invalid value 'abc' for parameter 'id'. Expected type: Long",
    "path": "/api/users/abc"
}
```

**404 Not Found** - User Not Found
```json
{
    "timestamp": "2025-12-17T07:35:00.123",
    "status": 404,
    "error": "Not Found",
    "message": "User not found",
    "path": "/api/users/999"
}
```

#### Example Requests

**cURL**:
```bash
curl -X GET http://localhost:8080/api/users/1
```

**JavaScript (Fetch API)**:
```javascript
const userId = 1;
fetch(`http://localhost:8080/api/users/${userId}`)
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => console.log(data))
    .catch(error => console.error('Error:', error));
```

**Python (Requests)**:
```python
import requests

user_id = 1
url = f'http://localhost:8080/api/users/{user_id}'
response = requests.get(url)

if response.status_code == 200:
    print(response.json())
else:
    print(f"Error: {response.status_code}")
    print(response.json())
```

---

## 📦 Data Models

### CreateUserRequest

Request model for creating a new user.

```java
{
    "name": "string",      // User's full name
    "email": "string",     // User's email address (unique)
    "password": "string"   // User's password (plain text, will be encrypted)
}
```

**Constraints**:
- `name`: Not blank
- `email`: Not blank, valid email format, unique
- `password`: Minimum 6 characters

---

### UserResponse

Response model for user data.

```java
{
    "id": "long",                    // User's unique ID
    "name": "string",                // User's full name
    "email": "string",               // User's email address
    "createdAt": "LocalDateTime",    // Account creation timestamp
    "updatedAt": "LocalDateTime"     // Last update timestamp
}
```

**Note**: Password is never returned in responses for security reasons.

---

### ErrorResponse

Response model for all error scenarios.

```java
{
    "timestamp": "LocalDateTime",              // Error occurrence timestamp
    "status": "int",                           // HTTP status code
    "error": "string",                         // Error type/category
    "message": "string",                       // Human-readable error message
    "fieldErrors": "Map<String, String>",      // Field validation errors (optional)
    "path": "string"                           // Request URI where error occurred
}
```

---

## ❌ Error Responses

### HTTP Status Codes

| Status Code | Description | When It Occurs |
|------------|-------------|----------------|
| 200 OK | Success | Successful GET requests |
| 201 CREATED | Resource created | Successful POST request |
| 400 BAD REQUEST | Invalid request | Validation failure, type mismatch |
| 404 NOT FOUND | Resource not found | User doesn't exist |
| 409 CONFLICT | Resource conflict | Email already exists |
| 500 INTERNAL SERVER ERROR | Server error | Unexpected server errors |

### Common Error Scenarios

#### 1. Validation Errors (400)
**Cause**: Invalid input data
**Solution**: Check field values against validation rules

#### 2. Email Already Exists (409)
**Cause**: Attempting to create user with existing email
**Solution**: Use a different email address

#### 3. User Not Found (404)
**Cause**: Requested user ID doesn't exist
**Solution**: Verify the user ID

#### 4. Type Mismatch (400)
**Cause**: Invalid data type for path variable (e.g., string instead of number)
**Solution**: Ensure correct data types in requests

#### 5. Internal Server Error (500)
**Cause**: Unexpected server error
**Solution**: Contact support or check server logs

---

## 💻 Code Examples

### Complete Integration Example (JavaScript)

```javascript
class UserAPI {
    constructor(baseURL = 'http://localhost:8080/api/users') {
        this.baseURL = baseURL;
    }

    async createUser(userData) {
        try {
            const response = await fetch(this.baseURL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(JSON.stringify(error));
            }

            return await response.json();
        } catch (error) {
            console.error('Failed to create user:', error);
            throw error;
        }
    }

    async getAllUsers() {
        try {
            const response = await fetch(this.baseURL);

            if (!response.ok) {
                const error = await response.json();
                throw new Error(JSON.stringify(error));
            }

            return await response.json();
        } catch (error) {
            console.error('Failed to fetch users:', error);
            throw error;
        }
    }

    async getUserById(id) {
        try {
            const response = await fetch(`${this.baseURL}/${id}`);

            if (!response.ok) {
                const error = await response.json();
                throw new Error(JSON.stringify(error));
            }

            return await response.json();
        } catch (error) {
            console.error(`Failed to fetch user ${id}:`, error);
            throw error;
        }
    }
}

// Usage
const userAPI = new UserAPI();

// Create user
userAPI.createUser({
    name: 'John Doe',
    email: 'john.doe@example.com',
    password: 'password123'
})
.then(user => console.log('Created:', user))
.catch(error => console.error('Error:', error));

// Get all users
userAPI.getAllUsers()
    .then(users => console.log('All users:', users))
    .catch(error => console.error('Error:', error));

// Get user by ID
userAPI.getUserById(1)
    .then(user => console.log('User:', user))
    .catch(error => console.error('Error:', error));
```

---

### Complete Integration Example (Python)

```python
import requests
from typing import Dict, List, Optional

class UserAPI:
    def __init__(self, base_url: str = 'http://localhost:8080/api/users'):
        self.base_url = base_url

    def create_user(self, user_data: Dict) -> Dict:
        """Create a new user"""
        try:
            response = requests.post(self.base_url, json=user_data)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.HTTPError as e:
            print(f"Failed to create user: {e.response.json()}")
            raise

    def get_all_users(self) -> List[Dict]:
        """Get all users"""
        try:
            response = requests.get(self.base_url)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.HTTPError as e:
            print(f"Failed to fetch users: {e.response.json()}")
            raise

    def get_user_by_id(self, user_id: int) -> Dict:
        """Get user by ID"""
        try:
            response = requests.get(f"{self.base_url}/{user_id}")
            response.raise_for_status()
            return response.json()
        except requests.exceptions.HTTPError as e:
            print(f"Failed to fetch user {user_id}: {e.response.json()}")
            raise

# Usage
if __name__ == '__main__':
    api = UserAPI()

    # Create user
    try:
        new_user = api.create_user({
            'name': 'John Doe',
            'email': 'john.doe@example.com',
            'password': 'password123'
        })
        print(f"Created user: {new_user}")
    except Exception as e:
        print(f"Error creating user: {e}")

    # Get all users
    try:
        users = api.get_all_users()
        print(f"All users: {users}")
    except Exception as e:
        print(f"Error fetching users: {e}")

    # Get user by ID
    try:
        user = api.get_user_by_id(1)
        print(f"User: {user}")
    except Exception as e:
        print(f"Error fetching user: {e}")
```

---

### Complete Integration Example (Java)

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserAPIClient {
    private final String baseURL;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public UserAPIClient(String baseURL) {
        this.baseURL = baseURL;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public UserResponse createUser(CreateUserRequest request) throws Exception {
        String requestBody = objectMapper.writeValueAsString(request);
        
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseURL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, 
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), UserResponse.class);
        } else {
            throw new Exception("Failed to create user: " + response.body());
        }
    }

    public List<UserResponse> getAllUsers() throws Exception {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseURL))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, 
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), 
                    new TypeReference<List<UserResponse>>() {});
        } else {
            throw new Exception("Failed to fetch users: " + response.body());
        }
    }

    public UserResponse getUserById(Long id) throws Exception {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseURL + "/" + id))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, 
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), UserResponse.class);
        } else {
            throw new Exception("Failed to fetch user: " + response.body());
        }
    }

    public static void main(String[] args) {
        UserAPIClient client = new UserAPIClient("http://localhost:8080/api/users");

        try {
            // Create user
            CreateUserRequest request = new CreateUserRequest();
            request.setName("John Doe");
            request.setEmail("john.doe@example.com");
            request.setPassword("password123");
            
            UserResponse user = client.createUser(request);
            System.out.println("Created user: " + user);

            // Get all users
            List<UserResponse> users = client.getAllUsers();
            System.out.println("All users: " + users);

            // Get user by ID
            UserResponse fetchedUser = client.getUserById(1L);
            System.out.println("User: " + fetchedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

## 🧪 Testing with Postman

### Collection Setup

1. **Create a new collection**: "User API"
2. **Set base URL variable**: `{{baseURL}}` = `http://localhost:8080/api/users`

### Test Cases

#### Test 1: Create User (Success)
- **Method**: POST
- **URL**: `{{baseURL}}`
- **Headers**: `Content-Type: application/json`
- **Body**:
```json
{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "password123"
}
```
- **Expected**: 201 CREATED

#### Test 2: Create User (Validation Error)
- **Method**: POST
- **URL**: `{{baseURL}}`
- **Body**:
```json
{
    "name": "",
    "email": "invalid-email",
    "password": "123"
}
```
- **Expected**: 400 BAD REQUEST with field errors

#### Test 3: Create User (Duplicate Email)
- **Method**: POST
- **URL**: `{{baseURL}}`
- **Body**: (use same email as Test 1)
- **Expected**: 409 CONFLICT

#### Test 4: Get All Users
- **Method**: GET
- **URL**: `{{baseURL}}`
- **Expected**: 200 OK with array of users

#### Test 5: Get User by ID (Success)
- **Method**: GET
- **URL**: `{{baseURL}}/1`
- **Expected**: 200 OK with user object

#### Test 6: Get User by ID (Not Found)
- **Method**: GET
- **URL**: `{{baseURL}}/999`
- **Expected**: 404 NOT FOUND

#### Test 7: Get User by ID (Invalid Format)
- **Method**: GET
- **URL**: `{{baseURL}}/abc`
- **Expected**: 400 BAD REQUEST

---

## 📚 Related Documentation

- [Error Handling Documentation](ERROR_HANDLING.md)
- [Setup and Database Documentation](SETUPANDDB.md)
- [Spring Boot REST API Best Practices](https://spring.io/guides/tutorials/rest/)

---

## 📞 Support

For API-related questions or issues:
1. Review this documentation
2. Check error response messages
3. Refer to [Error Handling Documentation](ERROR_HANDLING.md)
4. Contact the development team

---

**Last Updated**: December 17, 2025  
**API Version**: 1.0.0  
**Base URL**: http://localhost:8080/api/users

