package com.hotelreservationsystem.ui.controller;

import com.hotelreservationsystem.config.SingletonDatabaseManager;
import com.hotelreservationsystem.context.SessionContext;
import com.hotelreservationsystem.domain.user.Customer;
import com.hotelreservationsystem.domain.user.Staff;
import com.hotelreservationsystem.service.auth.AuthService;
import com.sun.glass.ui.Screen;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private final AuthService authService = new AuthService();

    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginBtn;

    private Connection connection;
    private PreparedStatement prepare;
    private ResultSet result;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = SingletonDatabaseManager.getInstance().getConnection();
    }

    public void login() {

        String user = userName.getText().trim();
        String pass = password.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            showError("Username and Password are required");
            return;
        }

        authService.login(user, pass)
                .ifPresentOrElse(person -> {

                    showInfo("Login successful");

                    SessionContext.setCurrentUser(person);

                    if (person instanceof Customer) {
                        SceneLoader.loadProfile(
                                "customer_dashboard.fxml",
                                loginBtn

                        );
                    } else if (person instanceof Staff) {
                        SceneLoader.loadProfile(
                                "staff_dashboard.fxml",
                                loginBtn

                        );
                    }

                }, () -> showError("Wrong username or password"));
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }


    Parent root;
    Stage stage;
    Scene scene;

    public void closeForm(ActionEvent e) throws IOException {

        stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        root=FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/main.fxml"));
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }




}
