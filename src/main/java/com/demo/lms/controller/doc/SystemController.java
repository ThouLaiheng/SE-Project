package com.demo.lms.controller.doc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Test controller to verify Swagger integration and provide API status information
 */
@RestController
@RequestMapping("/api/system")
@Slf4j
@Tag(name = "System Information", description = "System status and information endpoints")
public class SystemController {

    @Operation(
            summary = "Get system status",
            description = "Returns the current status of the Library Management System including server time and health status"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "System status retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        log.info("System status requested");

        Map<String, Object> status = new HashMap<>();
        status.put("service", "Library Management System");
        status.put("version", "1.0.0");
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now());
        status.put("swagger_ui_url", "http://localhost:8080/swagger-ui/index.html");
        status.put("api_docs_url", "http://localhost:8080/api-docs");

        return ResponseEntity.ok(status);
    }

    @Operation(
            summary = "API Documentation Links",
            description = "Returns links to various API documentation endpoints"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documentation links retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/docs")
    public ResponseEntity<Map<String, String>> getDocumentationLinks() {
        log.info("Documentation links requested");

        Map<String, String> links = new HashMap<>();
        links.put("swagger_ui", "/swagger-ui/index.html");
        links.put("openapi_json", "/api-docs");
        links.put("openapi_yaml", "/api-docs.yaml");
        links.put("actuator_health", "/actuator/health");

        return ResponseEntity.ok(links);
    }
}
