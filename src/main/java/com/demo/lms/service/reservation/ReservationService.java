package com.demo.lms.service.reservation;

import com.demo.lms.model.entity.Book;
import com.demo.lms.model.entity.Reservation;
import com.demo.lms.model.entity.User;
import com.demo.lms.model.enums.ReservationStatus;
import com.demo.lms.repository.BookRepository;
import com.demo.lms.repository.ReservationRepository;
import com.demo.lms.repository.UserRepository;
import com.demo.lms.service.audit.AuditLogService;
import com.demo.lms.service.notification.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;

    public Reservation reserveBook(Long bookId, Long userId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        boolean alreadyReserved = reservationRepository
                .existsByUserIdAndBookIdAndStatusIn(
                        userId,
                        bookId,
                        List.of(
                                ReservationStatus.PENDING,
                                ReservationStatus.APPROVED
                        )
                );

        if (alreadyReserved) {
            throw new IllegalStateException("You already have an active reservation for this book");
        }

        Reservation reservation = new Reservation();
        reservation.setBook(book);
        reservation.setUser(user);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.PENDING);

        Reservation saved = reservationRepository.save(reservation);

        auditLogService.log(
                userId,
                "Reserved book: " + book.getTitle()
        );

        return saved;
    }

    @Transactional(readOnly = true)
    public List<Reservation> getMyReservations(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    public Reservation approveReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("Only PENDING reservations can be approved");
        }

        reservation.setStatus(ReservationStatus.APPROVED);
        Reservation saved = reservationRepository.save(reservation);

        notificationService.notifyUser(
                reservation.getUser().getId(),
                "Your reservation for '" +
                        reservation.getBook().getTitle() +
                        "' has been approved."
        );

        auditLogService.log(
                reservation.getUser().getId(),
                "Reservation approved for book: " +
                        reservation.getBook().getTitle()
        );

        return saved;
    }

    public Reservation cancelReservation(Long reservationId, Long userId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new IllegalStateException("You are not allowed to cancel this reservation");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        Reservation saved = reservationRepository.save(reservation);

        auditLogService.log(
                userId,
                "Cancelled reservation for book: " +
                        reservation.getBook().getTitle()
        );

        return saved;
    }
}
