package com.hotelreservationsystem.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {


    Parent root;
    Stage stage;
    Scene scene;

    public void directLogIn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.setTitle("Log In");
        stage.show();
    }

    public void signupBtn(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotelreservationsystem/signup.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root,600,650);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Sign Up");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeForm() throws IOException {
        System.exit(0);
    }



}
