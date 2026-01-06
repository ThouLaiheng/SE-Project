package com.demo.lms.controller.fine;

import com.demo.lms.model.entity.Fine;
import com.demo.lms.repository.FineRepository;
import com.demo.lms.service.fine.FineService;
import com.demo.lms.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
public class FineController {

    private final FineRepository fineRepository;
    private final FineService fineService;

    @GetMapping("/my")
    public ResponseEntity<List<Fine>> myFines() {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(
                fineRepository.findAll().stream()
                        .filter(f -> f.getBorrowRecord().getUser().getId().equals(userId))
                        .toList()
        );
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<Fine> pay(@PathVariable Long id) {
        return ResponseEntity.ok(fineService.payFine(id));
    }
}
