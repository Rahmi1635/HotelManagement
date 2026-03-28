package com.hotelreservationsystem.service.reservation.strategy;

import com.hotelreservationsystem.domain.reservation.Reservation;

import java.time.LocalDate;
import java.util.List;

public class FilterByDateRangeStrategy implements ReservationFilterStrategy{

    private final LocalDate  startDate;
    private  final LocalDate endDate;


    public FilterByDateRangeStrategy(LocalDate startDate,LocalDate endDate)
    {
        this.startDate=startDate;
        this.endDate=endDate;
    }


    @Override
    public List<Reservation> filter(List<Reservation> reservations) {
        return reservations.stream()
                .filter(r->!r.getPlannedCheckIn().isAfter(endDate) &&
                        !r.getPlannedCheckOut().isBefore(startDate)).toList();
    }

}
