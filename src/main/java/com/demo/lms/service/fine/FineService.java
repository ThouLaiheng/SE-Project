package com.demo.lms.service.fine;

import com.demo.lms.model.entity.BorrowRecord;
import com.demo.lms.model.entity.Fine;
import com.demo.lms.model.enums.BorrowStatus;
import com.demo.lms.repository.FineRepository;
import com.demo.lms.service.audit.AuditLogService;
import com.demo.lms.service.notification.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class FineService {

    private static final BigDecimal DAILY_RATE = BigDecimal.valueOf(0.50);

    private final FineRepository fineRepository;
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;

    public void createFineIfNeeded(BorrowRecord record) {

        if (record.getStatus() != BorrowStatus.OVERDUE) return;

        if (fineRepository.findByBorrowRecordId(record.getId()).isPresent()) return;

        long overdueDays = Duration.between(
                record.getDueDate(),
                LocalDateTime.now()
        ).toDays();

        if (overdueDays <= 0) return;

        BigDecimal amount = DAILY_RATE.multiply(BigDecimal.valueOf(overdueDays));

        Fine fine = new Fine();
        fine.setBorrowRecord(record);
        fine.setAmount(amount);
        fine.setPaid(false);

        fineRepository.save(fine);

        notificationService.notifyUser(
                record.getUser().getId(),
                "You have an overdue fine of $" + amount +
                        " for '" + record.getBookCopy().getBook().getTitle() + "'."
        );
    }

    public Fine payFine(Long fineId) {

        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new EntityNotFoundException("Fine not found"));

        fine.setPaid(true);
        Fine saved = fineRepository.save(fine);

        auditLogService.log(
                fine.getBorrowRecord().getUser().getId(),
                "Paid fine of $" + fine.getAmount()
        );

        return saved;
    }
}
