package edu.wpi.cs3733.d19.teamO.controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Window;

class Controller {

  /**
   * Try to switch scenes to the fxml file.
   *
   * @param fxmlFile   String representing the fxml file to switch scenes to
   * @param currWindow The current window
   */
  protected void switchScenes(String fxmlFile, Window currWindow) {
    Stage stage = (Stage) currWindow.getScene().getWindow();
    Parent root = null;
    try {
      root = FXMLLoader.load(getClass().getResource(fxmlFile));
    } catch (IOException exception) {
      exception.printStackTrace();
    }

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Show an information alert, and wait for user to close it.
   */
  protected void showInformationAlert(String titleText, String contentText) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titleText);
    alert.setHeaderText(null);
    alert.setContentText(contentText);

    alert.showAndWait();
  }

  /**
   * Show an error alert, and wait for user to close it.
   */
  protected void showErrorAlert(String titleText, String headerText) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(titleText);
    alert.setHeaderText(headerText);
    alert.setContentText(null);

    alert.showAndWait();
  }
}
