package com.demo.lms.repository;

import com.demo.lms.model.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {

    /**
     * Find all copies of a book
     */
    List<BookCopy> findByBookId(Long bookId);

    /**
     * Find available copies of a book
     */
    List<BookCopy> findByBookIdAndAvailableTrue(Long bookId);
}
