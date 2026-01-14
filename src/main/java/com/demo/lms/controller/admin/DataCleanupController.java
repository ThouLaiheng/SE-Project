package com.demo.lms.controller.admin;

import com.demo.lms.repository.BookCopyRepository;
import com.demo.lms.repository.BorrowRecordRepository;
import com.demo.lms.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Admin controller for cleaning up data
 * WARNING: Use with caution - deletes data permanently
 */
@RestController
@RequestMapping("/api/admin/cleanup")
@RequiredArgsConstructor
@Slf4j
public class DataCleanupController {

    private final ReservationRepository reservationRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final BookCopyRepository bookCopyRepository;

    /**
     * Clear all reservations, borrow records, and book copies
     * This allows books to be deleted without constraint violations
     */
    @PostMapping("/clear-all")
    @Transactional
    public ResponseEntity<Map<String, Object>> clearAllData() {
        log.warn("⚠️ CLEARING ALL DATA - Reservations, Borrow Records, and Book Copies");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Count before deletion
            long reservationsCount = reservationRepository.count();
            long borrowRecordsCount = borrowRecordRepository.count();
            long bookCopiesCount = bookCopyRepository.count();
            
            // Delete in order
            reservationRepository.deleteAll();
            borrowRecordRepository.deleteAll();
            bookCopyRepository.deleteAll();
            
            result.put("success", true);
            result.put("message", "All data cleared successfully");
            result.put("deleted", Map.of(
                "reservations", reservationsCount,
                "borrowRecords", borrowRecordsCount,
                "bookCopies", bookCopiesCount
            ));
            
            log.info("✅ Data cleared: {} reservations, {} borrow records, {} book copies", 
                    reservationsCount, borrowRecordsCount, bookCopiesCount);
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("❌ Error clearing data: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
}
