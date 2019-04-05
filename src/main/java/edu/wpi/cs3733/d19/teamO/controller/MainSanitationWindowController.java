package edu.wpi.cs3733.d19.teamO.controller;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;

public class MainSanitationWindowController extends Controller {

  @FXML
  private JFXButton addSanitationButton;
  @FXML
  private JFXButton viewSanitationButton;
  @FXML
  private JFXButton goBackButton;

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
