package com.hotelreservationsystem.service.reservation;

import com.hotelreservationsystem.config.SingletonDatabaseManager;
import com.hotelreservationsystem.domain.reservation.Reservation;
import com.hotelreservationsystem.domain.reservation.ReservationStatus;
import com.hotelreservationsystem.domain.user.Customer;
import com.hotelreservationsystem.repository.jdbc.JdbcReservationRepository;
import com.hotelreservationsystem.repository.jdbc.JdbcRoomStatusRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationService {

    private final Connection connection =
            SingletonDatabaseManager.getInstance().getConnection();

    private final JdbcReservationRepository reservationRepository =
            new JdbcReservationRepository(connection);

    private final JdbcRoomStatusRepository roomStatusRepository =
            new JdbcRoomStatusRepository();

    /**
     * Customer için oda rezervasyonu oluşturur.
     * İş kuralları:
     * - Check-in < Check-out
     * - Check-in geçmişte olamaz
     * - Oda seçilen tarihlerde boş olmalı
     * - Toplam fiyat hesaplanır
     *
     * @param customer   Rezervasyon yapılacak müşteri
     * @param roomId     Rezervasyon yapılacak oda ID
     * @param checkIn    Giriş tarihi
     * @param checkOut   Çıkış tarihi
     * @param dailyPrice Günlük fiyat
     * @return Oluşturulan Reservation objesi
     */

    public Reservation reserveRoom(
            Customer customer,
            Long roomId,
            LocalDate checkIn,
            LocalDate checkOut,
            BigDecimal dailyPrice
    ) {
        // 1️⃣ Null kontrolleri
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Check-in and check-out dates are required");
        }

        if (customer == null) {
            throw new IllegalArgumentException("Valid customer with ID is required");
        }

        if (roomId == null) {
            throw new IllegalArgumentException("Room ID is required");
        }

        if (dailyPrice == null || dailyPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Daily price must be greater than zero");
        }

        // 2️⃣ Tarih validasyonu
        if (!checkIn.isBefore(checkOut)) {
            throw new IllegalArgumentException("Check-in date must be before check-out date");
        }

        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }

        // 3️⃣ Oda uygunluğu
        if (!roomStatusRepository.isRoomAvailable(roomId, checkIn, checkOut)) {
            throw new IllegalStateException("Selected room is not available in this period");
        }

        // 4️⃣ Fiyat hesaplama
        long days = ChronoUnit.DAYS.between(checkIn, checkOut);

        BigDecimal totalPrice = dailyPrice
                .multiply(BigDecimal.valueOf(days))
                .setScale(2, RoundingMode.HALF_UP);

        // 5️⃣ Reservation oluştur
        Reservation reservation = new Reservation(
                customer.getUser_id(),
                roomId,
                checkIn,
                checkOut,
                totalPrice,
                ReservationStatus.CONFIRMED
        );

        // 6️⃣ Transaction
        try {
            connection.setAutoCommit(false);

            reservationRepository.save(reservation);
            roomStatusRepository.updateRoomStatus(roomId, "RESERVED");

            connection.commit();

        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception ignored) {
            }
            throw new RuntimeException("Reservation transaction failed", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (Exception ignored) {
            }
        }

        return reservation;
    }
}
