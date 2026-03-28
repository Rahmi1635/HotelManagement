package com.hotelreservationsystem.service.reservation.strategy;

import com.hotelreservationsystem.domain.reservation.Reservation;

import java.util.List;
import java.util.Map;

public class FilterByCustomerNameStrategy implements ReservationFilterStrategy {

    private final String customerName;
    private final Map<Long, String> customerIdNameMap;

    public FilterByCustomerNameStrategy(
            String customerName,
            Map<Long, String> customerIdNameMap
    ) {
        this.customerName = customerName;
        this.customerIdNameMap = customerIdNameMap;
    }

    @Override
    public List<Reservation> filter(List<Reservation> reservations) {
        return reservations.stream()
                .filter(r -> {
                    String name = customerIdNameMap.get(r.getCustomerId());
                    return name != null &&
                            name.equalsIgnoreCase(customerName);
                })
                .toList();
    }

}
