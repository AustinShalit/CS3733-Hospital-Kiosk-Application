package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SchedulingWindowController extends Controller {

  @FXML
  private Button backButton;
  @FXML
  private Button submitButton;
  @FXML
  private ComboBox<String> roomComboBox;
  @FXML
  private TextField nameBox;
  @FXML
  private JFXTimePicker startChoiceBox;
  @FXML
  private JFXTimePicker endChoiceBox;
  @FXML
  private JFXDatePicker datePicker;

  protected List<String> nodeList = new ArrayList<>();

  @FXML
  void onBackButtonAction(ActionEvent event) {
    if (event.getSource() == backButton) {
      switchScenes("MainWindow.fxml", backButton.getScene().getWindow());
    }
  }
}
