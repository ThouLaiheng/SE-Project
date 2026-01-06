package com.demo.lms.service.borrow;

import com.demo.lms.model.entity.BookCopy;
import com.demo.lms.model.entity.BorrowRecord;
import com.demo.lms.model.entity.User;
import com.demo.lms.model.enums.BorrowStatus;
import com.demo.lms.repository.BookCopyRepository;
import com.demo.lms.repository.BorrowRecordRepository;
import com.demo.lms.repository.UserRepository;
import com.demo.lms.service.audit.AuditLogService;
import com.demo.lms.service.notification.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;

    public BorrowRecord borrow(Long bookCopyId, Long userId) {

        BookCopy copy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new EntityNotFoundException("Book copy not found"));

        if (!copy.isAvailable()) {
            throw new IllegalStateException("Book copy is not available");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        copy.setAvailable(false);

        BorrowRecord record = new BorrowRecord();
        record.setUser(user);
        record.setBookCopy(copy);
        record.setBorrowDate(LocalDateTime.now());
        record.setDueDate(LocalDateTime.now().plusDays(14));
        record.setStatus(BorrowStatus.BORROWED);

        BorrowRecord saved = borrowRecordRepository.save(record);

        notificationService.notifyUser(
                userId,
                "You borrowed '" +
                        copy.getBook().getTitle() +
                        "'. Due date: " + record.getDueDate()
        );

        auditLogService.log(
                userId,
                "Borrowed book: " + copy.getBook().getTitle()
        );

        return saved;
    }

    public BorrowRecord returnBook(Long borrowId, Long userId) {

        BorrowRecord record = borrowRecordRepository.findById(borrowId)
                .orElseThrow(() -> new EntityNotFoundException("Borrow record not found"));

        if (!record.getUser().getId().equals(userId)) {
            throw new IllegalStateException("You cannot return this book");
        }

        record.setReturnDate(LocalDateTime.now());

        if (record.getReturnDate().isAfter(record.getDueDate())) {
            record.setStatus(BorrowStatus.OVERDUE);
        } else {
            record.setStatus(BorrowStatus.RETURNED);
        }

        record.getBookCopy().setAvailable(true);
        BorrowRecord saved = borrowRecordRepository.save(record);

        notificationService.notifyUser(
                userId,
                "You returned '" +
                        record.getBookCopy().getBook().getTitle() + "'."
        );

        auditLogService.log(
                userId,
                "Returned book: " +
                        record.getBookCopy().getBook().getTitle()
        );

        return saved;
    }

    @Transactional(readOnly = true)
    public List<BorrowRecord> myBorrows(Long userId) {
        return borrowRecordRepository.findByUserId(userId);
    }
}
