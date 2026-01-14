package com.demo.lms.repository;

import com.demo.lms.model.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    
    /**
     * Delete all copies of a book using native query
     */
    @Modifying
    @Query(value = "DELETE FROM book_copies WHERE book_id = :bookId", nativeQuery = true)
    void deleteByBookId(@Param("bookId") Long bookId);
}
