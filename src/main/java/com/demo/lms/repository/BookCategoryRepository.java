package com.demo.lms.repository;

import com.demo.lms.model.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {

    /**
     * Find category by name (useful for validation)
     */
    Optional<BookCategory> findByName(String name);

    /**
     * Check if category name already exists
     */
    boolean existsByName(String name);
}
