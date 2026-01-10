package com.demo.lms.controller.book;

import com.demo.lms.model.entity.BookCategory;
import com.demo.lms.service.book.BookCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-categories")
@RequiredArgsConstructor
public class BookCategoryController {

    private final BookCategoryService categoryService;

    /**
     * Create category
     */
    @PostMapping
    public ResponseEntity<BookCategory> create(@RequestBody BookCategory request) {
        return ResponseEntity.ok(categoryService.create(request));
    }

    /**
     * Get all categories
     */
    @GetMapping
    public ResponseEntity<List<BookCategory>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    /**
     * Get category by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookCategory> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    /**
     * Update category
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookCategory> update(
            @PathVariable Long id,
            @RequestBody BookCategory request
    ) {
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    /**
     * Delete category
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
