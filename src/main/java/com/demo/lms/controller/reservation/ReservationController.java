package com.demo.lms.controller.reservation;

import com.demo.lms.dto.response.ReservationResponse;
import com.demo.lms.model.entity.Reservation;
import com.demo.lms.service.reservation.ReservationService;
import com.demo.lms.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * Reserve a book (USER)
     */
    @PostMapping("/book/{bookId}")
    public ResponseEntity<ReservationResponse> reserveBook(
            @PathVariable Long bookId
    ) {
        Long userId = SecurityUtil.getCurrentUserId();

        Reservation reservation = reservationService.reserveBook(bookId, userId);
        return ResponseEntity.ok(ReservationResponse.from(reservation));
    }

    /**
     * Get my reservations (USER)
     */
    @GetMapping("/my")
    public ResponseEntity<List<ReservationResponse>> getMyReservations() {
        Long userId = SecurityUtil.getCurrentUserId();

        List<ReservationResponse> response = reservationService
                .getMyReservations(userId)
                .stream()
                .map(ReservationResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    /**
     * Cancel reservation (USER)
     */
    @PutMapping("/{reservationId}/cancel")
    public ResponseEntity<ReservationResponse> cancelReservation(
            @PathVariable Long reservationId
    ) {
        Long userId = SecurityUtil.getCurrentUserId();

        Reservation reservation =
                reservationService.cancelReservation(reservationId, userId);

        return ResponseEntity.ok(ReservationResponse.from(reservation));
    }
}
