package com.hotelreservationsystem.domain.reservation.state;

import com.hotelreservationsystem.domain.reservation.Reservation;

public class CheckedOutState extends BaseReservationState {
    public CheckedOutState(Reservation reservation) {
        super(reservation);
    }
}
