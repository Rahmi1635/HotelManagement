package com.hotelreservationsystem.domain.reservation.state;

import com.hotelreservationsystem.domain.reservation.Reservation;

public class CancelledState extends BaseReservationState {
    public CancelledState(Reservation reservation) {
        super(reservation);
    }
}