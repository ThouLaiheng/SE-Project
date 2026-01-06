package com.demo.lms.controller.contact;

import com.demo.lms.dto.request.CreateContactRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Contact Management", description = "APIs for managing user contacts and support requests")
@SecurityRequirement(name = "Bearer Authentication")
public class ContactController {

    private final ContactService contactService;

    @Operation(
            summary = "Create a new contact/support request",
            description = "Creates a new contact or support request from a user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contact request created successfully",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PostMapping
    public ResponseEntity<SuccessResponse> create(
            @Parameter(description = "User ID who is creating the contact request")
            @RequestParam Long userId,
            @Valid @RequestBody CreateContactRequest request) {
        try {
            log.info("Creating contact request for user ID: {}", userId);
            ContactResponse contactResponse = contactService.createContact(userId, request);
            log.info("Contact request created successfully with ID: {}", contactResponse.getId());

            SuccessResponse response = SuccessResponse.builder()
                    .message("Contact request created successfully")
                    .data(contactResponse)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creating contact request for user ID: {}", userId, e);
            throw e;
        }
    }

    @Operation(
            summary = "Get user's contact requests",
            description = "Retrieve all contact requests submitted by a specific user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved contact requests",
                    content = @Content(schema = @Schema(implementation = ContactResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @GetMapping("/my")
    public ResponseEntity<List<ContactResponse>> myContacts(
            @Parameter(description = "User ID to retrieve contact requests for")
            @RequestParam Long userId) {
        try {
            log.info("Fetching contact requests for user ID: {}", userId);
            List<ContactResponse> contacts = contactService.getMyContacts(userId);
            log.info("Found {} contact requests for user ID: {}", contacts.size(), userId);
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            log.error("Error fetching contact requests for user ID: {}", userId, e);
            throw e;
        }
    }
}
