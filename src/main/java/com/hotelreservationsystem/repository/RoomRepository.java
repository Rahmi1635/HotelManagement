package com.hotelreservationsystem.repository;

import com.hotelreservationsystem.domain.room.BasicRoom;
import com.hotelreservationsystem.domain.room.RoomStatus;
import com.hotelreservationsystem.domain.room.RoomType;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RoomRepository {

    BasicRoom saveRoom(BasicRoom room);

    BasicRoom updateRoom(BasicRoom room);

    void deleteRoomById(long id);

    List<BasicRoom> findAllRooms();

    Optional<BasicRoom> findRoomById(long id);

    List<BasicRoom> findAvailableRoomsByDate(LocalDate checkIn, LocalDate checkOut);

    List<BasicRoom> findAvailableRoomsByCapacity(int capacity);

    List<BasicRoom> findAvailableRoomsByType(RoomType type);

    List<BasicRoom> findAvailableRooms(LocalDate checkIn, LocalDate checkOut, int capacity, RoomType type);

    Optional<BasicRoom> findRoomByNumber(String roomNumber);

    List<BasicRoom> findRoomsByStatus(RoomStatus status);

    public BigDecimal findDailyPriceByRoomId(Long roomId);

    void updateRoomStatus(Long roomId, RoomStatus status);

    Map<Long, String> findRoomIdTypeMap();
}
