package com.hotelreservationsystem.config;

public class AppConfig {
}
//AppConfig’in sağladıkları:
//
//Repository’leri oluşturur
//
//new JdbcCustomerRepository()
//
//new JdbcReservationRepository()
//
//Service katmanını oluşturur
//
//new CustomerService(customerRepository())
//
//new RoomService(roomRepository())
//
//Factory’i kullanarak uygun ReservationService’i üretir
//
//Customer login olursa → CustomerReservationService
//
//Staff login olursa → StaffReservationService
//
//Observer Subject nesnesini oluşturur
//
//Rezervasyon değişince ekranları güncellemek için
//
//UI Controller’lara bu nesneleri verir
//
//Bu sayede controller içinde new olmaz → temiz MVC