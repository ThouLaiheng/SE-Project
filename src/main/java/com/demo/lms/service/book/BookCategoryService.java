package com.demo.lms.service.book;

import com.demo.lms.model.entity.BookCategory;
import com.demo.lms.repository.BookCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookCategoryService {

    private final BookCategoryRepository categoryRepository;

    /**
     * Create new category
     */
    public BookCategory create(BookCategory category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalStateException("Category name already exists");
        }
        return categoryRepository.save(category);
    }

    /**
     * Get all categories
     */
    @Transactional(readOnly = true)
    public List<BookCategory> findAll() {
        return categoryRepository.findAll();
    }

    /**
     * Get category by id
     */
    @Transactional(readOnly = true)
    public BookCategory findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    /**
     * Update category
     */
    public BookCategory update(Long id, BookCategory request) {
        BookCategory category = findById(id);

        if (!category.getName().equals(request.getName())
                && categoryRepository.existsByName(request.getName())) {
            throw new IllegalStateException("Category name already exists");
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        return categoryRepository.save(category);
    }

    /**
     * Delete category
     */
    public void delete(Long id) {
        BookCategory category = findById(id);
        categoryRepository.delete(category);
    }
}
