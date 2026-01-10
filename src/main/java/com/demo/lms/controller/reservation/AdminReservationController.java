package com.demo.lms.controller.reservation;

import com.demo.lms.dto.response.ReservationResponse;
import com.demo.lms.model.entity.Reservation;
import com.demo.lms.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/reservations")
@RequiredArgsConstructor
public class AdminReservationController {

    private final ReservationService reservationService;

    /**
     * Approve reservation (ADMIN)
     */
    @PutMapping("/{id}/approve")
    public ResponseEntity<ReservationResponse> approve(@PathVariable Long id) {

        Reservation reservation = reservationService.approveReservation(id);
        return ResponseEntity.ok(ReservationResponse.from(reservation));
    }
}
