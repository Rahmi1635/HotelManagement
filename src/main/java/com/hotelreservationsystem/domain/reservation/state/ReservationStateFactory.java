package com.hotelreservationsystem.domain.reservation.state;

import com.hotelreservationsystem.domain.reservation.Reservation;
import com.hotelreservationsystem.domain.reservation.ReservationStatus;

public final class ReservationStateFactory {

    private ReservationStateFactory() {}

    public static ReservationState from(ReservationStatus status, Reservation reservation) {
        if (status == null) return new PendingState(reservation);

        return switch (status) {
            case PENDING -> new PendingState(reservation);
            case CONFIRMED -> new ConfirmedState(reservation);
            case CHECKED_IN -> new CheckedInState(reservation);
            case CHECKED_OUT -> new CheckedOutState(reservation);
            case CANCELLED -> new CancelledState(reservation);
        };
    }
}