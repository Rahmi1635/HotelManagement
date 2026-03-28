package com.hotelreservationsystem.service.reservation.factory;

import com.hotelreservationsystem.domain.reservation.ReservationStatus;
import com.hotelreservationsystem.service.reservation.strategy.*;

import java.time.LocalDate;
import java.util.Map;

public class ReservationServiceFactory {

    private ReservationServiceFactory() {}

    public static ReservationFilterStrategy createFilterStrategy(
            ReservationFilterType type,
            Object... params
    ) {

        return switch (type) {

            case DATE_RANGE -> new FilterByDateRangeStrategy(
                    (LocalDate) params[0],
                    (LocalDate) params[1]
            );

            case ROOM_NUMBER -> new FilterByRoomNumberStrategy(
                    (int) params[0],
                    (Map<Long, Integer>) params[1]
            );

            case CUSTOMER_NAME -> new FilterByCustomerNameStrategy(
                    (String) params[0],
                    (Map<Long, String>) params[1]
            );

            case STATUS -> new FilterByStatusStrategy(
                    (ReservationStatus) params[0]
            );
        };
    }
}
