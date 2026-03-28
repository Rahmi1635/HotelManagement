package com.hotelreservationsystem.service.reservation.strategy;

import com.hotelreservationsystem.domain.reservation.Reservation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterByRoomTypeStrategy implements ReservationFilterStrategy{

    private final String roomType;
    private final Map<Long, String> roomIdTypeMap;

    public FilterByRoomTypeStrategy(String roomType,
                                    Map<Long, String> roomIdTypeMap) {
        this.roomType = roomType.toLowerCase();
        this.roomIdTypeMap = roomIdTypeMap;
    }

    @Override
    public List<Reservation> filter(List<Reservation> reservations) {

        return reservations.stream()
                .filter(r -> {
                    String type = roomIdTypeMap.get(r.getRoomId());
                    return type != null &&
                            type.toLowerCase().contains(roomType);
                })
                .collect(Collectors.toList());
    }

}
