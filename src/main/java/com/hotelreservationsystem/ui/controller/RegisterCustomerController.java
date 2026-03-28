package com.hotelreservationsystem.ui.controller;

import com.hotelreservationsystem.domain.reservation.Reservation;
import com.hotelreservationsystem.domain.room.BasicRoom;
import com.hotelreservationsystem.domain.room.RoomType;
import com.hotelreservationsystem.domain.user.Customer;
import com.hotelreservationsystem.repository.jdbc.AvailableRoomDTO;
import com.hotelreservationsystem.service.customer.CustomerService;
import com.hotelreservationsystem.service.reservation.ReservationService;
import com.hotelreservationsystem.service.room.RoomService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class RegisterCustomerController {

    private final CustomerService customerService = new CustomerService();

    private final ReservationService reservationService = new ReservationService();

    private final RoomService roomService=new RoomService();

    Parent root;
    Scene scene;
    Stage stage;


    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField identityNumberField;

    @FXML
    private ComboBox<Customer> customerComboBox;

    @FXML
    private ComboBox<AvailableRoomDTO> roomNumberComboBox;

    @FXML
    private ComboBox<RoomType> roomTypeComboBox;

    @FXML
    private DatePicker checkInDatePicker;

    @FXML
    private DatePicker checkOutDatePicker;

    public void initialize() {
        customerComboBox.setItems(
                FXCollections.observableArrayList(customerService.getAllCustomers())
        );

        roomTypeComboBox.setItems(
                FXCollections.observableArrayList(RoomType.values())
        );

        roomTypeComboBox.setOnAction(e -> loadAvailableRooms());
        checkInDatePicker.setOnAction(e -> loadAvailableRooms());
        checkOutDatePicker.setOnAction(e -> loadAvailableRooms());
    }

    private void loadAvailableRooms() {
        if (roomTypeComboBox.getValue() == null ||
                checkInDatePicker.getValue() == null ||
                checkOutDatePicker.getValue() == null) {
            return;
        }

        roomNumberComboBox.setItems(
                FXCollections.observableArrayList(
                        roomService.search(
                                roomTypeComboBox.getValue().getId(),
                                checkInDatePicker.getValue(),
                                checkOutDatePicker.getValue()
                        )
                )
        );
    }

    private void refreshCustomers() {
        customerComboBox.setItems(
                FXCollections.observableArrayList(customerService.getAllCustomers())
        );
    }



    public void handleCreateReservation()
    {
        try {
            Customer selectedCustomer = customerComboBox.getValue();
            AvailableRoomDTO selectedRoom = roomNumberComboBox.getValue();
            LocalDate checkIn = checkInDatePicker.getValue();
            LocalDate checkOut = checkOutDatePicker.getValue();

            if (selectedCustomer == null || selectedRoom == null) {
                throw new IllegalArgumentException("Customer and room must be selected");
            }

            Reservation reservation = reservationService.reserveRoom(
                    selectedCustomer,
                    selectedRoom.getRoomId(),
                    checkIn,
                    checkOut,
                    BigDecimal.valueOf(selectedRoom.getPrice())
            );

            showInfo(
                    "Reservation Created",
                    "Room " + selectedRoom.getRoomNumber() + " successfully reserved"
            );

            clearReservationForm();

        } catch (Exception e) {
            showError("Reservation Error", e.getMessage());
        }
    }

    public void handleCreateCustomer()
    {

        if (emailField.getText() == null || emailField.getText().isBlank()) {
            showError("Validation Error", "Email is required");
            return;
        }

        try {
            customerService.createCustomerByPersonnel(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    identityNumberField.getText()
            );

            showInfo("Success", "Customer created successfully");
            clearCustomerForm();
            refreshCustomers();

        } catch (Exception e) {
            showError("Error", e.getMessage());
        }
    }

    private void clearReservationForm() {
        customerComboBox.setValue(null);
        roomNumberComboBox.setValue(null);
        roomTypeComboBox.setValue(null);
        checkInDatePicker.setValue(null);
        checkOutDatePicker.setValue(null);
    }


    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearCustomerForm() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
        identityNumberField.clear();
    }

    @FXML
    void closeForm(ActionEvent event) throws IOException {
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        root= FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/staff_dashboard.fxml"));
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



}
