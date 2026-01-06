package com.demo.lms.repository;

import com.demo.lms.model.entity.Reservation;
import com.demo.lms.model.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Get all reservations of a specific user
     */
    List<Reservation> findByUserId(Long userId);

    /**
     * Get all reservations of a specific book
     */
    List<Reservation> findByBookId(Long bookId);

    /**
     * Check if a user already has an active reservation for a book
     */
    boolean existsByUserIdAndBookIdAndStatusIn(
            Long userId,
            Long bookId,
            List<ReservationStatus> statuses
    );

    /**
     * Find active reservation of a book
     */
    Optional<Reservation> findByBookIdAndStatus(
            Long bookId,
            ReservationStatus status
    );
}
