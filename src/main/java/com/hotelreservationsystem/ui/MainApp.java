package com.hotelreservationsystem.ui;

import com.hotelreservationsystem.config.SingletonDatabaseManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
// Hello our project
public class MainApp extends Application{

    public static void main(String[] args)
    {
        launch(args);
    }

    private double x=0;
    private double y=0;

    @Override
    public void start(Stage stage) throws Exception {

        SingletonDatabaseManager.getInstance().getConnection();


        Parent root=FXMLLoader.load(getClass().getResource("/com/hotelreservationsystem/main.fxml"));

        root.setOnMousePressed((MouseEvent event)-> {
            x=event.getSceneX();
            y=event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event)->{
            stage.setX(event.getScreenX()-x);
            stage.setY(event.getScreenY()-y);

            stage.setOpacity(.4);
        }) ;

        root.setOnMouseReleased((MouseEvent event)->{
            stage.setOpacity(1);

        });

        Scene scene =new Scene(root,600,500);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Home");
        stage.show();
    }



}
