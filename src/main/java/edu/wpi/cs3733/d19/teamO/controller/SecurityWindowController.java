package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;

import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@SuppressWarnings("PMD")
public class SecurityWindowController extends Controller {

  @FXML
  private Button backButton;

  @FXML
  private Button alertbutton;

  @FXML
  private Button getnodesbutton;

  @FXML
  private Text securityTitle;

  @FXML
  private JFXComboBox insertlocationdropdown;

  @FXML
  void chooseLocation(ActionEvent event) {

  }

  private Database database;

  @FXML
  void initialize() throws SQLException {
    database = new Database();
    populateComboBox(database, insertlocationdropdown);
  }

  @FXML
  void onBackButtonAction(ActionEvent event) {
    if (event.getSource() == backButton) {
      switchScenes("MainWindow.fxml", backButton.getScene().getWindow());
    }
  }


}
