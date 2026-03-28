package com.hotelreservationsystem.service.observer;

import com.hotelreservationsystem.domain.reservation.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationSubject {

    private final List<ReservationObserver> observers = new ArrayList<>();

    public void addObserver(ReservationObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(
            ReservationEventType eventType,
            Reservation reservation
    ) {
        for (ReservationObserver observer : observers) {
            observer.update(eventType, reservation);
        }
    }

}
