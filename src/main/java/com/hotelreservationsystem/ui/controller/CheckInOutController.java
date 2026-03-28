package com.hotelreservationsystem.ui.controller;

import com.hotelreservationsystem.domain.reservation.Reservation;
import com.hotelreservationsystem.domain.reservation.ReservationStatus;
import com.hotelreservationsystem.service.reservation.StaffReservationService;
import com.hotelreservationsystem.service.reservation.StaffReservationService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CheckInOutController {

    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cmbStatus;

    @FXML private TableView<Reservation> tblReservations;
    @FXML private TableColumn<Reservation, String> colId;
    @FXML private TableColumn<Reservation, String> colCustomerId;
    @FXML private TableColumn<Reservation, String> colRoomId;
    @FXML private TableColumn<Reservation, String> colPlannedIn;
    @FXML private TableColumn<Reservation, String> colPlannedOut;
    @FXML private TableColumn<Reservation, String> colStatus;

    @FXML private TextField txtSelectedId;
    @FXML private TextField txtSelectedStatus;

    @FXML private Button btnCheckIn;
    @FXML private Button btnCheckOut;

    private final StaffReservationService staffReservationService = new StaffReservationService();

    private final ObservableList<Reservation> master = FXCollections.observableArrayList();
    private FilteredList<Reservation> filtered;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));
        colCustomerId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getCustomerId())));
        colRoomId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getRoomId())));
        colPlannedIn.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getPlannedCheckIn())));
        colPlannedOut.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getPlannedCheckOut())));
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus().name()));

        cmbStatus.setItems(FXCollections.observableArrayList(
                "ALL",
                ReservationStatus.PENDING.name(),
                ReservationStatus.CONFIRMED.name(),
                ReservationStatus.CHECKED_IN.name(),
                ReservationStatus.CHECKED_OUT.name(),
                ReservationStatus.CANCELLED.name()
        ));
        cmbStatus.getSelectionModel().select("ALL");

        // ✅ ÖNCE filtered oluştur
        filtered = new FilteredList<>(master, r -> true);
        tblReservations.setItems(filtered);

        // ✅ Sonra veri yükle (artık applyFilter güvenli)
        loadData();

        tblReservations.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> onSelect(n));
        txtSearch.textProperty().addListener((o, a, b) -> applyFilter());
        cmbStatus.valueProperty().addListener((o, a, b) -> applyFilter());

        clearSelection();
    }

    private void loadData() {
        master.setAll(staffReservationService.findAllReservations());
        applyFilter();
    }

    private void applyFilter() {
        // ekstra güvenlik (istersen kalsın)
        if (filtered == null) return;

        String q = txtSearch.getText() == null ? "" : txtSearch.getText().trim();
        String status = cmbStatus.getValue() == null ? "ALL" : cmbStatus.getValue();

        filtered.setPredicate(r -> {
            boolean statusOk = status.equals("ALL") || r.getStatus().name().equals(status);
            if (!statusOk) return false;

            if (q.isEmpty()) return true;

            return String.valueOf(r.getId()).contains(q)
                    || String.valueOf(r.getCustomerId()).contains(q)
                    || String.valueOf(r.getRoomId()).contains(q);
        });
    }

    private void onSelect(Reservation r) {
        if (r == null) {
            clearSelection();
            return;
        }

        txtSelectedId.setText(String.valueOf(r.getId()));
        txtSelectedStatus.setText(r.getStatus().name());

        btnCheckIn.setDisable(r.getStatus() != ReservationStatus.CONFIRMED);
        btnCheckOut.setDisable(r.getStatus() != ReservationStatus.CHECKED_IN);
    }

    private void clearSelection() {
        txtSelectedId.clear();
        txtSelectedStatus.clear();
        btnCheckIn.setDisable(true);
        btnCheckOut.setDisable(true);
    }

    @FXML
    private void refresh(ActionEvent e) {
        loadData();
        tblReservations.getSelectionModel().clearSelection();
        clearSelection();
    }

    @FXML
    private void search(ActionEvent e) {
        applyFilter();
    }

    @FXML
    private void checkIn(ActionEvent e) {
        Reservation r = tblReservations.getSelectionModel().getSelectedItem();
        if (r == null) return;

        try {
            staffReservationService.checkIn(r.getId());
            showInfo("Check-In successful");
            loadData();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void checkOut(ActionEvent e) {
        Reservation r = tblReservations.getSelectionModel().getSelectedItem();
        if (r == null) return;

        try {
            staffReservationService.checkOut(r.getId());
            showInfo("Check-Out successful");
            loadData();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void backToDashboard(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(
                getClass().getResource("/com/hotelreservationsystem/staff_dashboard.fxml")
        ));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void closeForm(ActionEvent e) {
        ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
