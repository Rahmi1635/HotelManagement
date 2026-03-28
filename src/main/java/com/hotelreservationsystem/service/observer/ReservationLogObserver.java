package com.hotelreservationsystem.service.observer;

import com.hotelreservationsystem.domain.reservation.Reservation;

public class ReservationLogObserver implements ReservationObserver{

    @Override
    public void update(ReservationEventType eventType, Reservation reservation) {
        System.out.println(
                "[RESERVATION EVENT] " + eventType +
                        " | Reservation ID: " + reservation.getId()
        );
    }
}
