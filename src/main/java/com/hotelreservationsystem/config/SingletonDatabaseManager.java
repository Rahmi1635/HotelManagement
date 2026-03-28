package com.hotelreservationsystem.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonDatabaseManager {

    private static SingletonDatabaseManager instance;
    private Connection connection;

    private SingletonDatabaseManager() {

        try {
            String url = "jdbc:mysql://localhost:3306/hotel_reservation_system";
            String user = "root";
            String pass = "12345";

            connection = DriverManager.getConnection(url, user, pass);
        }catch (SQLException e){
            throw new RuntimeException("Database connection error: " + e.getMessage(), e);
        }
    }

    public static synchronized SingletonDatabaseManager getInstance() {


        if (instance == null)
            instance = new SingletonDatabaseManager();
        return instance;
    }

    public Connection getConnection() {

        return connection;
    }
}
