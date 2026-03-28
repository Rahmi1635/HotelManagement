package com.hotelreservationsystem.ui.controller;

import com.hotelreservationsystem.context.SessionContext;
import com.hotelreservationsystem.domain.reservation.Reservation;
import com.hotelreservationsystem.service.reservation.CustomerReservationService;
import com.hotelreservationsystem.service.reservation.ReservationService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PastStaysController {

    @FXML
    private TableView<Reservation> pastStaysTable;
    @FXML
    private TableColumn<Reservation,LocalDate> checkInColumn;

    @FXML
    private TableColumn<Reservation, LocalDate> checkOutColumn;


    @FXML
    private TableColumn<Reservation, BigDecimal> priceColumn;

    @FXML
    private TableColumn<Reservation, Long> roomColumn;

    private final CustomerReservationService customerReservationService =
            new CustomerReservationService();

    Parent root;
    Scene scene;
    Stage stage;

    @FXML
    void handleBack(ActionEvent event) throws IOException {
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        root= FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/customer_dashboard.fxml"));
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() {
        // 1️⃣ Column → field mapping
        roomColumn.setCellValueFactory(
                new PropertyValueFactory<>("roomId")
        );

        checkInColumn.setCellValueFactory(
                new PropertyValueFactory<>("plannedCheckIn")
        );

        checkOutColumn.setCellValueFactory(
                new PropertyValueFactory<>("plannedCheckOut")
        );

        priceColumn.setCellValueFactory(
                new PropertyValueFactory<>("totalPrice")
        );

        // 2️⃣ Data load
        Long customerId = SessionContext.getCurrentUser().getUser_id();

        pastStaysTable.setItems(
                FXCollections.observableArrayList(
                        customerReservationService.getMyActiveReservations(customerId)
                )
        );
    }

}
