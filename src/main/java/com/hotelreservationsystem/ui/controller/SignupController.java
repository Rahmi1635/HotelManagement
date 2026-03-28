package com.hotelreservationsystem.ui.controller;

import com.hotelreservationsystem.config.SingletonDatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.mindrot.jbcrypt.BCrypt;

public class SignupController {

    Parent root;
    Stage stage;
    Scene scene;

    @FXML
    private Label errorConfirmPassword;

    @FXML
    private Label errorEmail;

    @FXML
    private Label errorFirstName;

    @FXML
    private Label errorLastName;

    @FXML
    private Label errorPassword;

    @FXML
    private Label errorUsername;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;


    @FXML
    void signupAction(ActionEvent event) {

        boolean valid=true;

        resetErrorStyle();

        if(firstNameField.getText().isEmpty())
        {
            setError(firstNameField,errorFirstName,"Zorunlu");
            valid=false;
        }
        if(lastNameField.getText().isEmpty())
        {
            setError(lastNameField,errorLastName,"Zorunlu");
            valid=false;
        }
        if(usernameField.getText().isEmpty())
        {
            setError(usernameField,errorUsername,"Zorunlu");
            valid=false;
        }
        if(emailField.getText().isEmpty())
        {
            setError(emailField,errorEmail,"Zorunlu");
            valid=false;
        }
        if(passwordField.getText().isEmpty())
        {
            setError(passwordField,errorPassword,"Zorunlu");
            valid=false;
        }
        if(confirmPasswordField.getText().isEmpty())
        {
            setError(confirmPasswordField,errorConfirmPassword,"Zorunlu");
            valid=false;
        }
        if(!valid)
        {
            return;
        }
        if(!passwordField.getText().equals(confirmPasswordField.getText()))
        {
            setError(confirmPasswordField,errorConfirmPassword,"Şifre Eşleşmiyor");
            return;
        }
        if(passwordField.getText().length()<=6)
        {
            setError(passwordField,errorPassword,"Minimum 6 karakter");
            return;
        }

        String hashedPassword=BCrypt.hashpw(passwordField.getText(),BCrypt.gensalt());

        try{
            Connection connection= SingletonDatabaseManager.getInstance().getConnection();
            String sql="INSERT INTO Person(first_name, last_name, username, email, hashed_password, role) VALUES (?, ?, ?, ?, ?, 'CUSTOMER')";
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,firstNameField.getText());
            ps.setString(2,lastNameField.getText());
            ps.setString(3,usernameField.getText());
            ps.setString(4,emailField.getText());
            ps.setString(5,hashedPassword);
            ps.executeUpdate();

            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Kayıt Başarılı");
            alert.setContentText("Müşteri kaydı başarılı şekilde oluşturuldu");
            alert.showAndWait();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("VeriTabanı Hatası");
            alert.setContentText("Kayıt İşlemi Tamamlanamadı");
            alert.showAndWait();
        }


    }

    private void setError(TextField field,Label label,String message)
    {
        field.setStyle("-fx-border-color:red;-fx-border-width:2;");
        label.setText(message);
        label.setTextFill(Color.RED);
    }

    private void resetErrorStyle()
    {
        firstNameField.setStyle(null);
        lastNameField.setStyle(null);
        usernameField.setStyle(null);
        emailField.setStyle(null);
        passwordField.setStyle(null);
        confirmPasswordField.setStyle(null);

        errorFirstName.setText("");
        errorLastName.setText("");
        errorUsername.setText("");
        errorEmail.setText("");
        errorPassword.setText("");
        errorConfirmPassword.setText("");
    }



    @FXML
    void closeForm(ActionEvent event) throws IOException {
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        root= FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/main.fxml"));
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
