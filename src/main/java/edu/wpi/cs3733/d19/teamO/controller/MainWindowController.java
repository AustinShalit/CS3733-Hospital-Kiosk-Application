package edu.wpi.cs3733.d19.teamO.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainWindowController extends Controller {

  @FXML
  private Button navigationButton;
  @FXML
  private Button mapToolButton;
  @FXML
  private Button sanitationRequestButton;
  @FXML
  private Button securityRequestButton;
  @FXML
  private Button schedulingButton;
  @FXML
  private Button logoutButton;
  @FXML
  private Button nodeImportButton;
  @FXML
  private Button edgeImportButton;
  @FXML
  private Label nodeImportInProgress;


  @FXML
  void navigationButtonAction(ActionEvent event) {
    switchScenes("NavigationWindow.fxml", navigationButton.getScene().getWindow());
  }

  @FXML
  void mapToolButtonAction(ActionEvent event) {
    switchScenes("MapEditMain.fxml", mapToolButton.getScene().getWindow());
  }

  @FXML
  void sanitationRequestButtonAction(ActionEvent event) {
    switchScenes("MainSanitationWindow.fxml", sanitationRequestButton.getScene().getWindow());
  }

  @FXML
  void securityRequestButtonAction(ActionEvent event) {
    switchScenes("SecurityWindow.fxml", securityRequestButton.getScene().getWindow());
  }

  @FXML
  void schedulingButtonButtonAction(ActionEvent event) {
    switchScenes("SchedulingWindow.fxml", schedulingButton.getScene().getWindow());
  }


  @FXML
  void logoutButtonAction() {
    switchScenes("LoginWindow.fxml", schedulingButton.getScene().getWindow());
  }

  @FXML
  public void initialize() {
  }
}

