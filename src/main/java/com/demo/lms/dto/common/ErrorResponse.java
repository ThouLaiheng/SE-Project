package com.demo.lms.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standardized error response DTO for API error handling.
 * Provides consistent error format across all endpoints.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * Timestamp when the error occurred
     */
    private LocalDateTime timestamp;

    /**
     * HTTP status code
     */
    private int status;

    /**
     * Error type/category
     */
    private String error;

    /**
     * Human-readable error message
     */
    private String message;

    /**
     * Optional field-level validation errors
     */
    private Map<String, String> fieldErrors;

    /**
     * Optional request path where error occurred
     */
    private String path;
}

