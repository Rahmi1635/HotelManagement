package com.hotelreservationsystem.ui.controller;

import com.hotelreservationsystem.domain.room.RoomType;
import com.hotelreservationsystem.repository.jdbc.AvailableRoomDTO;
import com.hotelreservationsystem.service.room.RoomService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class RoomSearchController implements Initializable {


    @FXML
    private TableView<AvailableRoomDTO> roomTableView;

    @FXML
    private TableColumn<AvailableRoomDTO, String> roomTypeColumn;

    @FXML
    private TableColumn<AvailableRoomDTO, Double> priceColumn;

    @FXML
    private TableColumn<AvailableRoomDTO, String> availabilityColumn;

    @FXML
    private TableColumn<AvailableRoomDTO, Void> actionColumn;

    @FXML
    private DatePicker checkInDatePicker;

    @FXML
    private DatePicker checkOutDatePicker;

    @FXML
    private ComboBox<RoomType> roomTypeComboBox;

    private final RoomService roomService = new RoomService();

    //  KRİTİK: UI listesi artık elde tutuluyor
    private ObservableList<AvailableRoomDTO> availableRooms;

    Parent root;
    Scene scene;
    Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initRoomTypeComboBox();

        roomTypeColumn.setCellValueFactory(
                new PropertyValueFactory<>("roomType")
        );

        priceColumn.setCellValueFactory(
                new PropertyValueFactory<>("price")
        );

        availabilityColumn.setCellValueFactory(
                cellData -> new ReadOnlyStringWrapper("Available")
        );

        actionColumn.setCellFactory(col -> new TableCell<>() {

            private final Button reserveBtn = new Button("Reserve");

            {
                reserveBtn.setOnAction(e -> {
                    AvailableRoomDTO room =
                            getTableView().getItems().get(getIndex());
                    createReservation(room);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : reserveBtn);
            }
        });
    }

    private void initRoomTypeComboBox() {
        roomTypeComboBox.getItems().addAll(RoomType.values());

        roomTypeComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(RoomType roomType) {
                if (roomType == null) return "";
                return roomType.getName() +
                        " (" + roomType.getCapacity() + " kişi) - " +
                        roomType.getBasePrice();
            }

            @Override
            public RoomType fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    void roomSearch(ActionEvent event) {

        RoomType selectedType = roomTypeComboBox.getValue();
        LocalDate checkIn = checkInDatePicker.getValue();
        LocalDate checkOut = checkOutDatePicker.getValue();

        if (selectedType == null || checkIn == null || checkOut == null) {
            showAlert("All fields must be filled");
            return;
        }

        if (!checkOut.isAfter(checkIn)) {
            showAlert("Check-out date must be after check-in date");
            return;
        }

        List<AvailableRoomDTO> rooms =
                roomService.search(selectedType.getId(), checkIn, checkOut);

        //  KRİTİK: ObservableList alanı dolduruluyor
        availableRooms = FXCollections.observableArrayList(rooms);
        roomTableView.setItems(availableRooms);
    }

    private void createReservation(AvailableRoomDTO room) {
        try {
            Long customerId = 3L; // şimdilik hardcoded

            roomService.reserveRoom(
                    customerId,
                    room.getRoomId(),
                    checkInDatePicker.getValue(),
                    checkOutDatePicker.getValue()
            );

            // 🔥 EN ÖNEMLİ SATIR
            availableRooms.remove(room);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Reservation created successfully!");
            alert.setContentText(
                    "Reserved for room number: " + room.getRoomNumber()
            );
            alert.showAndWait();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Reservation failed: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void closeForm(ActionEvent event) throws IOException {
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        root = FXMLLoader.load(
                getClass().getResource("/com/hotelreservationsystem/customer_dashboard.fxml")
        );
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
