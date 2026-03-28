package com.hotelreservationsystem.repository.jdbc;

import com.hotelreservationsystem.config.SingletonDatabaseManager;
import com.hotelreservationsystem.domain.reservation.Reservation;
import com.hotelreservationsystem.domain.reservation.ReservationStatus;
import com.hotelreservationsystem.repository.ReservationRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class JdbcReservationRepository implements ReservationRepository {

    private final Connection connection;

    public JdbcReservationRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            return insert(reservation);
        } else {
            return update(reservation);
        }
    }

    private Reservation insert(Reservation r) {
        final String sql = """
            INSERT INTO reservation
              (customer_id, room_id, status, planned_check_in, planned_check_out,
               actual_check_in, actual_check_out, notes, total_price)
            VALUES
              (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, r.getCustomerId());
            ps.setLong(2, r.getRoomId());
            ps.setString(3, r.getStatus().name());
            ps.setDate(4, Date.valueOf(r.getPlannedCheckIn()));
            ps.setDate(5, Date.valueOf(r.getPlannedCheckOut()));

            if (r.getActualCheckIn() != null) ps.setTimestamp(6, Timestamp.valueOf(r.getActualCheckIn()));
            else ps.setNull(6, Types.TIMESTAMP);

            if (r.getActualCheckOut() != null) ps.setTimestamp(7, Timestamp.valueOf(r.getActualCheckOut()));
            else ps.setNull(7, Types.TIMESTAMP);

            ps.setString(8, r.getNotes());

            if (r.getTotalPrice() != null) ps.setBigDecimal(9, r.getTotalPrice());
            else ps.setNull(9, Types.DECIMAL);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    r.setId(keys.getLong(1));
                }
            }
            return r;

        } catch (SQLException e) {
            throw new RuntimeException("Reservation insert failed.", e);
        }
    }

    private Reservation update(Reservation r) {
        final String sql = """
            UPDATE reservation SET
              customer_id = ?,
              room_id = ?,
              status = ?,
              planned_check_in = ?,
              planned_check_out = ?,
              actual_check_in = ?,
              actual_check_out = ?,
              notes = ?,
              total_price = ?
            WHERE id = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, r.getCustomerId());
            ps.setLong(2, r.getRoomId());
            ps.setString(3, r.getStatus().name());
            ps.setDate(4, Date.valueOf(r.getPlannedCheckIn()));
            ps.setDate(5, Date.valueOf(r.getPlannedCheckOut()));

            if (r.getActualCheckIn() != null) ps.setTimestamp(6, Timestamp.valueOf(r.getActualCheckIn()));
            else ps.setNull(6, Types.TIMESTAMP);

            if (r.getActualCheckOut() != null) ps.setTimestamp(7, Timestamp.valueOf(r.getActualCheckOut()));
            else ps.setNull(7, Types.TIMESTAMP);

            ps.setString(8, r.getNotes());

            if (r.getTotalPrice() != null) ps.setBigDecimal(9, r.getTotalPrice());
            else ps.setNull(9, Types.DECIMAL);

            ps.setLong(10, r.getId());

            ps.executeUpdate();
            return r;

        } catch (SQLException e) {
            throw new RuntimeException("Reservation update failed.", e);
        }
    }

    @Override
    public Optional<Reservation> findById(Long reservationId) {
        final String sql = "SELECT * FROM reservation WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, reservationId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapReservation(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Reservation findById failed.", e);
        }
    }

    @Override
    public List<Reservation> findAllByCustomerId(Long customerId) {
        final String sql = "SELECT * FROM reservation WHERE customer_id = ? ORDER BY planned_check_in DESC";

        List<Reservation> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapReservation(rs));
                }
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Reservation findAllByCustomerId failed.", e);
        }
    }

    @Override
    public Optional<Reservation> findByIdAndCustomerId(Long reservationId, Long customerId) {
        final String sql = "SELECT * FROM reservation WHERE id = ? AND customer_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, reservationId);
            ps.setLong(2, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapReservation(rs));
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Reservation findByIdAndCustomerId failed.", e);
        }
    }

    @Override
    public boolean updateStatus(Long reservationId, ReservationStatus newStatus) {
        final String sql = "UPDATE reservation SET status = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newStatus.name());
            ps.setLong(2, reservationId);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Reservation updateStatus failed.", e);
        }
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = "SELECT * FROM reservation ORDER BY planned_check_in DESC";

        List<Reservation> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapReservation(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Reservation findAll failed.", e);
        }
    }

    @Override
    public List<Reservation> searchByCustomerNameOrRoomNoOrRoomType(
            String customerNameLike,
            String roomNumberLike,
            String roomTypeLike
    ) {
        final String sql = """
            SELECT r.*
            FROM reservation r
            JOIN person p ON p.id = r.customer_id
            JOIN room ro ON ro.id = r.room_id
            JOIN room_type rt ON rt.id = ro.room_type_id
            WHERE
              ( ? IS NULL OR LOWER(CONCAT(p.name, ' ', p.surname)) LIKE LOWER(?) )
              OR
              ( ? IS NULL OR CAST(ro.room_number AS CHAR) LIKE ? )
              OR
              ( ? IS NULL OR LOWER(rt.name) LIKE LOWER(?) )
            ORDER BY r.planned_check_in DESC
            """;

        String nameParam = normalizeLike(customerNameLike);
        String roomNoParam = normalizeLike(roomNumberLike);
        String roomTypeParam = normalizeLike(roomTypeLike);

        List<Reservation> list = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nameParam == null ? null : customerNameLike);
            ps.setString(2, nameParam);

            ps.setString(3, roomNoParam == null ? null : roomNumberLike);
            ps.setString(4, roomNoParam);

            ps.setString(5, roomTypeParam == null ? null : roomTypeLike);
            ps.setString(6, roomTypeParam);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapReservation(rs));
                }
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Reservation searchByCustomerNameOrRoomNoOrRoomType failed.", e);
        }
    }

    @Override
    public boolean checkIn(Long reservationId) {
        final String sql = """
            UPDATE reservation
            SET status = ?, actual_check_in = CURRENT_TIMESTAMP
            WHERE id = ? AND status = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ReservationStatus.CHECKED_IN.name());
            ps.setLong(2, reservationId);
            ps.setString(3, ReservationStatus.CONFIRMED.name());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Reservation checkIn failed.", e);
        }
    }

    @Override
    public boolean checkOut(Long reservationId) {
        final String sql = """
            UPDATE reservation
            SET status = ?, actual_check_out = CURRENT_TIMESTAMP
            WHERE id = ? AND status = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ReservationStatus.CHECKED_OUT.name());
            ps.setLong(2, reservationId);
            ps.setString(3, ReservationStatus.CHECKED_IN.name());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Reservation checkOut failed.", e);
        }
    }

    @Override
    public List<Reservation> findActiveReservationsOverlapping(Long roomId, LocalDate start, LocalDate end) {
        final String sql = """
            SELECT *
            FROM reservation
            WHERE room_id = ?
              AND status IN (?, ?, ?)
              AND planned_check_in < ?
              AND planned_check_out > ?
            ORDER BY planned_check_in ASC
            """;

        List<Reservation> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, roomId);
            ps.setString(2, ReservationStatus.PENDING.name());
            ps.setString(3, ReservationStatus.CONFIRMED.name());
            ps.setString(4, ReservationStatus.CHECKED_IN.name());
            ps.setDate(5, Date.valueOf(end));
            ps.setDate(6, Date.valueOf(start));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapReservation(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Reservation findActiveReservationsOverlapping failed.", e);
        }
    }

    @Override
    public List<Reservation> findPastStaysByCustomerId(Long customerId) {
        final String sql = """
            SELECT *
            FROM reservation
            WHERE customer_id = ?
              AND status = ?
            ORDER BY planned_check_in DESC
            """;

        List<Reservation> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            ps.setString(2, ReservationStatus.CHECKED_OUT.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapReservation(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Reservation findPastStaysByCustomerId failed.", e);
        }
    }

    public List<com.hotelreservationsystem.domain.reservation.dto.ReservationListItem> searchForStaff(
            java.time.LocalDate from,
            java.time.LocalDate to,
            String customerNameLike,
            String roomNumberLike,
            String roomTypeLike,
            com.hotelreservationsystem.domain.reservation.ReservationStatus statusOrNull
    ) {
        final String sql = """
                SELECT
                        r.id AS reservation_id,
                        CONCAT(p.first_name, ' ', p.last_name) AS customer_full_name,
                        CAST(ro.room_number AS CHAR) AS room_number,
                        rt.name AS room_type,
                        r.planned_check_in,
                        r.planned_check_out,
                        r.status,
                        r.total_price
                    FROM reservation r
                    JOIN person p ON p.id = r.customer_id
                    JOIN room ro ON ro.id = r.room_id
                    JOIN room_type rt ON rt.id = ro.room_type_id
                    WHERE
                        ( ? IS NULL OR LOWER(CONCAT(p.first_name, ' ', p.last_name)) LIKE LOWER(?) )
                      AND ( ? IS NULL OR CAST(ro.room_number AS CHAR) LIKE ? )
                      AND ( ? IS NULL OR LOWER(rt.name) LIKE LOWER(?) )
                      AND ( ? IS NULL OR r.status = ? )
                      AND ( ? IS NULL OR r.planned_check_in >= ? )
                      AND ( ? IS NULL OR r.planned_check_out <= ? )
                    ORDER BY r.planned_check_in DESC
                """;

        String nameParam = normalizeLike(customerNameLike);
        String roomNoParam = normalizeLike(roomNumberLike);
        String roomTypeParam = normalizeLike(roomTypeLike);

        List<com.hotelreservationsystem.domain.reservation.dto.ReservationListItem> list = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nameParam == null ? null : customerNameLike);
            ps.setString(2, nameParam);

            ps.setString(3, roomNoParam == null ? null : roomNumberLike);
            ps.setString(4, roomNoParam);

            ps.setString(5, roomTypeParam == null ? null : roomTypeLike);
            ps.setString(6, roomTypeParam);

            String st = (statusOrNull == null) ? null : statusOrNull.name();
            ps.setString(7, st);
            ps.setString(8, st);

            java.sql.Date dFrom = (from == null) ? null : java.sql.Date.valueOf(from);
            java.sql.Date dTo   = (to == null) ? null : java.sql.Date.valueOf(to);

            ps.setDate(9, dFrom);
            ps.setDate(10, dFrom);
            ps.setDate(11, dTo);
            ps.setDate(12, dTo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Long id = rs.getLong("reservation_id");
                    String fullName = rs.getString("customer_full_name");
                    String roomNo = rs.getString("room_number");
                    String roomType = rs.getString("room_type");

                    java.time.LocalDate plannedIn = rs.getDate("planned_check_in").toLocalDate();
                    java.time.LocalDate plannedOut = rs.getDate("planned_check_out").toLocalDate();

                    String status = rs.getString("status");
                    java.math.BigDecimal totalPrice = rs.getBigDecimal("total_price");

                    list.add(new com.hotelreservationsystem.domain.reservation.dto.ReservationListItem(
                            id, fullName, roomNo, roomType, plannedIn, plannedOut, status, totalPrice
                    ));
                }
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Reservation searchForStaff failed.", e);
        }
    }


    private Reservation mapReservation(ResultSet rs) throws SQLException {

        Long id = rs.getLong("id");
        Long customerId = rs.getLong("customer_id");
        Long roomId = rs.getLong("room_id");

        ReservationStatus status = ReservationStatus.valueOf(rs.getString("status"));

        LocalDate plannedIn = rs.getDate("planned_check_in").toLocalDate();
        LocalDate plannedOut = rs.getDate("planned_check_out").toLocalDate();

        Timestamp aci = rs.getTimestamp("actual_check_in");
        Timestamp aco = rs.getTimestamp("actual_check_out");

        LocalDateTime actualIn = (aci != null) ? aci.toLocalDateTime() : null;
        LocalDateTime actualOut = (aco != null) ? aco.toLocalDateTime() : null;

        String notes = rs.getString("notes");
        BigDecimal totalPrice = rs.getBigDecimal("total_price");

        Reservation r = new Reservation(customerId, roomId, plannedIn, plannedOut, totalPrice, notes);
        r.setId(id);
        r.setStatus(status);
        r.setActualCheckIn(actualIn);
        r.setActualCheckOut(actualOut);

        return r;
    }

    private String normalizeLike(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty()) return null;
        return "%" + t + "%";
    }
}