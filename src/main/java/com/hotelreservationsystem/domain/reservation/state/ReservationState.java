package com.hotelreservationsystem.domain.reservation.state;

public interface ReservationState {
    void confirm();
    void cancel();
    void checkIn();
    void checkOut();
}