-- CREATE DATABASE hotel_reservation_system
-- use hotel_reservation_system 

-- PERSON
CREATE TABLE Person (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    username VARCHAR(80) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(30),
    tcno VARCHAR(20),
    hashed_password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'customer',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;


-- ROOM TYPE
CREATE TABLE Room_Type (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    base_price DECIMAL(10,2) NOT NULL,
    capacity INT UNSIGNED NOT NULL,
    description TEXT
) ENGINE=InnoDB;


-- ROOM
CREATE TABLE Room (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(50) NOT NULL UNIQUE,
    room_type_id BIGINT UNSIGNED NOT NULL,
    status ENUM('available','occupied','maintenance','reserved') 
        NOT NULL DEFAULT 'available',

    CONSTRAINT fk_room_roomtype
        FOREIGN KEY (room_type_id)
        REFERENCES RoomType(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;


-- RESERVATION
CREATE TABLE Reservation (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,

    customer_id BIGINT UNSIGNED NOT NULL,
    room_id BIGINT UNSIGNED NOT NULL,

    status ENUM('booked','checked_in','checked_out','cancelled') 
        NOT NULL DEFAULT 'booked',

    planned_check_in DATETIME NOT NULL,
    planned_check_out DATETIME NOT NULL,
    actual_check_in DATETIME,
    actual_check_out DATETIME,
    notes TEXT,
    total_price DECIMAL(10,2),

    CONSTRAINT fk_reservation_customer
        FOREIGN KEY (customer_id)
        REFERENCES Person(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_reservation_room
        FOREIGN KEY (room_id)
        REFERENCES Room(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;


-- PAYMENT  (1-1 relationship with Reservation)
CREATE TABLE Payment (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,

    reservation_id BIGINT UNSIGNED NOT NULL UNIQUE,   -- 1–1 ilişki burada

    amount DECIMAL(10,2) NOT NULL,
    method VARCHAR(50) NOT NULL,
    paid_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_reservation
        FOREIGN KEY (reservation_id)
        REFERENCES Reservation(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;
