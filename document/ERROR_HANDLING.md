# Error Handling Documentation - Library Management System

## 📋 Table of Contents
- [Overview](#overview)
- [Error Response Structure](#error-response-structure)
- [Exception Types](#exception-types)
- [HTTP Status Codes](#http-status-codes)
- [Error Examples](#error-examples)
- [Best Practices](#best-practices)
- [Testing Error Handling](#testing-error-handling)

---

## 🎯 Overview

The Library Management System implements a comprehensive error handling mechanism using Spring Boot's `@RestControllerAdvice` annotation. All exceptions are centrally handled by the `GlobalExceptionHandler` class, ensuring consistent and standardized error responses across all API endpoints.

### Key Features
- **Centralized Error Handling**: All exceptions are caught and processed in one place
- **Consistent Response Format**: Standardized error response structure
- **Detailed Error Information**: Includes timestamp, status code, error type, and descriptive messages
- **Field-Level Validation**: Provides specific field errors for validation failures
- **Request Path Tracking**: Includes the request URI in error responses
- **Type Safety**: Uses custom exceptions for domain-specific errors

---

## 🔧 Error Response Structure

All error responses follow a standardized format using the `ErrorResponse` DTO:

### ErrorResponse Fields

```java
{
    "timestamp": "2025-12-17T07:35:00.123",  // When the error occurred
    "status": 404,                            // HTTP status code
    "error": "Not Found",                     // Error category/type
    "message": "User not found",              // Human-readable error message
    "fieldErrors": {                          // Optional: Field validation errors
        "email": "must be a valid email",
        "name": "must not be blank"
    },
    "path": "/api/users/999"                  // Request URI where error occurred
}
```

### ErrorResponse Class Structure

```java
package com.demo.lms.dto.response;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private Map<String, String> fieldErrors;
    private String path;
}
```

---

## 🚨 Exception Types

### 1. UserNotFoundException
**Purpose**: Thrown when a requested user cannot be found in the database

**HTTP Status**: `404 NOT FOUND`

**When Thrown**:
- GET `/api/users/{id}` - User ID doesn't exist
- Any operation requiring a user that doesn't exist

**Example**:
```java
public UserResponse getUserById(Long id) {
    return userRepository.findById(id)
        .map(UserMapper::toResponse)
        .orElseThrow(() -> new UserNotFoundException("User not found"));
}
```

---

### 2. UserAlreadyExistsException
**Purpose**: Thrown when attempting to create a user with an email that already exists

**HTTP Status**: `409 CONFLICT`

**When Thrown**:
- POST `/api/users` - Email already registered

**Example**:
```java
public UserResponse createUser(CreateUserRequest request) {
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
        throw new UserAlreadyExistsException("Email already exists");
    }
    // ... continue user creation
}
```

---

### 3. MethodArgumentNotValidException
**Purpose**: Thrown automatically by Spring when request validation fails

**HTTP Status**: `400 BAD REQUEST`

**When Thrown**:
- POST `/api/users` - Invalid request body
- Any endpoint with `@Valid` annotation

**Validation Annotations**:
```java
public class CreateUserRequest {
    @NotBlank(message = "Name is required")
    private String name;
    
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
```

---

### 4. MethodArgumentTypeMismatchException
**Purpose**: Thrown when a request parameter has an invalid type

**HTTP Status**: `400 BAD REQUEST`

**When Thrown**:
- GET `/api/users/{id}` - ID is not a valid Long (e.g., `/api/users/abc`)
- Any path variable or request parameter with type mismatch

**Example Error**:
```json
{
    "timestamp": "2025-12-17T07:35:00.123",
    "status": 400,
    "error": "Bad Request",
    "message": "Invalid value 'abc' for parameter 'id'. Expected type: Long",
    "path": "/api/users/abc"
}
```

---

### 5. IllegalArgumentException
**Purpose**: Thrown for invalid arguments in business logic

**HTTP Status**: `400 BAD REQUEST`

**When Thrown**:
- Invalid business logic parameters
- Invalid state transitions
- Invalid data formats

---

### 6. Exception (General)
**Purpose**: Catches all unhandled exceptions

**HTTP Status**: `500 INTERNAL SERVER ERROR`

**When Thrown**:
- Unexpected errors
- Database connection failures
- System errors

**Security Note**: Generic message provided to prevent information leakage

---

## 📊 HTTP Status Codes

| Status Code | Status Text | Exception Type | Description |
|------------|-------------|----------------|-------------|
| 200 | OK | - | Successful request |
| 201 | CREATED | - | Resource created successfully |
| 400 | BAD REQUEST | MethodArgumentNotValidException, MethodArgumentTypeMismatchException, IllegalArgumentException | Invalid request parameters or validation failure |
| 404 | NOT FOUND | UserNotFoundException | Requested resource not found |
| 409 | CONFLICT | UserAlreadyExistsException | Resource already exists |
| 500 | INTERNAL SERVER ERROR | Exception (General) | Unexpected server error |

---

## 📝 Error Examples

### Example 1: User Not Found (404)

**Request**:
```http
GET /api/users/999
```

**Response**:
```json
{
    "timestamp": "2025-12-17T07:35:00.123",
    "status": 404,
    "error": "Not Found",
    "message": "User not found",
    "fieldErrors": null,
    "path": "/api/users/999"
}
```

---

### Example 2: Email Already Exists (409)

**Request**:
```http
POST /api/users
Content-Type: application/json

{
    "name": "John Doe",
    "email": "existing@example.com",
    "password": "password123"
}
```

**Response**:
```json
{
    "timestamp": "2025-12-17T07:35:00.123",
    "status": 409,
    "error": "Conflict",
    "message": "Email already exists",
    "fieldErrors": null,
    "path": "/api/users"
}
```

---

### Example 3: Validation Failure (400)

**Request**:
```http
POST /api/users
Content-Type: application/json

{
    "name": "",
    "email": "invalid-email",
    "password": "123"
}
```

**Response**:
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

---

### Example 4: Type Mismatch (400)

**Request**:
```http
GET /api/users/abc
```

**Response**:
```json
{
    "timestamp": "2025-12-17T07:35:00.123",
    "status": 400,
    "error": "Bad Request",
    "message": "Invalid value 'abc' for parameter 'id'. Expected type: Long",
    "fieldErrors": null,
    "path": "/api/users/abc"
}
```

---

### Example 5: Internal Server Error (500)

**Request**:
```http
GET /api/users
```

**Response** (when unexpected error occurs):
```json
{
    "timestamp": "2025-12-17T07:35:00.123",
    "status": 500,
    "error": "Internal Server Error",
    "message": "An unexpected error occurred. Please contact support if the problem persists.",
    "fieldErrors": null,
    "path": "/api/users"
}
```

---

## ✅ Best Practices

### 1. Use Appropriate HTTP Status Codes
- **2xx**: Success
- **4xx**: Client errors (user can fix)
- **5xx**: Server errors (requires investigation)

### 2. Provide Meaningful Error Messages
```java
// ❌ Bad
throw new UserNotFoundException("Error");

// ✅ Good
throw new UserNotFoundException("User with ID " + id + " not found");
```

### 3. Never Expose Sensitive Information
```java
// ❌ Bad - Exposes internal details
throw new Exception("SQL error: " + sqlException.getMessage());

// ✅ Good - Generic message
throw new RuntimeException("An error occurred while processing your request");
```

### 4. Use Domain-Specific Exceptions
```java
// ✅ Create custom exceptions for your domain
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) {
        super(message);
    }
}
```

### 5. Validate Input at Controller Level
```java
@PostMapping
public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
    // @Valid triggers automatic validation
}
```

### 6. Log Errors Appropriately
```java
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
    log.error("Unexpected error occurred at {}", request.getRequestURI(), ex);
    // Return generic error response
}
```

---

## 🧪 Testing Error Handling

### 1. Test User Not Found

**Using cURL**:
```bash
curl -X GET http://localhost:8080/api/users/999
```

**Using Postman**:
- Method: GET
- URL: `http://localhost:8080/api/users/999`
- Expected: 404 Not Found

---

### 2. Test Validation Errors

**Using cURL**:
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "email": "invalid",
    "password": "123"
  }'
```

**Expected**: 400 Bad Request with field errors

---

### 3. Test Type Mismatch

**Using cURL**:
```bash
curl -X GET http://localhost:8080/api/users/abc
```

**Expected**: 400 Bad Request with type mismatch message

---

### 4. Test Duplicate Email

**Using cURL**:
```bash
# First, create a user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123"
  }'

# Then, try to create another user with the same email
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Doe",
    "email": "john@example.com",
    "password": "password456"
  }'
```

**Expected**: 409 Conflict

---

## 🔍 Implementation Details

### GlobalExceptionHandler.java

The `GlobalExceptionHandler` class is annotated with `@RestControllerAdvice`, making it a global exception handler for all controllers:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
            UserNotFoundException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
    // ... other exception handlers
}
```

### Key Components:
1. **@RestControllerAdvice**: Makes the class a global exception handler
2. **@ExceptionHandler**: Defines which exception type to handle
3. **HttpServletRequest**: Provides request context (URI, method, etc.)
4. **ErrorResponse.builder()**: Creates standardized error response
5. **ResponseEntity**: Wraps response with appropriate HTTP status

---

## 📚 Additional Resources

### Related Files
- `GlobalExceptionHandler.java` - Main exception handler
- `ErrorResponse.java` - Standardized error DTO
- `UserNotFoundException.java` - Custom exception for user not found
- `UserAlreadyExistsException.java` - Custom exception for duplicate users
- `UserController.java` - Example controller with error handling

### Spring Boot Documentation
- [Error Handling](https://docs.spring.io/spring-boot/docs/current/reference/html/web.html#web.servlet.spring-mvc.error-handling)
- [Validation](https://docs.spring.io/spring-boot/docs/current/reference/html/io.html#io.validation)
- [Exception Handling](https://spring.io/guides/tutorials/rest/)

---

## 📞 Support

For questions or issues related to error handling:
1. Check the error message and status code
2. Refer to this documentation
3. Review the `GlobalExceptionHandler` class
4. Contact the development team

---

**Last Updated**: December 17, 2025
**Version**: 1.0.0

