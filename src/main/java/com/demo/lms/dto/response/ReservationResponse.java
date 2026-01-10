package com.demo.lms.dto.response;

import com.demo.lms.model.entity.Reservation;
import com.demo.lms.model.enums.ReservationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReservationResponse {

    private Long id;
    private Long bookId;
    private String bookTitle;
    private ReservationStatus status;
    private LocalDateTime reservationDate;

    public static ReservationResponse from(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .bookId(reservation.getBook().getId())
                .bookTitle(reservation.getBook().getTitle())
                .status(reservation.getStatus())
                .reservationDate(reservation.getReservationDate())
                .build();
    }
}
