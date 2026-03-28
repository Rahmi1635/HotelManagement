package com.hotelreservationsystem.repository.jdbc;

import com.hotelreservationsystem.config.SingletonDatabaseManager;
import com.hotelreservationsystem.domain.room.BasicRoom;
import com.hotelreservationsystem.domain.room.RoomStatus;
import com.hotelreservationsystem.domain.room.RoomType;
import com.hotelreservationsystem.repository.RoomRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class JdbcRoomRepository implements RoomRepository {

    private final Connection connection;

    public JdbcRoomRepository() {
        this.connection = SingletonDatabaseManager.getInstance().getConnection();
    }

    public void updateStatus(Long roomId, String status) {

        final String sql = "UPDATE room SET status = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, roomId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private BasicRoom mapRowToBasicRoom(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String room_number = rs.getString("room_number");
        String statusString = rs.getString("status");
        Long room_type_id = rs.getLong("room_type_id");
        RoomType roomType = RoomType.fromId(room_type_id);

        RoomStatus status;
        if ("RESERVED".equalsIgnoreCase(statusString)) {
            status = RoomStatus.RESERVED;
        } else if ("OCCUPIED".equalsIgnoreCase(statusString)) {
            status = RoomStatus.OCCUPIED;
        } else {
            status = RoomStatus.AVAILABLE;
        }

        return new BasicRoom(id, room_number, status, roomType);
    }

    @Override
    public BasicRoom saveRoom(BasicRoom room) {
        String sql = "INSERT INTO room (room_number, room_type_id, status) VALUES(?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, room.getRoomNumber());
            ps.setLong(2, room.getRoomTypeId());
            ps.setString(3, room.getRoomStatus());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long generatedId = rs.getLong(1);
                    room.setRoomId(generatedId);
                }
            }
            return room;

        }catch (SQLException e){
            throw new RuntimeException("Error saving room: " + e.getMessage(), e);
        }
    }

    @Override
    public BasicRoom updateRoom(BasicRoom room) {
        String sql = "UPDATE room SET room_number = ?, room_type_id = ?, status = ?  WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, room.getRoomNumber());
            ps.setLong(2, room.getRoomTypeId());
            ps.setString(3, room.getRoomStatus());
            ps.setLong(4, room.getRoomId());

            ps.executeUpdate();

            return room;

        }catch (SQLException e){
            throw new RuntimeException("Error updating room: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteRoomById(long id) {
        String sql = "DELETE FROM room WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setLong(1, id);

            ps.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException("Error deleting room: " + e.getMessage(), e);
        }
    }

    @Override
    public List<BasicRoom> findAllRooms() {
        String sql = "SELECT * FROM room";
        List<BasicRoom> rooms = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){

            while (rs.next()){
                BasicRoom r = mapRowToBasicRoom(rs);
                rooms.add(r);
            }
            return rooms;

        }catch (SQLException e){
            throw new RuntimeException("Error finding all rooms: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<BasicRoom> findRoomById(long id) {
        String sql = "SELECT * FROM room WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    BasicRoom r = mapRowToBasicRoom(rs);
                    return Optional.of(r);
                } else {
                    return Optional.empty();
                }
            }

        }catch (SQLException e){
            throw new RuntimeException("Error finding room by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<BasicRoom> findAvailableRoomsByDate(LocalDate checkIn, LocalDate checkOut) {
        String sql = """
                SELECT r.*
                FROM room r
                WHERE r.status = 'AVAILABLE'
                  AND NOT EXISTS (
                      SELECT 1
                      FROM reservation res
                      WHERE res.room_id = r.id
                        AND NOT (res.planned_check_out <= ? OR res.planned_check_in >= ?)
                  )
                """;

        List<BasicRoom> rooms = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(checkIn));
            ps.setDate(2, Date.valueOf(checkOut));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapRowToBasicRoom(rs));
                }
            }

            return rooms;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding available rooms by date: " + e.getMessage(), e);
        }
    }

    @Override
    public List<BasicRoom> findAvailableRoomsByCapacity(int capacity) {
        String sql = """
                SELECT r.*
                FROM room r
                JOIN room_type rt ON r.room_type_id = rt.id
                WHERE r.status = 'AVAILABLE'
                  AND rt.capacity >= ?
                """;

        List<BasicRoom> rooms = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, capacity);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapRowToBasicRoom(rs));
                }
            }

            return rooms;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding available rooms by capacity: " + e.getMessage(), e);
        }
    }

    @Override
    public List<BasicRoom> findAvailableRoomsByType(RoomType type) {
        String sql = "SELECT * FROM room WHERE room_type_id = ? AND status = 'AVAILABLE'";

        List<BasicRoom> rooms = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, type.getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapRowToBasicRoom(rs));
                }
            }

            return rooms;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding available rooms by type: " + e.getMessage(), e);
        }
    }

    @Override
    public List<BasicRoom> findAvailableRooms(LocalDate checkIn, LocalDate checkOut, int capacity, RoomType type) {
        String sql = """
                SELECT r.*
                FROM room r
                JOIN room_type rt ON r.room_type_id = rt.id
                WHERE r.status = 'AVAILABLE'
                  AND r.room_type_id = ?
                  AND rt.capacity >= ?
                  AND NOT EXISTS (
                      SELECT 1
                      FROM reservation res
                      WHERE res.room_id = r.id
                        AND NOT (res.planned_check_out <= ? OR res.planned_check_in >= ?)
                  )
                """;

        List<BasicRoom> rooms = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, type.getId());
            ps.setInt(2, capacity);
            ps.setDate(3, Date.valueOf(checkIn));
            ps.setDate(4, Date.valueOf(checkOut));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapRowToBasicRoom(rs));
                }
            }

            return rooms;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding available rooms with filters: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<BasicRoom> findRoomByNumber(String roomNumber) {
        String sql = "SELECT * FROM room WHERE room_number = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, roomNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToBasicRoom(rs));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding room by number: " + e.getMessage(), e);
        }
    }

    @Override
    public List<BasicRoom> findRoomsByStatus(RoomStatus status) {
        String sql = "SELECT * FROM room WHERE status = ?";

        List<BasicRoom> rooms = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, status.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapRowToBasicRoom(rs));
                }
            }

            return rooms;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding rooms by status: " + e.getMessage(), e);
        }
    }

    @Override
    public BigDecimal findDailyPriceByRoomId(Long roomId) {

        String sql = """
        SELECT rt.base_price
        FROM room r
        JOIN room_type rt ON r.room_type_id = rt.id
        WHERE r.id = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, roomId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("base_price");
            }

            throw new RuntimeException("Room not found");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateRoomStatus(Long roomId, RoomStatus status) {
        final String sql = """
        UPDATE room
        SET status = ?
        WHERE id = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setLong(2, roomId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Room status update failed", e);
        }
    }

    public Map<Long, Integer> findRoomIdNumberMap() {

        String sql = "SELECT id, room_number FROM room";

        Map<Long, Integer> map = new HashMap<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(
                        rs.getLong("id"),
                        rs.getInt("room_number")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return map;
    }

    @Override
    public Map<Long, String> findRoomIdTypeMap() {
        String sql = """
        SELECT r.id AS id,
               rt.name AS name
        FROM room r
        JOIN room_type rt ON r.room_type_id = rt.id
    """;

        Map<Long, String> map = new HashMap<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(
                        rs.getLong("id"),
                        rs.getString("name")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return map;
    }

}