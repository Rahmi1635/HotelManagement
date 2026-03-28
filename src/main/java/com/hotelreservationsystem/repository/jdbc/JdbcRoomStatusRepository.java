package com.hotelreservationsystem.repository.jdbc;

import com.hotelreservationsystem.config.SingletonDatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class JdbcRoomStatusRepository {

    private final Connection connection =
            SingletonDatabaseManager.getInstance().getConnection();

    public void updateRoomStatus(Long roomId, String status) {

        final String sql = """
            UPDATE room
            SET status = ?
            WHERE id = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, roomId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Room status update failed", e);
        }
    }

    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {

        final String sql = """
        SELECT COUNT(*)
        FROM reservation
        WHERE room_id = ?
          AND status != 'CANCELLED'
          AND (
                ? < planned_check_out
            AND ? > planned_check_in
          )
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, roomId);
            ps.setDate(2, java.sql.Date.valueOf(checkIn));
            ps.setDate(3, java.sql.Date.valueOf(checkOut));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // 0 ise oda boş
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Room availability check failed", e);
        }

        return false;
    }

}
