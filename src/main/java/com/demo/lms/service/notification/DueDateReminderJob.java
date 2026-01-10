package com.demo.lms.service.notification;

import com.demo.lms.model.enums.BorrowStatus;
import com.demo.lms.repository.BorrowRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DueDateReminderJob {

    private final BorrowRecordRepository borrowRecordRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 9 * * *") // every day at 9 AM
    public void remindDueSoon() {

        borrowRecordRepository.findAll().stream()
                .filter(b -> b.getStatus() == BorrowStatus.BORROWED)
                .filter(b -> b.getDueDate().isBefore(LocalDateTime.now().plusDays(1)))
                .forEach(b ->
                        notificationService.notifyUser(
                                b.getUser().getId(),
                                "Reminder: '" + b.getBookCopy().getBook().getTitle()
                                        + "' is due tomorrow."
                        )
                );
    }
}
