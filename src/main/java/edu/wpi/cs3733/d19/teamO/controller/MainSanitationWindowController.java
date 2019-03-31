package edu.wpi.cs3733.d19.teamO.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainSanitationWindowController extends Controller {

  @FXML
  private Button addSanitationButton;
  @FXML
  private Button viewSanitationButton;
  @FXML
  private Button goBackButton;

  @FXML
  void addSanitationButtonAction(ActionEvent event) {
    if (event.getSource() == addSanitationButton) {
      switchScenes("SanitationWindow.fxml", addSanitationButton.getScene().getWindow());
    }
  }

  @FXML
  void viewSanitationButtonAction(ActionEvent event) {
    if (event.getSource() == viewSanitationButton) {
      switchScenes("SanitationViewWindow.fxml", viewSanitationButton.getScene().getWindow());
    }
  }

  @FXML
  void goBackButtonAction(ActionEvent event) {
    if (event.getSource() == goBackButton) {
      switchScenes("MainWindow.fxml", goBackButton.getScene().getWindow());
    }
  }
}
