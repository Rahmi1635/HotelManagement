package com.hotelreservationsystem.repository;

import com.hotelreservationsystem.domain.reservation.Reservation;
import com.hotelreservationsystem.domain.reservation.ReservationStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    // 1) Rezervasyon oluşturma / güncelleme (create + status değişimleri)
    Reservation save(Reservation reservation);

    // 2) Rezervasyon detay görüntüleme (müşteri veya personel)
    Optional<Reservation> findById(Long reservationId);

    // 3) Müşteri kendi rezervasyonlarını listeleyebilmeli (aktif + iptal dahil)
    List<Reservation> findAllByCustomerId(Long customerId);

    // 4) Müşteri rezervasyon detayını görürken yetki kontrolü için:
    Optional<Reservation> findByIdAndCustomerId(Long reservationId, Long customerId);

    // 5) Müşteri rezervasyon iptali (pratik: önce findByIdAndCustomerId, sonra status güncelle)
    // Repository seviyesinde direkt “cancel” metodu şart değil; ama gereksinime uygun ve net:
    boolean updateStatus(Long reservationId, ReservationStatus newStatus);

    // 6) Personel: tüm rezervasyonları listeleme
    List<Reservation> findAll();

    // 7) Personel: filtreleme (müşteri adı/oda no/oda tipi) -> DB’de reservation tablosunda ad/odaNo/odaType yok
    // Bu nedenle JOIN gerektiren arama metodu tanımlanır (implementasyonunda person + room join).
    List<Reservation> searchByCustomerNameOrRoomNoOrRoomType(
            String customerNameLike,
            String roomNumberLike,
            String roomTypeLike
    );

    // 8) Personel: check-in / check-out (status geçişlerini kaydetmek için save/updateStatus yeterli)
    // Yine de tek metotta yapmak istersen:
    boolean checkIn(Long reservationId);   // status=CHECKED_IN, actual_check_in=NOW
    boolean checkOut(Long reservationId);  // status=CHECKED_OUT, actual_check_out=NOW

    // 9) Uygun oda arama için kritik: tarih aralığında oda çakışması var mı?
    // Eğer bu liste boşsa oda müsait demektir.
    List<Reservation> findActiveReservationsOverlapping(Long roomId, LocalDate start, LocalDate end);

    // 10) Personel/müşteri: geçmiş konaklamalar (check-out yapılmış olanlar)
    List<Reservation> findPastStaysByCustomerId(Long customerId); // status=CHECKED_OUT
}
