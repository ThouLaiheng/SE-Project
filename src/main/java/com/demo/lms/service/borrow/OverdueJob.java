package com.demo.lms.service.borrow;

import com.demo.lms.model.enums.BorrowStatus;
import com.demo.lms.repository.BorrowRecordRepository;
import com.demo.lms.service.fine.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OverdueJob {

    private final BorrowRecordRepository borrowRecordRepository;
    private final FineService fineService;

    @Scheduled(cron = "0 0 * * * *") // every hour
    public void detectOverdue() {

        borrowRecordRepository.findAll().stream()
                .filter(b -> b.getStatus() == BorrowStatus.BORROWED)
                .filter(b -> b.getDueDate().isBefore(LocalDateTime.now()))
                .forEach(b -> {
                    b.setStatus(BorrowStatus.OVERDUE);
                    fineService.createFineIfNeeded(b);
                });
    }
}
