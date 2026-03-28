package com.hotelreservationsystem.ui.controller;

import com.hotelreservationsystem.context.SessionContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CustomerDashboardController {

    Parent root;
    Scene scene;
    Stage stage;

    public void openProfile(ActionEvent event) throws IOException {

        root= FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/customer_profile.fxml"));
        scene=new Scene(root);
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Profile");
        stage.show();
    }

    @FXML
    void openRoomSearch(ActionEvent event) throws IOException {

        root= FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/room_search.fxml"));
        scene=new Scene(root);
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Room Search");
        stage.show();
    }

    public void openMyReservations(ActionEvent event) throws IOException {
        root= FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/my_reservations.fxml"));
        scene=new Scene(root);
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Room Search");
        stage.show();
    }
    @FXML
    void openPastStays(ActionEvent event) throws IOException {
        root= FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/past_stays.fxml"));
        scene=new Scene(root);
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Past Stays");
        stage.show();
    }

    public void logout(ActionEvent event) throws IOException {

        SessionContext.clear();
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        root=FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/login.fxml"));
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    public void closeForm(ActionEvent e) {
        System.exit(0);
    }
}
