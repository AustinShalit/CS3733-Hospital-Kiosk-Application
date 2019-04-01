package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;

public class SanitationWindowController extends Controller {

  @FXML
  private Button backButton;
  @FXML
  private Button submitButton;
  @FXML
  private Label titleLabel;
  @FXML
  private TextArea descriptionTextArea;
  @FXML
  private ComboBox locationComboBox;
  @FXML
  private ComboBox<SanitationRequest.SanitationRequestType> categoryComboBox;

  @FXML
  void initialize() throws SQLException {
    categoryComboBox.getItems().setAll(SanitationRequest.SanitationRequestType.values());
    //    locationComboBox.getItems().setAll(db.getAllSanitationRequests());
  }


  @FXML
  void onBackButtonAction() {
    switchScenes("MainSanitationWindow.fxml", backButton.getScene().getWindow());
  }


  @FXML
  void onSubmitButtonAction() {
    submitButton.setText("Not yet implemented");
  }
}
