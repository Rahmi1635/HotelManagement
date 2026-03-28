package com.hotelreservationsystem.domain.room;

public interface RoomComponent {
    Long getRoomId();
    Long getRoomTypeId();
    String getRoomStatus();
    String getRoomNumber();
    String getName();
    String getDescription();
    int getCapacity();
    double getPricePerNight();
}