package com.hotelreservationsystem.service.room;

import com.hotelreservationsystem.config.SingletonDatabaseManager;
import com.hotelreservationsystem.domain.reservation.Reservation;
import com.hotelreservationsystem.domain.reservation.ReservationStatus;
import com.hotelreservationsystem.repository.jdbc.AvailableRoomDTO;
import com.hotelreservationsystem.repository.jdbc.JdbcReservationRepository;
import com.hotelreservationsystem.repository.jdbc.JdbcRoomRepository;
import com.hotelreservationsystem.repository.query.JdbcRoomSearchRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class RoomService {

    private final Connection connection =
            SingletonDatabaseManager.getInstance().getConnection();

    private final JdbcReservationRepository reservationRepository =
            new JdbcReservationRepository(connection);

    private final JdbcRoomRepository roomRepository =
            new JdbcRoomRepository();

    private final JdbcRoomSearchRepository roomSearchRepository =
            new JdbcRoomSearchRepository();

    public List<AvailableRoomDTO> getAvailableRooms(
            Long roomTypeId,
            LocalDate checkIn,
            LocalDate checkOut
    ) {
        return search(roomTypeId, checkIn, checkOut);
    }



    // 🔹 ODA ARAMA
    public List<AvailableRoomDTO> search(
            Long roomTypeId,
            LocalDate checkIn,
            LocalDate checkOut
    ) {
        return roomSearchRepository
                .findAvailableRooms(roomTypeId, checkIn, checkOut);
    }

    // 🔹 REZERVASYON
    public void reserveRoom(
            Long customerId,
            Long roomId,
            LocalDate checkIn,
            LocalDate checkOut
    ) {

        try {
            connection.setAutoCommit(false);

            Reservation reservation = new Reservation(
                    customerId,
                    roomId,
                    checkIn,
                    checkOut,
                    null,
                    "Created from UI"
            );

            reservation.setStatus(ReservationStatus.CONFIRMED);

            reservationRepository.save(reservation);

            roomRepository.updateStatus(roomId, "RESERVED");

            connection.commit();

        } catch (Exception e) {
            try { connection.rollback(); } catch (SQLException ex) {}
            throw new RuntimeException("Reservation failed", e);
        }
    }

}
