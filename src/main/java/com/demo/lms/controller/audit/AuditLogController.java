package com.demo.lms.controller.audit;

import com.demo.lms.model.entity.AuditLog;
import com.demo.lms.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    @GetMapping
    public ResponseEntity<Page<AuditLog>> getLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "") String action,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        LocalDateTime start = parse(startDate, LocalDateTime.MIN);
        LocalDateTime end = parse(endDate, LocalDateTime.now());

        PageRequest pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        if (userId != null) {
            return ResponseEntity.ok(
                    auditLogRepository
                            .findByUserIdAndActionContainingIgnoreCaseAndCreatedAtBetween(
                                    userId, action, start, end, pageable
                            )
            );
        }

        return ResponseEntity.ok(
                auditLogRepository
                        .findByActionContainingIgnoreCaseAndCreatedAtBetween(
                                action, start, end, pageable
                        )
        );
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportCsv(
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {

        LocalDateTime start = parse(startDate, LocalDateTime.MIN);
        LocalDateTime end = parse(endDate, LocalDateTime.now());

        List<AuditLog> logs = auditLogRepository.findByCreatedAtBetween(start, end);

        StringBuilder csv = new StringBuilder();
        csv.append("ID,UserId,Action,CreatedAt\n");

        for (AuditLog log : logs) {
            csv.append(log.getId()).append(",");
            csv.append(log.getUser() != null ? log.getUser().getId() : "").append(",");
            csv.append("\"").append(log.getAction()).append("\",");
            csv.append(log.getCreatedAt()).append("\n");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=audit_logs.csv")
                .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                .body(csv.toString());
    }

    private LocalDateTime parse(String value, LocalDateTime defaultValue) {
        if (value == null || value.isBlank()) return defaultValue;
        return LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
    }
}
