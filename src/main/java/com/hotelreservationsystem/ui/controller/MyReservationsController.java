package com.hotelreservationsystem.ui.controller;

import com.hotelreservationsystem.context.SessionContext;
import com.hotelreservationsystem.domain.reservation.Reservation;
import com.hotelreservationsystem.domain.reservation.ReservationStatus;
import com.hotelreservationsystem.repository.jdbc.JdbcReservationRepository;
import com.hotelreservationsystem.repository.jdbc.JdbcRoomRepository;
import com.hotelreservationsystem.service.pricing.PricingService;
import com.hotelreservationsystem.service.reservation.CustomerReservationService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class MyReservationsController  {

    Parent root;
    Scene scene;
    Stage stage;

    public void handleBack(ActionEvent event) throws IOException {
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        root= FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/customer_dashboard.fxml"));
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private TableView<Reservation> reservationTable;

    @FXML private TableColumn<Reservation, Long> roomColumn;
    @FXML private TableColumn<Reservation, LocalDate> checkInColumn;
    @FXML private TableColumn<Reservation, LocalDate> checkOutColumn;
    @FXML private TableColumn<Reservation, BigDecimal> priceColumn;
    @FXML private TableColumn<Reservation, String> statusColumn;
    @FXML private TableColumn<Reservation, Void> actionColumn;

    private final CustomerReservationService customerReservationService =
            new CustomerReservationService();

    @FXML
    public void initialize() {
        setupColumns();
        setupActionColumn();
        loadMyReservations();

        reservationTable.setRowFactory(tv -> {
            TableRow<Reservation> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showReservationDetails(row.getItem());
                }
            });
            return row;
        });
    }

    private void setupColumns() {
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        checkInColumn.setCellValueFactory(new PropertyValueFactory<>("plannedCheckIn"));
        checkOutColumn.setCellValueFactory(new PropertyValueFactory<>("plannedCheckOut"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("calculatedTotalPrice"));

        statusColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getStatus().name()
                )
        );
    }

    private void loadMyReservations() {
        Long customerId = SessionContext.getCurrentUser().getUser_id();

        reservationTable.setItems(
                FXCollections.observableArrayList(
                        customerReservationService.getMyActiveReservations(customerId)
                )
        );
    }

    private void setupActionColumn() {
        actionColumn.setCellFactory(col -> new TableCell<>() {

            private final Button cancelBtn = new Button("Cancel");

            {
                cancelBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                cancelBtn.setOnAction(e -> {
                    Reservation reservation =
                            getTableView().getItems().get(getIndex());
                    handleCancelReservation(reservation);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    Reservation r = getTableView().getItems().get(getIndex());

                    boolean cancellable =
                            r.getStatus().name().equals("PENDING") ||
                                    r.getStatus().name().equals("CONFIRMED");

                    cancelBtn.setDisable(!cancellable);
                    setGraphic(cancelBtn);
                }
            }
        });
    }

    private void handleCancelReservation(Reservation reservation) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cancel Reservation");
        confirm.setHeaderText("Are you sure?");
        confirm.setContentText("This reservation will be cancelled.");

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                Long customerId = SessionContext.getCurrentUser().getUser_id();

                customerReservationService.cancelMyReservation(
                        reservation.getId(),
                        customerId
                );

                loadMyReservations();
            }
        });
    }

    private void showReservationDetails(Reservation r) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Reservation Details");
        info.setHeaderText("Reservation #" + r.getId());
        info.setContentText(
                "Room: " + r.getRoomId() + "\n" +
                        "Check-in: " + r.getPlannedCheckIn() + "\n" +
                        "Check-out: " + r.getPlannedCheckOut() + "\n" +
                        "Status: " + r.getStatus() + "\n" +
                        "Total Price: " + r.getTotalPrice()
        );
        info.showAndWait();
    }





}
