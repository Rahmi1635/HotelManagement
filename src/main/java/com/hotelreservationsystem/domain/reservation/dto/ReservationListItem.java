package com.hotelreservationsystem.domain.reservation.dto;

import com.hotelreservationsystem.domain.reservation.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReservationListItem {
    private final Long reservationId;
    private final String customerFullName;
    private final String roomNumber;
    private final String roomType;
    private final LocalDate plannedCheckIn;
    private final LocalDate plannedCheckOut;
    private final String status;
    private final BigDecimal totalPrice;

    public ReservationListItem(Long reservationId,
                               String customerFullName,
                               String roomNumber,
                               String roomType,
                               LocalDate plannedCheckIn,
                               LocalDate plannedCheckOut,
                               String status,
                               BigDecimal totalPrice) {
        this.reservationId = reservationId;
        this.customerFullName = customerFullName;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.plannedCheckIn = plannedCheckIn;
        this.plannedCheckOut = plannedCheckOut;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public static ReservationListItem fromReservation(Reservation r) {

        return new ReservationListItem(
                r.getId(),
                String.valueOf(r.getCustomerId()),
                String.valueOf(r.getRoomId()),
                "",
                r.getPlannedCheckIn(),
                r.getPlannedCheckOut(),
                r.getStatus().name(),
                r.getTotalPrice()
        );
    }


    public Long getReservationId() { return reservationId; }
    public String getCustomerFullName() { return customerFullName; }
    public String getRoomNumber() { return roomNumber; }
    public String getRoomType() { return roomType; }
    public LocalDate getPlannedCheckIn() { return plannedCheckIn; }
    public LocalDate getPlannedCheckOut() { return plannedCheckOut; }
    public String getStatus() { return status; }
    public BigDecimal getTotalPrice() { return totalPrice; }


}
