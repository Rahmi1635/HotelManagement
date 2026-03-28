package com.hotelreservationsystem.domain.room.decorator;

import com.hotelreservationsystem.domain.room.RoomComponent;

public abstract class ExtraBedDecorator extends RoomDecorator{

    public ExtraBedDecorator(RoomComponent room) {
        super(room);
    }

    @Override
    public String getDescription() {
        return room.getDescription() + ", ekstra yataklı";
    }

    @Override
    public double getPricePerNight() {
        return room.getPricePerNight() + 60.0;
    }
}
