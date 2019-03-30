package edu.wpi.cs3733.d19.teamO.controller;

import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.sql.SQLException;

public class SanitationWindowController extends Controller {

  private Database database;

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
    database = new Database();
    categoryComboBox.getItems().setAll(SanitationRequest.SanitationRequestType.values());
    //locationComboBox.getItems().setAll(database.getAllNodes());
  }


  @FXML
  void onBackButtonAction(ActionEvent event) {
    if (event.getSource() == backButton) {
      switchScenes("MainSanitationWindow.fxml", backButton.getScene().getWindow());
    }
  }

  @FXML
  void onSubmitButtonAction(ActionEvent event) {
    if(event.getSource() == submitButton) {
      submitButton.setText("Not yet implemented");
    }
  }
}
