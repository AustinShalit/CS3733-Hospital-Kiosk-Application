package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.Time;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class SchedulingWindowController extends Controller {

  @FXML
  private Button backButton;
  @FXML
  private Button submitButton;
  @FXML
  private ComboBox<String> roomComboBox;
  @FXML
  private ChoiceBox<Time> startChoiceBox;
  @FXML
  private ChoiceBox<Time> endChoiceBox;
  @FXML
  private DatePicker datePicker;

  @FXML
  void onBackButtonAction(ActionEvent event) {
    if (event.getSource() == backButton) {
      switchScenes("MainWindow.fxml", backButton.getScene().getWindow());
    }
  }
}
