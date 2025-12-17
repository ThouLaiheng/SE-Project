# User CRUD Operations - API Documentation

## Delete User

**Endpoint:** `DELETE /api/users/{id}`

### Success Response (200 OK)
```json
{
  "message": "User deleted successfully",
  "data": {
    "deletedUserId": 1
  }
}
```

### Error Responses

**User Not Found (404)**
```json
{
  "error": "User not found with id: 1"
}
```

**Internal Server Error (500)**
```json
{
  "error": "An error occurred: [error details]"
}
```

## Other CRUD Operations

### Create User
**Endpoint:** `POST /api/users`

**Success Response (201 Created):**
```json
{
  "message": "User created successfully",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "roles": ["STUDENT"]
  }
}
```

### Update User
**Endpoint:** `PUT /api/users/{id}`

**Success Response (200 OK):**
```json
{
  "message": "User updated successfully",
  "data": {
    "id": 1,
    "name": "John Doe Updated",
    "email": "john.updated@example.com",
    "roles": ["STUDENT"]
  }
}
```

### Update User Status
**Endpoint:** `PATCH /api/users/{id}/status?enabled=false`

**Success Response (200 OK):**
```json
{
  "message": "User disabled successfully",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "roles": ["STUDENT"]
  }
}
```

### Get All Users
**Endpoint:** `GET /api/users`

**Success Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "roles": ["STUDENT"]
  },
  {
    "id": 2,
    "name": "Jane Smith",
    "email": "jane@example.com",
    "roles": ["ADMIN"]
  }
]
```

### Get User by ID
**Endpoint:** `GET /api/users/{id}`

**Success Response (200 OK):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "roles": ["STUDENT"]
}
```
