package com.hotelreservationsystem.domain.room.decorator;

import com.hotelreservationsystem.domain.room.RoomComponent;

public abstract class RoomDecorator implements RoomComponent {

    protected final RoomComponent room;

    protected RoomDecorator(RoomComponent room) {
        this.room = room;
    }

    @Override
    public Long getRoomId() {
        return room.getRoomId();
    }

   /* @Override
    public Long getId() { return room.getId(); }*/

    @Override
    public String getRoomNumber() { return room.getRoomNumber(); }

    @Override
    public String getName() { return room.getName(); }

    @Override
    public int getCapacity() { return room.getCapacity(); }

    @Override
    public String getDescription() { return room.getDescription(); }

    @Override
    public double getPricePerNight() { return room.getPricePerNight(); }
}
