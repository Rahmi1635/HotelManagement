package com.hotelreservationsystem.ui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoader {

    public static void loadProfile(String fxmlName, Button btn) {
        try {
            Stage stage = (Stage) btn.getScene().getWindow();
            Parent root = FXMLLoader.load(
                    SceneLoader.class.getResource("/com/hotelreservationsystem/" + fxmlName)
            );
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
