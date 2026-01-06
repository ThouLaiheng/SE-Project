package com.demo.lms.service.reservation;

import com.demo.lms.model.enums.ReservationStatus;
import com.demo.lms.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ReservationExpiryJob {

    private final ReservationRepository reservationRepository;

    @Scheduled(cron = "0 0 * * * *") // every hour
    public void expireReservations() {

        reservationRepository.findAll().stream()
                .filter(r -> r.getStatus() == ReservationStatus.PENDING)
                .filter(r -> r.getExpiryDate() != null)
                .filter(r -> r.getExpiryDate().isBefore(LocalDateTime.now()))
                .forEach(r -> {
                    r.setStatus(ReservationStatus.EXPIRED);
                    reservationRepository.save(r);
                });
    }
}
