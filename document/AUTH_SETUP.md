# LMS Authentication Setup Report

Date: 2025-12-23

## Overview
- Purpose: Add user authentication (register/login) with a simple web UI and secure API.
- Result: Working register/login flow, JWT-based auth, and a public landing UI served on port 8080.

## Backend Changes

### Dependencies
- Consolidated JWT to a single version and removed duplicates in [SE-Project/pom.xml](SE-Project/pom.xml).
- Fixed runtime error “NoClassDefFoundError: io/jsonwebtoken/security/SecureRequest” by upgrading JJWT and code.

### JWT Utility
- Migrated to JJWT 0.12.x API:
  - Builder: `subject()`, `issuedAt()`, `expiration()`
  - Parser: `parser().verifyWith(key).parseSignedClaims(token)`
- File: [SE-Project/src/main/java/com/demo/lms/Jwt/JwtUtil.java](SE-Project/src/main/java/com/demo/lms/Jwt/JwtUtil.java)

### Security
- Public access granted for static assets and root path.
- Added forward from `/` to `index.html`.
- Files:
  - [SE-Project/src/main/java/com/demo/lms/config/SecurityConfig.java](SE-Project/src/main/java/com/demo/lms/config/SecurityConfig.java)
  - [SE-Project/src/main/java/com/demo/lms/config/WebMvcConfig.java](SE-Project/src/main/java/com/demo/lms/config/WebMvcConfig.java)

### Controllers
- Register and Login:
  - POST `/api/register`
  - POST `/api/login`
- File: [SE-Project/src/main/java/com/demo/lms/controller/user/AuthenticationController.java](SE-Project/src/main/java/com/demo/lms/controller/user/AuthenticationController.java)

### Error Handling
- Centralized responses and logs in [SE-Project/src/main/java/com/demo/lms/exception/GlobalExceptionHandler.java](SE-Project/src/main/java/com/demo/lms/exception/GlobalExceptionHandler.java)

## API Endpoints

### Register
- POST `/api/register`
- Request:
  ```json
  { "email": "user@example.com", "password": "secret123" }
  ```
- Response: `{ "email": "...", "token": "..." }`
- Duplicate email returns HTTP 409 with error message.

### Login
- POST `/api/login`
- Request:
  ```json
  { "email": "user@example.com", "password": "secret123" }
  ```
- Response: `{ "email": "...", "token": "..." }`

## Frontend UI

### Files
- Page: [SE-Project/src/main/resources/static/index.html](SE-Project/src/main/resources/static/index.html)
- Styles: 
  - [SE-Project/src/main/resources/static/css/auth.css](SE-Project/src/main/resources/static/css/auth.css)
  - [SE-Project/src/main/resources/static/css/dashboard.css](SE-Project/src/main/resources/static/css/dashboard.css)
- Scripts:
  - [SE-Project/src/main/resources/static/js/auth.js](SE-Project/src/main/resources/static/js/auth.js)
  - [SE-Project/src/main/resources/static/js/dashboard.js](SE-Project/src/main/resources/static/js/dashboard.js)
- Dashboard: [SE-Project/src/main/resources/static/dashboard.html](SE-Project/src/main/resources/static/dashboard.html)

### Features
- Toggle Login/Register, client-side validation, loading spinners.
- Calls `/api/register` and `/api/login`.
- Stores JWT in localStorage; redirects to dashboard on success.

## Security Matrix

- Public:
  - `/`, `/index.html`, `/favicon.ico`
  - `/css/**`, `/js/**`, `/images/**`
  - `/swagger-ui/**`, `/v3/api-docs/**`, `/api-docs/**`
  - `/api/login`, `/api/register`
- Protected:
  - All other routes require JWT (handled via `JwtFilter`).

## Build & Run

### Build
```bash
./mvnw.cmd clean package -DskipTests
```

### Run (Port 8080)
```bash
java -jar target/LMS-0.0.1-SNAPSHOT.jar --server.port=8080
```

### Access
- UI: http://localhost:8080/
- Swagger: http://localhost:8080/swagger-ui.html

## Troubleshooting Performed
- Fixed wrong endpoint usage (`/api/register` vs `/register`).
- Resolved 403 on static UI by permitting static routes and forwarding `/` to `index.html`.
- Resolved port conflicts by stopping previous Java processes before starting.
- Cleaned Maven cache and rebuilt to ensure correct JJWT version is used.

## How To Test

### Register (API)
```http
POST /api/register
Content-Type: application/json

{ "email": "test@example.com", "password": "secret123" }
```

### Login (API)
```http
POST /api/login
Content-Type: application/json

{ "email": "test@example.com", "password": "secret123" }
```

### UI
- Use the forms on the homepage to register and login.
- After login, you’ll be redirected to the dashboard.

## Next Steps (Optional)
- Add a favicon to silence `/favicon.ico` warnings.
- Serve `dashboard.html` through a controller and secure it server-side.
- Add logout endpoint and token invalidation (if needed).
- Add more user fields (name, avatar) and validations.
