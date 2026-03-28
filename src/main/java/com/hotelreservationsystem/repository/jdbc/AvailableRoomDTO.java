package com.hotelreservationsystem.repository.jdbc;

import java.time.LocalDate;
import java.util.List;

public class AvailableRoomDTO {

    private Long roomId;
    private String roomNumber;
    private String roomType;
    private double price;
    private int capacity;
    private String description;

    public AvailableRoomDTO(Long roomId, String roomNumber, String roomType, double price, int capacity, String description) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.capacity = capacity;
        this.description = description;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Oda " + roomNumber +
                " | " + roomType +
                " | " + capacity + " kişi | " +
                getPrice() + "₺";
    }


}
