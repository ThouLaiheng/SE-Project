package com.demo.lms.service.audit;

import com.demo.lms.model.entity.AuditLog;
import com.demo.lms.model.entity.User;
import com.demo.lms.repository.AuditLogRepository;
import com.demo.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    /**
     * Log user action
     */
    public void log(Long userId, String action) {

        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setCreatedAt(LocalDateTime.now());

        if (userId != null) {
            userRepository.findById(userId).ifPresent(log::setUser);
        }

        auditLogRepository.save(log);
    }
}
