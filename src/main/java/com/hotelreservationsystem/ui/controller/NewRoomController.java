package com.hotelreservationsystem.ui.controller;

import com.hotelreservationsystem.domain.room.RoomType;

import com.hotelreservationsystem.service.room.StaffRoomService;
import javafx.collections.FXCollections;
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

public class NewRoomController {

    @FXML private TextField txtRoomNumber;
    @FXML private ComboBox<RoomType> cmbRoomType;

    private final StaffRoomService staffRoomService = new StaffRoomService();

    @FXML
    public void initialize() {
        cmbRoomType.setItems(FXCollections.observableArrayList(staffRoomService.getRoomTypes()));
    }

    @FXML
    private void save(ActionEvent e) {
        try {
            String roomNo = txtRoomNumber.getText();
            RoomType type = cmbRoomType.getValue();

            staffRoomService.createRoom(roomNo, type);

            showInfo("Room created successfully.");
            clear(null);

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void clear(ActionEvent e) {
        txtRoomNumber.clear();
        cmbRoomType.getSelectionModel().clearSelection();
    }

    @FXML
    private void back(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(
                getClass().getResource("/com/hotelreservationsystem/staff_dashboard.fxml")
        ));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
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
