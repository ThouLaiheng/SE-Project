package com.demo.lms.controller.user;

import com.demo.lms.dto.request.CreateUserRequest;
import com.demo.lms.dto.request.UpdateUserRequest;
import com.demo.lms.dto.common.SuccessResponse;
import com.demo.lms.dto.response.UserResponse;
import com.demo.lms.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "APIs for managing users in the library system")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Create a new user",
            description = "Creates a new user account in the library management system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "409", description = "User already exists", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PostMapping
    public ResponseEntity<SuccessResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            log.info("Creating user with email: {}", request.getEmail());
            UserResponse userResponse = userService.createUser(request);
            log.info("User created successfully with ID: {}", userResponse.getId());

            SuccessResponse response = SuccessResponse.builder()
                    .message("User created successfully")
                    .data(userResponse)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creating user with email: {}", request.getEmail(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieve a list of all users in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        try {
            log.info("Fetching all users");
            List<UserResponse> users = userService.getAllUsers();
            log.info("Found {} users", users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error fetching all users", e);
            throw e;
        }
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieve a specific user by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id) {
        try {
            log.info("Fetching user with ID: {}", id);
            UserResponse user = userService.getUserById(id);
            log.info("User found: {}", user.getEmail());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Error fetching user with ID: {}", id, e);
            throw e;
        }
    }

    @Operation(
            summary = "Update user",
            description = "Update an existing user's information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> updateUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        try {
            log.info("Updating user with ID: {}", id);
            UserResponse userResponse = userService.updateUser(id, request);
            log.info("User updated successfully with ID: {}", userResponse.getId());

            SuccessResponse response = SuccessResponse.builder()
                    .message("User updated successfully")
                    .data(userResponse)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating user with ID: {}", id, e);
            throw e;
        }
    }

    @Operation(
            summary = "Delete user",
            description = "Delete a user from the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id) {
        try {
            log.info("Deleting user with ID: {}", id);
            userService.deleteUser(id);
            log.info("User deleted successfully with ID: {}", id);

            SuccessResponse response = SuccessResponse.builder()
                    .message("User deleted successfully")
                    .data(Map.of("deletedUserId", id))
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error deleting user with ID: {}", id, e);
            throw e;
        }
    }

    @Operation(
            summary = "Update user status",
            description = "Enable or disable a user account"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User status updated successfully",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<SuccessResponse> updateUserStatus(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Enable or disable user", required = true, example = "true")
            @RequestParam boolean enabled) {
        try {
            log.info("Updating user status for ID: {} to enabled: {}", id, enabled);
            UserResponse userResponse = userService.updateUserStatus(id, enabled);
            log.info("User status updated successfully for ID: {}", id);

            String statusMessage = enabled ? "enabled" : "disabled";
            SuccessResponse response = SuccessResponse.builder()
                    .message("User " + statusMessage + " successfully")
                    .data(userResponse)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating user status for ID: {}", id, e);
            throw e;
        }
    }

}
