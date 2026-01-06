package com.demo.lms.repository;

import com.demo.lms.model.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {

    Optional<Fine> findByBorrowRecordId(Long borrowRecordId);

    List<Fine> findByPaidFalse();
}
