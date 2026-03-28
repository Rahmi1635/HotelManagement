package com.hotelreservationsystem.service.reservation.strategy;

import com.hotelreservationsystem.domain.reservation.Reservation;

import java.util.List;
import java.util.Map;

public class FilterByRoomNumberStrategy implements ReservationFilterStrategy{

    private final int roomNumber;
    private final Map<Long, Integer> roomIdNumberMap;

    public FilterByRoomNumberStrategy(
            int roomNumber,
            Map<Long, Integer> roomIdNumberMap
    ) {
        this.roomNumber = roomNumber;
        this.roomIdNumberMap = roomIdNumberMap;
    }

    @Override
    public List<Reservation> filter(List<Reservation> reservations) {
        return reservations.stream()
                .filter(r -> {
                    Integer number = roomIdNumberMap.get(r.getRoomId());
                    return number != null && number == roomNumber;
                })
                .toList();
    }

}
