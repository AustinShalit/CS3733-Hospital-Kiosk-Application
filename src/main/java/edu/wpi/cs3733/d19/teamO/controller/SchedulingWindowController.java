package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@SuppressWarnings("PMD")
public class SchedulingWindowController extends Controller {

  @FXML
  private Button backButton;
  @FXML
  private Button submitButton;
  @FXML
  private JFXComboBox<Node> roomComboBox;
  @FXML
  private TextField nameBox;
  @FXML
  private JFXTimePicker startChoiceBox;
  @FXML
  private JFXTimePicker endChoiceBox;
  @FXML
  private JFXDatePicker datePicker;
  @FXML
  private Label submitStatus;

  private Database database;

  @FXML
  void onBackButtonAction(ActionEvent event) {
    if (event.getSource() == backButton) {
      switchScenes("MainWindow.fxml", backButton.getScene().getWindow());
    }
  }

  /**
   * Check to make sure Scheduling Request is valid.
   *
   * @param e Action Event from Submit button
   */
  @FXML
  void onSubmitButtonAction(ActionEvent e) {
    if (e.getSource() == submitButton) {
      submitStatus.setText("All fields are filled. Nice!");
    } else {
      submitStatus.setText("Make sure all fields are filled in.");
    }

  }

  /**
   * Populate the Room selection ComboBox.
   *
   * @throws SQLException When stuff goes wrong.
   */
  @FXML
  public void initialize() throws SQLException {
    database = new Database();
    populateComboBox(database, roomComboBox);
    submitButton.disableProperty().bind(new BooleanBinding() {
      {
        super.bind(nameBox.textProperty(),
            startChoiceBox.valueProperty(),
            endChoiceBox.valueProperty(),
            datePicker.valueProperty(),
            roomComboBox.valueProperty());
      }

      @Override
      protected boolean computeValue() {
        return nameBox.getText() == null
            || startChoiceBox.getValue() == null
            || endChoiceBox.getValue() == null
            || datePicker.getValue() == null
            || roomComboBox.getValue() == null;
      }
    });
  }

}
