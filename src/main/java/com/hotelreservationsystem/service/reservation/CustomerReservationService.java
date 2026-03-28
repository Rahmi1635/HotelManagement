package com.hotelreservationsystem.service.reservation;

import com.hotelreservationsystem.config.SingletonDatabaseManager;
import com.hotelreservationsystem.domain.reservation.Reservation;
import com.hotelreservationsystem.domain.reservation.ReservationStatus;
import com.hotelreservationsystem.domain.room.RoomStatus;
import com.hotelreservationsystem.repository.jdbc.JdbcReservationRepository;
import com.hotelreservationsystem.repository.jdbc.JdbcRoomRepository;
import com.hotelreservationsystem.repository.jdbc.JdbcRoomStatusRepository;
import com.hotelreservationsystem.service.pricing.PricingService;

import java.math.BigDecimal;
import java.util.List;
import java.sql.Connection;

public class CustomerReservationService {

    private final Connection connection =
            SingletonDatabaseManager.getInstance().getConnection();

    private final JdbcReservationRepository reservationRepository =
            new JdbcReservationRepository(connection);

    private final JdbcRoomRepository roomRepository=new JdbcRoomRepository();

    private final PricingService pricingService = new PricingService();

    private final JdbcRoomStatusRepository roomStatusRepository=new JdbcRoomStatusRepository();


    public List<Reservation> getMyActiveReservations(Long customerId) {


        List<Reservation> reservations =
                reservationRepository.findAllByCustomerId(customerId)
                        .stream()
                        .filter(r ->
                                r.getStatus() == ReservationStatus.PENDING ||
                                        r.getStatus() == ReservationStatus.CONFIRMED ||
                                        r.getStatus() == ReservationStatus.CHECKED_IN
                        )
                        .toList();

        for (Reservation r : reservations) {
            BigDecimal dailyPrice =
                    roomRepository.findDailyPriceByRoomId(r.getRoomId());

            BigDecimal totalPrice =
                    pricingService.calculateTotalPrice(
                            r.getPlannedCheckIn(),
                            r.getPlannedCheckOut(),
                            dailyPrice
                    );

            r.setTotalPrice(totalPrice);
        }

        return reservations;
    }



    public void cancelMyReservation(Long reservationId, Long customerId) {

        try {
            connection.setAutoCommit(false);

            Reservation reservation =
                    reservationRepository
                            .findByIdAndCustomerId(reservationId, customerId)
                            .orElseThrow(() -> new RuntimeException("Reservation not found"));

            if (reservation.getStatus() != ReservationStatus.PENDING &&
                    reservation.getStatus() != ReservationStatus.CONFIRMED) {
                throw new IllegalStateException("Reservation cannot be cancelled");
            }

            reservation.changeStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(reservation);

            roomRepository.updateRoomStatus(
                    reservation.getRoomId(),
                    RoomStatus.AVAILABLE
            );

            connection.commit();

        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception ignored) {
            }
            throw new RuntimeException("Cancel reservation failed", e);

        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (Exception ignored) {
            }
        }
    }

}
