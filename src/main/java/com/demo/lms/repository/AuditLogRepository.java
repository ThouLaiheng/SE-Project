package com.demo.lms.repository;

import com.demo.lms.model.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findByUserIdAndActionContainingIgnoreCaseAndCreatedAtBetween(
            Long userId,
            String action,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    Page<AuditLog> findByActionContainingIgnoreCaseAndCreatedAtBetween(
            String action,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    List<AuditLog> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Modifying
    @Transactional
    void deleteByUserId(Long userId);
}
