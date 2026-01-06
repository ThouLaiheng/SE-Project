package com.demo.lms.repository;

import com.demo.lms.model.entity.BorrowRecord;
import com.demo.lms.model.enums.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByUserId(Long userId);

    Optional<BorrowRecord> findByBookCopyIdAndStatus(
            Long bookCopyId,
            BorrowStatus status
    );
}
