package edu.wpi.cs3733d18.onyx_owlmen.databasePrototype;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DatabasePrototype extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("displayDatabase.fxml"));
        primaryStage.setTitle("Database Prototype");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
