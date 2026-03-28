package com.hotelreservationsystem.domain.room.decorator;

import com.hotelreservationsystem.domain.room.RoomComponent;

public abstract class SeaViewDecorator extends RoomDecorator {

    public SeaViewDecorator(RoomComponent room) {
        super(room);
    }

    @Override
    public String getDescription() {
        return room.getDescription() + ", deniz manzaralı";
    }

    @Override
    public double getPricePerNight() {
        return room.getPricePerNight() + 50.0;
    }
}