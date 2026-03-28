package com.hotelreservationsystem.domain.reservation;


import com.hotelreservationsystem.domain.reservation.state.PendingState;
import com.hotelreservationsystem.domain.reservation.state.ReservationState;
import com.hotelreservationsystem.domain.reservation.state.ReservationStateFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class Reservation {

    private Long id;
    private Long customerId;
    private Long roomId;


    private ReservationStatus status;

    private LocalDate plannedCheckIn;
    private LocalDate plannedCheckOut;

    private LocalDateTime actualCheckIn;
    private LocalDateTime actualCheckOut;

    private String notes;
    private BigDecimal totalPrice;

    private transient ReservationState state;

    public Reservation(Long customerId, Long roomId, LocalDate plannedCheckIn,
                       LocalDate plannedCheckOut, BigDecimal totalPrice, String notes) {

        if (plannedCheckIn == null || plannedCheckOut == null) {
            throw new IllegalArgumentException("Planned check-in/out boş olamaz.");
        }
        if (!plannedCheckIn.isBefore(plannedCheckOut)) {
            throw new IllegalArgumentException("Planned check-in, planned check-out'tan önce olmalıdır.");
        }

        this.customerId = customerId;
        this.roomId = roomId;
        this.plannedCheckIn = plannedCheckIn;
        this.plannedCheckOut = plannedCheckOut;
        this.totalPrice = totalPrice;
        this.notes = notes;

        this.status = ReservationStatus.PENDING;
        this.state = new PendingState(this);
    }

    public Reservation(
            Long customerId,
            Long roomId,
            LocalDate plannedCheckIn,
            LocalDate plannedCheckOut,
            BigDecimal totalPrice,
            ReservationStatus status)
    {
        this.customerId=customerId;
        this.roomId=roomId;
        this.plannedCheckIn= plannedCheckIn;
        this.plannedCheckOut=plannedCheckOut;
        this.totalPrice=totalPrice;
        this.notes=null;
        changeStatus(status);
    }


    public void confirm() { state.confirm(); }
    public void cancel()  { state.cancel(); }
    public void checkIn() { state.checkIn(); }
    public void checkOut(){ state.checkOut(); }

    public void changeStatus(ReservationStatus newStatus) {
        this.status = newStatus;
        this.state = ReservationStateFactory.from(newStatus, this);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) {
        this.status = status;
        this.state = ReservationStateFactory.from(status, this);
    }

    public LocalDate getPlannedCheckIn() { return plannedCheckIn; }
    public void setPlannedCheckIn(LocalDate plannedCheckIn) { this.plannedCheckIn = plannedCheckIn; }

    public LocalDate getPlannedCheckOut() { return plannedCheckOut; }
    public void setPlannedCheckOut(LocalDate plannedCheckOut) { this.plannedCheckOut = plannedCheckOut; }

    public LocalDateTime getActualCheckIn() { return actualCheckIn; }
    public void setActualCheckIn(LocalDateTime actualCheckIn) { this.actualCheckIn = actualCheckIn; }

    public LocalDateTime getActualCheckOut() { return actualCheckOut; }
    public void setActualCheckOut(LocalDateTime actualCheckOut) { this.actualCheckOut = actualCheckOut; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }


}