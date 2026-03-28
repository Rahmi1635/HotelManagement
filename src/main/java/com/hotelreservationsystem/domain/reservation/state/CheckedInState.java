package com.hotelreservationsystem.domain.reservation.state;

import com.hotelreservationsystem.domain.reservation.Reservation;
import com.hotelreservationsystem.domain.reservation.ReservationStatus;

import java.time.LocalDateTime;

public class CheckedInState extends BaseReservationState {

    public CheckedInState(Reservation reservation) {
        super(reservation);
    }

    @Override
    public void checkOut() {
        reservation.setActualCheckOut(LocalDateTime.now());
        reservation.changeStatus(ReservationStatus.CHECKED_OUT);
    }
}