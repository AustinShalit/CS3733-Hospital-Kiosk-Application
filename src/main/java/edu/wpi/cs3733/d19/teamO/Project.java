package edu.wpi.cs3733.d19.teamO;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Project extends Application {

  @Override
  public void start(final Stage primaryStage) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
    Pane mainWindow = loader.load();

    primaryStage.setTitle("Project Title");
    primaryStage.setScene(new Scene(mainWindow));
    primaryStage.show();
  }

}
