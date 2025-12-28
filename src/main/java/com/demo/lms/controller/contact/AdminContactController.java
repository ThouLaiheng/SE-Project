package com.demo.lms.controller.contact;

import com.demo.lms.dto.request.UpdateContactStatusRequest;
import com.demo.lms.dto.response.ContactResponse;
import com.demo.lms.dto.common.SuccessResponse;
import com.demo.lms.service.contact.ContactService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/contacts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin Contact Management", description = "Admin APIs for managing contact requests and support tickets")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
public class AdminContactController {

    private final ContactService contactService;

    @Operation(
            summary = "Get all contact requests",
            description = "Retrieve all contact requests in the system (Admin only)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all contact requests",
                    content = @Content(schema = @Schema(implementation = ContactResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ContactResponse>> getAll() {
        try {
            log.info("Admin fetching all contact requests");
            List<ContactResponse> contacts = contactService.getAllContacts();
            log.info("Found {} contact requests", contacts.size());
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            log.error("Error fetching all contact requests", e);
            throw e;
        }
    }

    @Operation(
            summary = "Update contact request status",
            description = "Update the status and admin response of a contact request (Admin only)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contact request updated successfully",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Contact request not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> update(
            @Parameter(description = "Contact request ID to update")
            @PathVariable Long id,
            @Valid @RequestBody UpdateContactStatusRequest request) {
        try {
            log.info("Admin updating contact request with ID: {}", id);
            ContactResponse contactResponse = contactService.updateContact(id, request);
            log.info("Contact request updated successfully with ID: {}", contactResponse.getId());

            SuccessResponse response = SuccessResponse.builder()
                    .message("Contact request updated successfully")
                    .data(contactResponse)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating contact request with ID: {}", id, e);
            throw e;
        }
    }

    @Operation(
            summary = "Delete contact request",
            description = "Delete a contact request from the system (Admin only)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contact request deleted successfully",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "Contact request not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> delete(
            @Parameter(description = "Contact request ID to delete")
            @PathVariable Long id) {
        try {
            log.info("Admin deleting contact request with ID: {}", id);
            contactService.deleteContact(id);
            log.info("Contact request deleted successfully with ID: {}", id);

            SuccessResponse response = SuccessResponse.builder()
                    .message("Contact request deleted successfully")
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error deleting contact request with ID: {}", id, e);
            throw e;
        }
    }
}
