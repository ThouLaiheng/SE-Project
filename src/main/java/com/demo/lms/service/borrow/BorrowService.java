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

    public BorrowRecord borrow(Long bookCopyId, Long userId, int borrowDays) {

        BookCopy copy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new EntityNotFoundException("Book copy not found"));

        if (!copy.isAvailable()) {
            throw new IllegalStateException("Book copy is not available");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        // Check if user has already borrowed 5 books (limit)
        long currentBorrowCount = borrowRecordRepository.countByUserIdAndStatus(userId, BorrowStatus.BORROWED);
        if (currentBorrowCount >= 5) {
            throw new IllegalStateException("You have reached the maximum limit of 5 borrowed books. Please return a book before borrowing more.");
        }

        copy.setAvailable(false);

        BorrowRecord record = new BorrowRecord();
        record.setUser(user);
        record.setBookCopy(copy);
        record.setBorrowDate(LocalDateTime.now());
        record.setDueDate(LocalDateTime.now().plusDays(borrowDays));
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

        // Allow any authenticated user (admin/librarian) to mark books as returned
        // No ownership check needed for librarians/admins managing student borrowings

        record.setReturnDate(LocalDateTime.now());
        
        // When a book is returned, it should always be marked as RETURNED
        // regardless of whether it was returned late or on time
        record.setStatus(BorrowStatus.RETURNED);

        record.getBookCopy().setAvailable(true);
        BorrowRecord saved = borrowRecordRepository.save(record);

        notificationService.notifyUser(
                record.getUser().getId(),
                "Your book '" +
                        record.getBookCopy().getBook().getTitle() + "' has been marked as returned."
        );

        auditLogService.log(
                userId,
                "Marked book as returned: " +
                        record.getBookCopy().getBook().getTitle() + 
                        " (Student: " + record.getUser().getName() + ")"
        );

        return saved;
    }

    @Transactional(readOnly = true)
    public List<BorrowRecord> myBorrows(Long userId) {
        return borrowRecordRepository.findByUserId(userId);
    }
    
    @Transactional(readOnly = true)
    public List<BorrowRecord> allBorrows() {
        return borrowRecordRepository.findAll();
    }
    
    public BorrowRecord markAsUnreturned(Long borrowId, Long userId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowId)
                .orElseThrow(() -> new EntityNotFoundException("Borrow record not found"));
        
        // Set book copy as unavailable again
        record.getBookCopy().setAvailable(false);
        
        // Reset return date and status
        record.setReturnDate(null);
        record.setStatus(BorrowStatus.BORROWED);
        
        BorrowRecord saved = borrowRecordRepository.save(record);
        
        notificationService.notifyUser(
                record.getUser().getId(),
                "Your book '" + record.getBookCopy().getBook().getTitle() + "' has been marked as unreturned."
        );
        
        auditLogService.log(
                userId,
                "Marked book as unreturned: " + record.getBookCopy().getBook().getTitle() + 
                " (Student: " + record.getUser().getName() + ")"
        );
        
        return saved;
    }
    
    public void deleteBorrowRecord(Long borrowId, Long userId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowId)
                .orElseThrow(() -> new EntityNotFoundException("Borrow record not found"));
        
        // Allow any authenticated user (librarian/admin) to delete returned records
        // No ownership check needed for managing student borrowings
        
        // Only allow deletion of returned records
        if (record.getReturnDate() == null) {
            throw new IllegalStateException("Cannot delete an active borrow record. Return the book first.");
        }
        
        auditLogService.log(
                userId,
                "Deleted borrow record: " + record.getBookCopy().getBook().getTitle() + 
                " (Student: " + record.getUser().getName() + ")"
        );
        
        borrowRecordRepository.delete(record);
    }
}
