package edu.wpi.cs3733.d19.teamO.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;

public class SecurityWindowController extends Controller {

  @FXML
  private Button backButton;

  @FXML
  private Button alertbutton;

  @FXML
  private JFXTextField insertLocationTextArea;

  @FXML
  private Text securityTitle;

  @FXML
  private JFXComboBox insertlocationdropdown;

  @FXML
  void initialize() {
    insertlocationdropdown.getItems().addAll(
        "Place 1",
        "Place 2",
        "Place 3",
        "Place 4"
    );
  }


  @FXML
  void onBackButtonAction(ActionEvent event) {
    if (event.getSource() == backButton) {
      switchScenes("MainWindow.fxml", backButton.getScene().getWindow());
    }
  }











}
