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
    if (event.getSource() == navigationButton) {
      navigationButton.setText("Not yet implemented");
    }
  }

  @FXML
  void mapToolButtonAction(ActionEvent event) {
    //mapToolButton.setText("Not yet implemented");
    if (event.getSource() == mapToolButton) {
      switchScenes("MapEditMain.fxml", mapToolButton.getScene().getWindow());
    }
  }

  @FXML
  void sanitationRequestButtonAction(ActionEvent event) {
    if (event.getSource() == sanitationRequestButton) {
      switchScenes("SanitationWindow.fxml", sanitationRequestButton.getScene().getWindow());
    }
  }

  @FXML
  void securityRequestButtonAction(ActionEvent event) {
    if (event.getSource() == securityRequestButton) {
      securityRequestButton.setText("Not yet implemented");
    }
  }

  @FXML
  void schedulingButtonButtonAction(ActionEvent event) {
    if (event.getSource() == schedulingButton) {
      switchScenes("SchedulingWindow.fxml", schedulingButton.getScene().getWindow());
    }
  }
}
