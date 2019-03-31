package edu.wpi.cs3733.d19.teamO.controller;

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
  void addSanitationButtonAction() {
    switchScenes("SanitationWindow.fxml", addSanitationButton.getScene().getWindow());
  }

  @FXML
  void viewSanitationButtonAction() {
    switchScenes("SanitationViewWindow.fxml", viewSanitationButton.getScene().getWindow());
  }

  @FXML
  void goBackButtonAction() {
    switchScenes("MainWindow.fxml", goBackButton.getScene().getWindow());
  }
}
