package com.demo.lms.controller.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/docs")
@RequiredArgsConstructor
@Tag(name = "API Documentation", description = "API documentation and information endpoints")
public class ApiDocumentationController {

    @Operation(
            summary = "Get API documentation",
            description = "Returns API documentation with links to Swagger UI and OpenAPI spec"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved API documentation")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getApiDocumentation() {
        Map<String, Object> apiDocs = new HashMap<>();

        apiDocs.put("title", "Library Management System API");
        apiDocs.put("version", "1.0.0");
        apiDocs.put("description", "REST API for managing users, books, and library operations");

        // Swagger UI and OpenAPI links
        Map<String, String> documentation = new HashMap<>();
        documentation.put("swagger-ui", "http://localhost:8090/swagger-ui.html");
        documentation.put("openapi-json", "http://localhost:8090/api-docs");
        documentation.put("openapi-yaml", "http://localhost:8090/api-docs.yaml");

        apiDocs.put("documentation", documentation);

        Map<String, Object> endpoints = new HashMap<>();

        // User endpoints
        Map<String, Object> userEndpoints = new HashMap<>();
        userEndpoints.put("GET /api/users", "Get all users");
        userEndpoints.put("GET /api/users/{id}", "Get user by ID");
        userEndpoints.put("POST /api/users", "Create new user");
        userEndpoints.put("PUT /api/users/{id}", "Update user");
        userEndpoints.put("DELETE /api/users/{id}", "Delete user (returns success message)");
        userEndpoints.put("PATCH /api/users/{id}/status?enabled=true/false", "Enable/disable user");

        endpoints.put("User Management", userEndpoints);

        // Health and monitoring
        Map<String, Object> systemEndpoints = new HashMap<>();
        systemEndpoints.put("GET /actuator/health", "Application health check");
        systemEndpoints.put("GET /swagger-ui.html", "Interactive API Documentation (Swagger UI)");
        systemEndpoints.put("GET /api-docs", "OpenAPI 3 specification (JSON format)");
        systemEndpoints.put("GET /api/docs", "This API documentation");

        endpoints.put("System", systemEndpoints);

        apiDocs.put("endpoints", endpoints);

        Map<String, String> sampleUsers = new HashMap<>();
        sampleUsers.put("admin@lms.com", "Admin user (password: admin123)");
        sampleUsers.put("librarian@lms.com", "Librarian user (password: librarian123)");
        sampleUsers.put("student@lms.com", "Student user (password: student123)");

        apiDocs.put("sampleUsers", sampleUsers);

        return ResponseEntity.ok(apiDocs);
    }

    @GetMapping("/user-endpoints")
    public ResponseEntity<Map<String, Object>> getUserEndpointDetails() {
        Map<String, Object> userApiDetails = new HashMap<>();

        // Create User
        Map<String, Object> createUser = new HashMap<>();
        createUser.put("method", "POST");
        createUser.put("path", "/api/users");
        createUser.put("description", "Creates a new user with STUDENT role by default");
        createUser.put("requestBody", "{ \"name\": \"John Doe\", \"email\": \"john@example.com\", \"password\": \"password123\" }");
        createUser.put("responses", Map.of(
            "201", "User created successfully with success message",
            "400", "Invalid input data",
            "409", "Email already exists"
        ));

        // Get All Users
        Map<String, Object> getAllUsers = new HashMap<>();
        getAllUsers.put("method", "GET");
        getAllUsers.put("path", "/api/users");
        getAllUsers.put("description", "Retrieves all users with their roles");
        getAllUsers.put("responses", Map.of(
            "200", "List of users with roles"
        ));

        // Get User by ID
        Map<String, Object> getUserById = new HashMap<>();
        getUserById.put("method", "GET");
        getUserById.put("path", "/api/users/{id}");
        getUserById.put("description", "Retrieves a specific user by ID");
        getUserById.put("responses", Map.of(
            "200", "User details with roles",
            "404", "User not found"
        ));

        // Update User
        Map<String, Object> updateUser = new HashMap<>();
        updateUser.put("method", "PUT");
        updateUser.put("path", "/api/users/{id}");
        updateUser.put("description", "Updates user information");
        updateUser.put("requestBody", "{ \"name\": \"John Updated\", \"email\": \"john.updated@example.com\", \"password\": \"newPassword\" }");
        updateUser.put("responses", Map.of(
            "200", "User updated successfully with success message",
            "400", "Invalid input",
            "404", "User not found",
            "409", "Email already exists"
        ));

        // Delete User
        Map<String, Object> deleteUser = new HashMap<>();
        deleteUser.put("method", "DELETE");
        deleteUser.put("path", "/api/users/{id}");
        deleteUser.put("description", "Deletes a user and returns success message");
        deleteUser.put("responses", Map.of(
            "200", "{ \"message\": \"User deleted successfully\", \"data\": { \"deletedUserId\": 1 } }",
            "404", "User not found"
        ));

        // Update User Status
        Map<String, Object> updateStatus = new HashMap<>();
        updateStatus.put("method", "PATCH");
        updateStatus.put("path", "/api/users/{id}/status");
        updateStatus.put("description", "Enable or disable a user account");
        updateStatus.put("parameters", "?enabled=true (to enable) or ?enabled=false (to disable)");
        updateStatus.put("responses", Map.of(
            "200", "User status updated with success message",
            "404", "User not found"
        ));

        userApiDetails.put("createUser", createUser);
        userApiDetails.put("getAllUsers", getAllUsers);
        userApiDetails.put("getUserById", getUserById);
        userApiDetails.put("updateUser", updateUser);
        userApiDetails.put("deleteUser", deleteUser);
        userApiDetails.put("updateUserStatus", updateStatus);

        return ResponseEntity.ok(userApiDetails);
    }
}
