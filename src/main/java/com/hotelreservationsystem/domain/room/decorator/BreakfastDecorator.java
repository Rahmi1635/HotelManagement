package com.hotelreservationsystem.domain.room.decorator;

import com.hotelreservationsystem.domain.room.RoomComponent;

public abstract class BreakfastDecorator extends RoomDecorator{

    protected BreakfastDecorator(RoomComponent room) {
        super(room);
    }

    @Override
    public String getDescription() {
        return room.getDescription() + ", kahvaltı dahil";
    }

    @Override
    public double getPricePerNight() {
        return room.getPricePerNight() + 70.0;
    }
}
