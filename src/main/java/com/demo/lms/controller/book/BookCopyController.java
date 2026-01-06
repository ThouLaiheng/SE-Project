package com.demo.lms.controller.book;

import com.demo.lms.dto.request.BookCopyRequest;
import com.demo.lms.dto.response.BookCopyResponse;
import com.demo.lms.service.book.BookCopyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-copies")
@RequiredArgsConstructor
public class BookCopyController {

    private final BookCopyService bookCopyService;

    /**
     * Create book copy (ADMIN)
     */
    @PostMapping
    public ResponseEntity<BookCopyResponse> create(
            @RequestBody BookCopyRequest request
    ) {
        return ResponseEntity.ok(
                BookCopyResponse.from(bookCopyService.create(request))
        );
    }

    /**
     * Get copies by book
     */
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BookCopyResponse>> findByBook(
            @PathVariable Long bookId
    ) {
        return ResponseEntity.ok(
                bookCopyService.findByBook(bookId)
                        .stream()
                        .map(BookCopyResponse::from)
                        .toList()
        );
    }

    /**
     * Update availability
     */
    @PutMapping("/{id}/availability")
    public ResponseEntity<BookCopyResponse> updateAvailability(
            @PathVariable Long id,
            @RequestParam boolean available
    ) {
        return ResponseEntity.ok(
                BookCopyResponse.from(
                        bookCopyService.updateAvailability(id, available)
                )
        );
    }

    /**
     * Delete copy
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookCopyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
