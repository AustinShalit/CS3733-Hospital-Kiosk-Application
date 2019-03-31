package edu.wpi.cs3733.d19.teamO.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;

public class NagivationWindowController extends Controller {
  @FXML
  private JFXRadioButton currLocJFXRadio;
  @FXML
  private JFXRadioButton selectLocJFXRadio;
  @FXML
  private JFXComboBox selectLocJFXCombo;
  @FXML
  private ToggleGroup locationToggle;

  @FXML
  void onCurrLocRadioAction(ActionEvent event) {
  }

  @FXML
  void onSelectLocRadioAction(ActionEvent event) {
    if (selectLocJFXRadio.isPressed()) {
      selectLocJFXCombo.setDisable(false);
    } else {
      selectLocJFXCombo.setDisable(true);
    }
  }
}
