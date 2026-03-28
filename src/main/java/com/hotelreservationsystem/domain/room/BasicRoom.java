package com.hotelreservationsystem.domain.room;

public class BasicRoom implements RoomComponent{

    private long room_id;
    private String room_number;
    private RoomStatus room_status;
    private RoomType room_type;


    public BasicRoom(String room_number,RoomStatus status,RoomType roomType)
    {
        this.room_number=room_number;
        this.room_status=status;
        this.room_type=roomType;
    }

    public BasicRoom(long id, String room_number,RoomStatus status,RoomType roomType)
    {
        this.room_id = id;
        this.room_number=room_number;
        this.room_status=status;
        this.room_type=roomType;
    }

    public void showInfo()
    {
        System.out.println("Room #" + room_number + " - " + room_type.getName());
        System.out.println("Status: " + room_status.name());
        System.out.println("Base price: " + room_type.getBasePrice());
        System.out.println("Capacity: " + room_type.getCapacity());
        System.out.println("Description: " + room_type.getDescription());
    }

    @Override
    public Long getRoomId() {
        return room_id;
    }

    public void setRoomId(Long room_id){
        this.room_id = room_id;
    }

    @Override
    public Long getRoomTypeId() {
        return room_type.getId();
    }

    @Override
    public String getRoomStatus() {
        return room_status.name();
    }

    @Override
    public String getRoomNumber() {
        return room_number;
    }

    @Override
    public String getName() {
        return room_type.getName();
    }

    @Override
    public String getDescription() {
        return room_type.getDescription();
    }

    @Override
    public int getCapacity() {
        return room_type.getCapacity();
    }

    @Override
    public double getPricePerNight() {
        return room_type.getBasePrice();
    }

    public String toString() {
        return room_number + " (" + room_type + ")";
    }
}
