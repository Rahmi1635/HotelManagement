package com.hotelreservationsystem.domain.room.decorator;

import com.hotelreservationsystem.domain.room.RoomComponent;

public abstract class HighSpeedInternetDecorator extends RoomDecorator{

    protected HighSpeedInternetDecorator(RoomComponent room) {
        super(room);
    }



    @Override
    public String getDescription() {
        return room.getDescription() + ", hızlı internet erişimi";
    }

    @Override
    public double getPricePerNight() {
        return room.getPricePerNight() + 40.0;
    }

}
