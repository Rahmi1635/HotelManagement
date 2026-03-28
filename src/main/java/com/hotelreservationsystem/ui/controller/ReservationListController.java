package com.hotelreservationsystem.ui.controller;

import com.hotelreservationsystem.domain.reservation.Reservation;
import com.hotelreservationsystem.domain.reservation.ReservationStatus;
import com.hotelreservationsystem.domain.reservation.dto.ReservationListItem;
import com.hotelreservationsystem.service.reservation.StaffReservationService;

import com.hotelreservationsystem.service.reservation.strategy.*;
import javafx.beans.property.SimpleStringProperty;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReservationListController {

    @FXML private DatePicker dpFrom;
    @FXML private DatePicker dpTo;
    @FXML private TextField txtCustomer;
    @FXML private TextField txtRoomNo;
    @FXML private TextField txtRoomType;
    @FXML private ComboBox<String> cmbStatus;

    @FXML private TableView<ReservationListItem> tbl;
    @FXML private TableColumn<ReservationListItem, String> colId;
    @FXML private TableColumn<ReservationListItem, String> colCustomer;
    @FXML private TableColumn<ReservationListItem, String> colRoomNo;
    @FXML private TableColumn<ReservationListItem, String> colRoomType;
    @FXML private TableColumn<ReservationListItem, String> colIn;
    @FXML private TableColumn<ReservationListItem, String> colOut;
    @FXML private TableColumn<ReservationListItem, String> colStatus;
    @FXML private TableColumn<ReservationListItem, String> colTotal;

    private final StaffReservationService staffService = new StaffReservationService();

    private Map<Long, Integer> roomIdNumberMap;
    private Map<Long, String> customerIdNameMap;
    private Map<Long, String> roomIdTypeMap;







    @FXML
    public void initialize() {

        roomIdNumberMap = staffService.getRoomIdNumberMap();
        customerIdNameMap = staffService.getCustomerIdNameMap();
        roomIdTypeMap = staffService.getRoomIdTypeMap();


        colId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getReservationId())));
        colCustomer.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCustomerFullName()));
        colRoomNo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRoomNumber()));
        colRoomType.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRoomType()));
        colIn.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getPlannedCheckIn())));
        colOut.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getPlannedCheckOut())));
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));
        colTotal.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getTotalPrice() == null ? "" : c.getValue().getTotalPrice().toString()
        ));

        cmbStatus.setItems(FXCollections.observableArrayList(
                "ALL",
                ReservationStatus.PENDING.name(),
                ReservationStatus.CONFIRMED.name(),
                ReservationStatus.CHECKED_IN.name(),
                ReservationStatus.CHECKED_OUT.name(),
                ReservationStatus.CANCELLED.name()
        ));
        cmbStatus.getSelectionModel().select("ALL");

        // İlk açılışta tüm liste
        doSearch();
    }

    @FXML
    private void search(ActionEvent e) {
       searchByStrategies();

    }

    @FXML
    private void clear(ActionEvent e) {
        dpFrom.setValue(null);
        dpTo.setValue(null);
        txtCustomer.clear();
        txtRoomNo.clear();
        txtRoomType.clear();
        cmbStatus.getSelectionModel().select("ALL");
        doSearch();
    }

    private void doSearch() {
        LocalDate from = dpFrom.getValue();
        LocalDate to = dpTo.getValue();

        if (from != null && to != null && from.isAfter(to)) {
            showWarn("From date cannot be after To date.");
            return;
        }

        String customer = normalize(txtCustomer.getText());
        String roomNo = normalize(txtRoomNo.getText());
        String roomType = normalize(txtRoomType.getText());

        ReservationStatus st = null;
        String stStr = cmbStatus.getValue();
        if (stStr != null && !"ALL".equals(stStr)) {
            st = ReservationStatus.valueOf(stStr);
        }

        tbl.setItems(FXCollections.observableArrayList(
                staffService.searchReservations(from, to, customer, roomNo, roomType, st)
        ));
    }

    private String normalize(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
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

    private void showWarn(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void searchByStrategies() {

        List<ReservationFilterStrategy> strategies = new ArrayList<>();

        // DATE RANGE
        if (dpFrom.getValue() != null && dpTo.getValue() != null) {
            strategies.add(
                    new FilterByDateRangeStrategy(
                            dpFrom.getValue(),
                            dpTo.getValue()
                    )
            );
        }

        // ROOM NUMBER
        if (!txtRoomNo.getText().isBlank()) {
            int roomNumber = Integer.parseInt(txtRoomNo.getText().trim());
            strategies.add(
                    new FilterByRoomNumberStrategy(
                            roomNumber,
                            staffService.getRoomIdNumberMap()
                    )
            );
        }

        // STATUS
        if (cmbStatus.getValue() != null && !"ALL".equals(cmbStatus.getValue())) {
            strategies.add(
                    new FilterByStatusStrategy(
                            ReservationStatus.valueOf(cmbStatus.getValue())
                    )
            );
        }

        //CUSTOMER NAME
        if (!txtCustomer.getText().isBlank()) {
            strategies.add(
                    new FilterByCustomerNameStrategy(
                            txtCustomer.getText().trim(),
                            staffService.getCustomerIdNameMap() // varsa
                    )
            );
        }

        if (!txtRoomType.getText().isBlank()) {
            strategies.add(
                    new FilterByRoomTypeStrategy(
                            txtRoomType.getText().trim(),
                            roomIdTypeMap
                    )
            );
        }

        // BASE LIST
        List<Reservation> result =
                staffService.findAllReservationsRaw();

        // APPLY STRATEGIES
        for (ReservationFilterStrategy s : strategies) {
            result = s.filter(result);
        }

        // UI'ya bas
        tbl.setItems(FXCollections.observableArrayList(
                mapToListItem(result)
        ));
    }

    private List<ReservationListItem> mapToListItem(List<Reservation> list) {
        return list.stream()
                .map(this::toListItem)
                .toList();
    }


    private ReservationListItem toListItem(Reservation r) {


        Integer roomNoInt = roomIdNumberMap.get(r.getRoomId());
        String roomNo = roomNoInt != null ? roomNoInt.toString() : "";

        String roomType = roomIdTypeMap.getOrDefault(r.getRoomId(), "");

        String customerName =
                customerIdNameMap.getOrDefault(r.getCustomerId(), "");

        return new ReservationListItem(
                r.getId(),
                customerName,
                roomNo,
                roomType,
                r.getPlannedCheckIn(),
                r.getPlannedCheckOut(),
                r.getStatus().name(),
                r.getTotalPrice()
        );

    }





}
