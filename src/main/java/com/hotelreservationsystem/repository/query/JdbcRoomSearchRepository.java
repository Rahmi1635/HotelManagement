package com.hotelreservationsystem.repository.query;

import com.hotelreservationsystem.config.SingletonDatabaseManager;
import com.hotelreservationsystem.repository.jdbc.AvailableRoomDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcRoomSearchRepository {

    private final Connection connection;

    public JdbcRoomSearchRepository() {
        this.connection = SingletonDatabaseManager
                .getInstance()
                .getConnection();
    }

    public List<AvailableRoomDTO> findAvailableRooms(
            Long roomTypeId,
            LocalDate checkIn,
            LocalDate checkOut) {

        String sql = """
        
                SELECT r.id,
               r.room_number,
               rt.name       AS room_type,
               rt.base_price AS price,
               rt.capacity,
               rt.description
        FROM room r
        JOIN room_type rt ON r.room_type_id = rt.id
        WHERE r.status = 'available'
          AND rt.id = ?
          AND NOT EXISTS (
              SELECT 1
              FROM reservation res
              WHERE res.room_id = r.id
                AND res.status IN ('booked','checked_in')
                AND res.planned_check_in < ?
                AND res.planned_check_out > ?
          )
        """;

        List<AvailableRoomDTO> result = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, roomTypeId);
            ps.setTimestamp(2, Timestamp.valueOf(checkOut.atStartOfDay()));
            ps.setTimestamp(3, Timestamp.valueOf(checkIn.atStartOfDay()));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(new AvailableRoomDTO(
                        rs.getLong("id"),
                        rs.getString("room_number"),
                        rs.getString("room_type"),
                        rs.getDouble("price"),
                        rs.getInt("capacity"),
                        rs.getString("description")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Room search failed", e);
        }

        return result;
}

}
