package com.hotelreservationsystem.domain.reservation.state;

import com.hotelreservationsystem.domain.reservation.Reservation;

public abstract class BaseReservationState implements ReservationState {

    protected final Reservation reservation;

    protected BaseReservationState(Reservation reservation) {
        this.reservation = reservation;
    }

    protected void invalid(String action) {
        throw new IllegalStateException(
                "Bu durumda '" + action + "' işlemi yapılamaz. Mevcut durum: " + reservation.getStatus()
        );
    }

    @Override public void confirm() { invalid("confirm"); }
    @Override public void cancel()  { invalid("cancel"); }
    @Override public void checkIn() { invalid("checkIn"); }
    @Override public void checkOut(){ invalid("checkOut"); }
}