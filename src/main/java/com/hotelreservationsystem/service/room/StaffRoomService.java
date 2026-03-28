package com.hotelreservationsystem.service.room;

import com.hotelreservationsystem.config.SingletonDatabaseManager;
import com.hotelreservationsystem.domain.room.BasicRoom;
import com.hotelreservationsystem.domain.room.RoomStatus;
import com.hotelreservationsystem.domain.room.RoomType;
import com.hotelreservationsystem.repository.jdbc.JdbcRoomRepository;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public class StaffRoomService {

    private final Connection connection =
            SingletonDatabaseManager.getInstance().getConnection();

    private final JdbcRoomRepository roomRepository = new JdbcRoomRepository();

    public List<RoomType> getRoomTypes() {
        return Arrays.asList(RoomType.values());
    }

    public void createRoom(String roomNumber, RoomType roomType) {

        if (roomNumber == null || roomNumber.trim().isEmpty())
            throw new IllegalArgumentException("Room number cannot be empty.");

        if (roomType == null)
            throw new IllegalArgumentException("Room type must be selected.");

        String rn = roomNumber.trim();

        try {
            connection.setAutoCommit(false);

            if (roomRepository.findRoomByNumber(rn).isPresent()) {
                throw new IllegalStateException("Room number already exists: " + rn);
            }

            BasicRoom room = new BasicRoom(rn, RoomStatus.AVAILABLE, roomType);

            roomRepository.saveRoom(room);

            connection.commit();

        } catch (Exception e) {
            try { connection.rollback(); } catch (Exception ignored) {}

            Throwable root = e;
            while (root.getCause() != null) root = root.getCause();

            throw new RuntimeException("Create room failed: " + root.getMessage(), e);

        } finally {
            try { connection.setAutoCommit(true); } catch (Exception ignored) {}
        }
    }
}