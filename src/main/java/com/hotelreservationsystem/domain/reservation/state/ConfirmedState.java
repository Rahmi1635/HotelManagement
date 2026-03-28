package com.hotelreservationsystem.domain.reservation.state;

import com.hotelreservationsystem.domain.reservation.Reservation;
import com.hotelreservationsystem.domain.reservation.ReservationStatus;

import java.time.LocalDateTime;

public class ConfirmedState extends BaseReservationState {

    public ConfirmedState(Reservation reservation) {
        super(reservation);
    }

    @Override
    public void cancel() {
        reservation.changeStatus(ReservationStatus.CANCELLED);
    }

    @Override
    public void checkIn() {
        reservation.setActualCheckIn(LocalDateTime.now());
        reservation.changeStatus(ReservationStatus.CHECKED_IN);
    }
}