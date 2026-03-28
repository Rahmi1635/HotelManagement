package com.hotelreservationsystem.service.reservation.strategy;

import com.hotelreservationsystem.domain.reservation.Reservation;

import java.util.List;

public interface ReservationFilterStrategy {

    List<Reservation> filter(List<Reservation> reservations);

}
