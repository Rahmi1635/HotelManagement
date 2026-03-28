package com.hotelreservationsystem.service.reservation;

import com.hotelreservationsystem.config.SingletonDatabaseManager;
import com.hotelreservationsystem.domain.reservation.Reservation;
import com.hotelreservationsystem.domain.reservation.ReservationStatus;
import com.hotelreservationsystem.domain.room.RoomStatus;
import com.hotelreservationsystem.repository.PersonRepository;
import com.hotelreservationsystem.repository.jdbc.JdbcPersonRepository;
import com.hotelreservationsystem.repository.jdbc.JdbcReservationRepository;
import com.hotelreservationsystem.repository.jdbc.JdbcRoomRepository;
import com.hotelreservationsystem.domain.reservation.dto.ReservationListItem;
import com.hotelreservationsystem.service.observer.ReservationEventType;
import com.hotelreservationsystem.service.observer.ReservationLogObserver;
import com.hotelreservationsystem.service.observer.ReservationSubject;
import com.hotelreservationsystem.service.reservation.strategy.ReservationFilterStrategy;

import java.time.LocalDate;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class StaffReservationService {


    private final Connection connection =
            SingletonDatabaseManager.getInstance().getConnection();

    private final ReservationSubject reservationSubject =
            new ReservationSubject();

    private final JdbcReservationRepository reservationRepository =
            new JdbcReservationRepository(connection);

    private final JdbcRoomRepository roomRepository =
            new JdbcRoomRepository();

    private final PersonRepository personRepository =
            new JdbcPersonRepository();


    public StaffReservationService()
    {
        reservationSubject.addObserver(new ReservationLogObserver());
    }

    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() ->
                        new RuntimeException("Reservation not found. ID=" + reservationId));
    }

    public void checkIn(Long reservationId) {

        try {
            connection.setAutoCommit(false);

            Reservation reservation = getReservationById(reservationId);

            if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
                throw new IllegalStateException(
                        "Check-In only allowed for CONFIRMED reservations");
            }

            boolean updated = reservationRepository.checkIn(reservationId);
            if (!updated) {
                throw new IllegalStateException("Check-In DB update failed");
            }

            roomRepository.updateRoomStatus(
                    reservation.getRoomId(),
                    RoomStatus.OCCUPIED
            );

            reservationSubject.notifyObservers(
                    ReservationEventType.CHECK_IN,
                    reservation
            );

            connection.commit();

        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception ignored) {}

            throw new RuntimeException("Check-In failed", e);

        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (Exception ignored) {}
        }


    }

    public void checkOut(Long reservationId) {

        try {
            connection.setAutoCommit(false);

            Reservation reservation = getReservationById(reservationId);

            if (reservation.getStatus() != ReservationStatus.CHECKED_IN) {
                throw new IllegalStateException(
                        "Check-Out only allowed for CHECKED_IN reservations");
            }

            boolean updated = reservationRepository.checkOut(reservationId);
            if (!updated) {
                throw new IllegalStateException("Check-Out DB update failed");
            }

            roomRepository.updateRoomStatus(
                    reservation.getRoomId(),
                    RoomStatus.AVAILABLE
            );

            reservationSubject.notifyObservers(
                    ReservationEventType.CHECK_OUT,
                    reservation
            );

            connection.commit();

        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception ignored) {}

            throw new RuntimeException("Check-Out failed", e);

        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (Exception ignored) {}
        }



    }

    public List<ReservationListItem> searchReservations(LocalDate from, LocalDate to, String customerName,
            String roomNumber, String roomType, ReservationStatus statusOrNull) {
        return reservationRepository.searchForStaff(
                from, to, customerName, roomNumber, roomType, statusOrNull
        );
    }

    public List<Reservation>getReservationByStrategy(ReservationFilterStrategy strategy){
        List<Reservation> allReservations = findAllReservations();
        return strategy.filter(allReservations);
    }

    public Map<Long, Integer> getRoomIdNumberMap() {
        return roomRepository.findRoomIdNumberMap();
    }

    public List<Reservation> findAllReservationsRaw() {
        return reservationRepository.findAll();
    }

    public Map<Long, String> getCustomerIdNameMap() {
        return personRepository.findCustomerIdNameMap();
    }

    public Map<Long, String> getRoomIdTypeMap() {
        return roomRepository.findRoomIdTypeMap();
    }





}
