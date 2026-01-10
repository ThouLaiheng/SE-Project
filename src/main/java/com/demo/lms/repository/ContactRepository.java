package com.demo.lms.repository;

import com.demo.lms.model.entity.Contact;
import com.demo.lms.model.enums.ContactStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByUserId(Long userId);

    List<Contact> findByStatus(ContactStatus status);

    Page<Contact> findByUserId(Long userId, Pageable pageable);

    Page<Contact> findByStatus(ContactStatus status, Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    List<Contact> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId AND c.status = :status")
    List<Contact> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") ContactStatus status);

    long countByStatus(ContactStatus status);

    long countByUserId(Long userId);
}
