package com.demo.lms.controller.borrow;

import com.demo.lms.dto.response.BorrowResponse;
import com.demo.lms.model.entity.BorrowRecord;
import com.demo.lms.repository.BookCopyRepository;
import com.demo.lms.service.borrow.BorrowService;
import com.demo.lms.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;
    private final BookCopyRepository bookCopyRepository;

    @PostMapping("/copy/{copyId}")
    public ResponseEntity<BorrowResponse> borrow(
            @PathVariable Long copyId,
            @RequestParam(defaultValue = "14") int borrowDays
    ) {
        Long userId = SecurityUtil.getCurrentUserId();
        BorrowRecord record = borrowService.borrow(copyId, userId, borrowDays);
        return ResponseEntity.ok(BorrowResponse.from(record));
    }

    /**
     * Borrow the first available copy for a given book
     */
    @PostMapping("/book/{bookId}")
    public ResponseEntity<BorrowResponse> borrowByBook(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "14") int borrowDays
    ) {
        Long userId = SecurityUtil.getCurrentUserId();
        var copies = bookCopyRepository.findByBookIdAndAvailableTrue(bookId);
        if (copies.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        BorrowRecord record = borrowService.borrow(copies.get(0).getId(), userId, borrowDays);
        return ResponseEntity.ok(BorrowResponse.from(record));
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<BorrowResponse> returnBook(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        BorrowRecord record = borrowService.returnBook(id, userId);
        return ResponseEntity.ok(BorrowResponse.from(record));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BorrowResponse>> myBorrows() {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(
                borrowService.myBorrows(userId)
                        .stream()
                        .map(BorrowResponse::from)
                        .toList()
        );
    }
    
    @GetMapping
    public ResponseEntity<List<BorrowResponse>> allBorrows() {
        return ResponseEntity.ok(
                borrowService.allBorrows()
                        .stream()
                        .map(BorrowResponse::from)
                        .toList()
        );
    }
    
    @PutMapping("/{id}/unreturned")
    public ResponseEntity<BorrowResponse> markAsUnreturned(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        BorrowRecord record = borrowService.markAsUnreturned(id, userId);
        return ResponseEntity.ok(BorrowResponse.from(record));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrowRecord(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        borrowService.deleteBorrowRecord(id, userId);
        return ResponseEntity.noContent().build();
    }
}
