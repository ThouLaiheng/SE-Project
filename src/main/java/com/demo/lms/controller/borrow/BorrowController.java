package com.demo.lms.controller.borrow;

import com.demo.lms.model.entity.BorrowRecord;
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

    @PostMapping("/copy/{copyId}")
    public ResponseEntity<BorrowRecord> borrow(@PathVariable Long copyId) {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(borrowService.borrow(copyId, userId));
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<BorrowRecord> returnBook(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(borrowService.returnBook(id, userId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BorrowRecord>> myBorrows() {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(borrowService.myBorrows(userId));
    }
}
