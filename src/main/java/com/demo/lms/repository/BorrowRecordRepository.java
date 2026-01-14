package com.demo.lms.repository;

import com.demo.lms.model.entity.BorrowRecord;
import com.demo.lms.model.enums.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByUserId(Long userId);
    
    long countByUserIdAndStatus(Long userId, BorrowStatus status);

    Optional<BorrowRecord> findByBookCopyIdAndStatus(
            Long bookCopyId,
            BorrowStatus status
    );
    
    List<BorrowRecord> findByBookCopyId(Long bookCopyId);
    
    boolean existsByBookCopyId(Long bookCopyId);
    
    @Modifying
    @Query(value = "DELETE FROM borrow_records WHERE book_copy_id IN (SELECT id FROM book_copies WHERE book_id = :bookId)", nativeQuery = true)
    void deleteByBookId(@Param("bookId") Long bookId);
}
