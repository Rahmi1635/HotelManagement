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

public class StaffDashboardController {

    Parent root;
    Scene scene;
    Stage stage;

    public void openNewCustomer(ActionEvent e) throws IOException {
        root= FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/register_customer.fxml"));
        scene=new Scene(root);
        stage=(Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Add Customer");
        stage.show();
    }
    public void openCustomerList(ActionEvent e) throws IOException{

        root= FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/customer_list.fxml"));
        scene=new Scene(root);
        stage=(Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Customer List");
        stage.show();
    }
    public void openNewRoom(ActionEvent e) throws IOException{

        root= FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/new_room.fxml"));
        scene=new Scene(root);
        stage=(Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("New Room");
        stage.show();
    }
    public void openReservationList(ActionEvent e) throws IOException{

        root= FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/reservation_list.fxml"));
        scene=new Scene(root);
        stage=(Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Reservations");
        stage.show();
    }
    public void openCheckInOut(ActionEvent e) throws IOException{

        root= FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/check_in_out.fxml"));
        scene=new Scene(root);
        stage=(Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Check In/Out");
        stage.show();
    }

    public void logout(ActionEvent event) throws IOException {

        SessionContext.clear();
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        root= FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/login.fxml"));
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void closeForm(ActionEvent e) {System.exit(0);}
}
