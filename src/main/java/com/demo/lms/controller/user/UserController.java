package com.demo.lms.controller.user;

import com.demo.lms.dto.request.CreateUserRequest;
import com.demo.lms.dto.request.UpdateUserRequest;
import com.demo.lms.dto.result.SuccessResponse;
import com.demo.lms.dto.response.UserResponse;
import com.demo.lms.service.user.UserService;
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
public class UserController {

    private final UserService userService;

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

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
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

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> updateUser(@PathVariable Long id,
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

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteUser(@PathVariable Long id) {
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

    @PatchMapping("/{id}/status")
    public ResponseEntity<SuccessResponse> updateUserStatus(@PathVariable Long id,
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
