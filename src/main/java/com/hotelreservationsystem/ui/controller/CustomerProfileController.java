package com.hotelreservationsystem.ui.controller;

import com.hotelreservationsystem.context.SessionContext;
import com.hotelreservationsystem.domain.user.Customer;
import com.hotelreservationsystem.domain.user.Person;
import com.hotelreservationsystem.service.customer.CustomerService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerProfileController implements Initializable {

    private final CustomerService customerService=new CustomerService();

    Parent root;
    Scene scene;
    Stage stage;

    @FXML
    private Label lblCustomerName;

    @FXML
    private Circle profilePhoto;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtPhone;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Person currentUser = SessionContext.getCurrentUser();

        if (currentUser instanceof Customer customer) {
            lblCustomerName.setText(customer.getUsername());
            txtFirstName.setText(customer.getFirst_name());
            txtLastName.setText(customer.getLast_name());
            txtEmail.setText(customer.getEmail());
            txtPhone.setText(customer.getPhone());
        }
    }

    @FXML
    void saveInfo(ActionEvent event) {

        Person user= SessionContext.getCurrentUser();

        if(!(user instanceof Customer customer))
        {
            showError("Invalid user session");
            return;
        }

        try{
            customerService.updateProfile(customer.getUsername(),
                    txtFirstName.getText(),
                    txtLastName.getText(),
                    txtEmail.getText(),
                    txtPhone.getText(),
                    txtPassword.getText());

            showInfo("Profile updated successfully");
            txtPassword.clear();

        }
        catch (IllegalArgumentException e)
        {
            showError(e.getMessage());
        }
        catch (Exception e) {
            showError("Update failed : " + e.getMessage());
        }
    }

    private void showError(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void closeForm(ActionEvent e) throws IOException {

        stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        root= FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/customer_dashboard.fxml"));
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
