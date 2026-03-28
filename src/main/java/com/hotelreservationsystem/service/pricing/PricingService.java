package com.hotelreservationsystem.service.pricing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PricingService {

    public BigDecimal calculateTotalPrice(LocalDate checkIn,LocalDate checkOut,BigDecimal dailyPrice)
    {
        long days= ChronoUnit.DAYS.between(checkIn,checkOut);
        if(days<=0)
        {
            throw new IllegalArgumentException("Check-out must be after check-in");
        }

        return dailyPrice.multiply(BigDecimal.valueOf(days));
    }

}
