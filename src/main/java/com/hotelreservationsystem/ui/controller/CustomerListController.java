package com.hotelreservationsystem.ui.controller;

import com.hotelreservationsystem.domain.user.Customer;
import com.hotelreservationsystem.service.customer.StaffCustomerService;

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
import java.util.List;
import java.util.Objects;

public class CustomerListController {

    @FXML private TextField txtId;
    @FXML private TextField txtTcno;
    @FXML private TextField txtPhone;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;

    @FXML private TableView<Customer> tblCustomers;
    @FXML private TableColumn<Customer, String> colId;
    @FXML private TableColumn<Customer, String> colFirstName;
    @FXML private TableColumn<Customer, String> colLastName;
    @FXML private TableColumn<Customer, String> colUsername;
    @FXML private TableColumn<Customer, String> colEmail;
    @FXML private TableColumn<Customer, String> colPhone;
    @FXML private TableColumn<Customer, String> colTcno;

    @FXML private TextField txtSelected;

    private final StaffCustomerService customerService = new StaffCustomerService();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getUser_id())));
        colFirstName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFirst_name()));
        colLastName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLast_name()));
        colUsername.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsername()));
        colEmail.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));
        colPhone.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPhone()));
        colTcno.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTc()));

        loadAll();

        tblCustomers.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n == null) {
                txtSelected.clear();
            } else {
                txtSelected.setText(
                        n.getUser_id() + " - " +
                                safe(n.getFirst_name()) + " " + safe(n.getLast_name()) +
                                " | " + safe(n.getPhone()) +
                                " | " + safe(n.getEmail())
                );
            }
        });
    }

    private void loadAll() {
        tblCustomers.setItems(FXCollections.observableArrayList(customerService.listAllCustomers()));
    }

    @FXML
    private void search(ActionEvent e) {
        try {
            List<Customer> results = customerService.searchCustomers(
                    txtFirstName.getText(),
                    txtLastName.getText(),
                    txtPhone.getText(),
                    txtTcno.getText(),
                    txtId.getText()
            );
            tblCustomers.setItems(FXCollections.observableArrayList(results));
        } catch (Exception ex) {
            ex.printStackTrace();
            showError(ex.getMessage());
        }
    }

    @FXML
    private void clear(ActionEvent e) {
        txtId.clear();
        txtTcno.clear();
        txtPhone.clear();
        txtFirstName.clear();
        txtLastName.clear();
        tblCustomers.getSelectionModel().clearSelection();
        txtSelected.clear();
    }

    @FXML
    private void refresh(ActionEvent e) {
        clear(null);
        loadAll();
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

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
