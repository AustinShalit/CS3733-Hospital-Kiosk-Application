package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SchedulingRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@SuppressWarnings("PMD")
public class SchedulingWindowController extends Controller {

  @FXML
  private JFXButton backButton;
  @FXML
  private JFXButton submitButton;
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
  @FXML
  private JFXButton viewSchedulingButton;

  private Database database;

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

  /**
   * Catches a button click and switches scenes back to mainWindow.fxml.
   *
   * @param event action event to be handled.
   */
  @FXML
  void onBackButtonAction(ActionEvent event) {
    if (event.getSource() == backButton) {
      switchScenes("MainWindow.fxml", backButton.getScene().getWindow());
    }
  }

  /**
   * Check to make sure Scheduling Request is valid.
   */
  @FXML
  void onSubmitButtonAction() {
    SchedulingRequest request = parseUserSchedulingRequestTest();
    if (database.insertSchedulingRequest(request)) {
      String message = "Successfully submitted scheduling request.";
      showInformationAlert("Success!", message);
    } else {
      showErrorAlert("Error.", "Unable to submit scheduling request.");
    }
  }

  /**
   * Parse input the user has inputted for the scheduling request.
   *
   * @return A SchedulingRequest representing the users input.
   */
  private SchedulingRequest parseUserSchedulingRequestTest() {
    // if input is valid, parse it and return a new SanitationRequest
    if (Objects.nonNull(startChoiceBox.getValue())
        && Objects.nonNull(endChoiceBox.getValue())
        && Objects.nonNull(datePicker.getValue())
        && (!nameBox.getText().isEmpty())
        && (!roomComboBox.getValue().getNodeId().isEmpty())) {

      LocalDateTime start = LocalDateTime.of(datePicker.getValue(), startChoiceBox.getValue());
      LocalDateTime end = LocalDateTime.of(datePicker.getValue(), endChoiceBox.getValue());
      LocalDateTime now = LocalDateTime.now();
      String name = nameBox.getText();
      Node roomNode = roomComboBox.getValue();


      return new SchedulingRequest(start, end, now, now, name, roomNode);
    }

    // otherwise, some input was invalid
    showErrorAlert("Error.", "Please make sure all fields are filled out.");
    return null;
  }


  @FXML
  void viewSchedulingButtonAction() {
    switchScenes("SchedulingViewWindow.fxml", viewSchedulingButton.getScene().getWindow());
  }

}
