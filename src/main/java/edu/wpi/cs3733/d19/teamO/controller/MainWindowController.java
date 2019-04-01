package edu.wpi.cs3733.d19.teamO.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
  void navigationButtonAction(ActionEvent event) {
    this.switchScenes("NavigationWindow.fxml", navigationButton.getScene().getWindow());
  }

  @FXML
  void mapToolButtonAction(ActionEvent event) {
    if (event.getSource() == mapToolButton) {
      mapToolButton.setText("Not yet implemented");
    }
  }

  @FXML
  void sanitationRequestButtonAction(ActionEvent event) {
    if (event.getSource() == sanitationRequestButton) {
      switchScenes("MainSanitationWindow.fxml", sanitationRequestButton.getScene().getWindow());
    }
  }

  @FXML
  void securityRequestButtonAction(ActionEvent event) {
    switchScenes("SecurityWindow.fxml", securityRequestButton.getScene().getWindow());
  }

  @FXML
  void schedulingButtonButtonAction(ActionEvent event) {
    if (event.getSource() == schedulingButton) {
      switchScenes("SchedulingWindow.fxml", schedulingButton.getScene().getWindow());
    }
  }
}
