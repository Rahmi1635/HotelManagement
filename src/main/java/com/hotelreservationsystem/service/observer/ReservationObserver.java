package com.hotelreservationsystem.service.observer;

import com.hotelreservationsystem.domain.reservation.Reservation;

public interface ReservationObserver {
    void update(ReservationEventType eventType, Reservation reservation);
}
