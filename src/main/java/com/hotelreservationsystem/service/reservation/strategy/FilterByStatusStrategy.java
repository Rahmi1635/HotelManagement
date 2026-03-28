package com.hotelreservationsystem.service.reservation.strategy;

import com.hotelreservationsystem.domain.reservation.Reservation;
import com.hotelreservationsystem.domain.reservation.ReservationStatus;

import java.util.List;

public class FilterByStatusStrategy implements  ReservationFilterStrategy {

    private final ReservationStatus status;

    public FilterByStatusStrategy(ReservationStatus status)
    {
        this.status=status;
    }

    @Override
    public List<Reservation> filter(List<Reservation> reservations) {
        return reservations.stream()
                .filter(statu->statu.getStatus()==status)
                .toList();
    }
}
