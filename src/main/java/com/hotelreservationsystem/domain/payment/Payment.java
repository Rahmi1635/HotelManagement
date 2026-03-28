package com.hotelreservationsystem.domain.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {

    private Long id;

    private Long reservationId;

    private BigDecimal amount;
    private PaymentMethod method;
    private LocalDateTime paidAt;

    public Payment(Long reservationId, BigDecimal amount, PaymentMethod method) {

        if (reservationId == null) {
            throw new IllegalArgumentException("ReservationId boş olamaz.");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Ödeme tutarı pozitif olmalıdır.");
        }
        if (method == null) {
            throw new IllegalArgumentException("Ödeme yöntemi seçilmelidir.");
        }

        this.reservationId = reservationId;
        this.amount = amount;
        this.method = method;
        this.paidAt = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Ödeme tutarı pozitif olmalıdır.");
        }
        this.amount = amount;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("Ödeme yöntemi boş olamaz.");
        }
        this.method = method;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}
