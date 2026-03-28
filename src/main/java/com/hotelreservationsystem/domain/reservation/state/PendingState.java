package com.hotelreservationsystem.domain.reservation.state;

import com.hotelreservationsystem.domain.reservation.Reservation;
import com.hotelreservationsystem.domain.reservation.ReservationStatus;

public class PendingState extends BaseReservationState {

    public PendingState(Reservation reservation) {
        super(reservation);
    }

    @Override
    public void confirm() {
        reservation.changeStatus(ReservationStatus.CONFIRMED);
    }

    @Override
    public void cancel() {
        reservation.changeStatus(ReservationStatus.CANCELLED);
    }
}