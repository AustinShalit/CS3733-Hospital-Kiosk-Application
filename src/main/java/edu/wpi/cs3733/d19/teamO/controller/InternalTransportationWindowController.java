package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;

import edu.wpi.cs3733.d19.teamO.controller.exception.InvalidUserInputException;
import edu.wpi.cs3733.d19.teamO.entity.InternalTransportationRequest;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class InternalTransportationWindowController extends Controller {

  @FXML
  private JFXTextField nametxt;
  @FXML
  private JFXComboBox<Node> locationbox;
  @FXML
  private JFXComboBox<InternalTransportationRequest.InternalTransportationRequestType> categorybox;
  @FXML
  private JFXTextArea descriptiontxt;
  @FXML
  private JFXButton submitbtn;
  @FXML
  private JFXButton backbtn;

  private Database database;

  @FXML
  void initialize() throws SQLException {
    database = new Database();
    populateComboBox(database, locationbox);
    categorybox.getItems().setAll(InternalTransportationRequest.InternalTransportationRequestType
        .values());
  }

  @FXML
  void onbackButtonAction() {
    switchScenes("MainWindow.fxml", backbtn.getScene().getWindow());
  }

  @FXML
  void onSubmitButtonAction() {
    try {
      InternalTransportationRequest sr = parseUserITRequest();
      if (database.insertInternalTransportationRequest(sr)) {
        String message = "Successfully submitted internal transportation request.";
        showInformationAlert("Success!", message);
      } else {
        showErrorAlert("Error.",
            "Unable to submit internal transportation request.");
      }
    } catch (InvalidUserInputException ex) {
      Logger logger = Logger.getLogger(SanitationWindowController.class.getName());
      logger.log(Level.WARNING, "Unable to parse internal transportation Request.", ex);
    }
  }

  /**
   * Parse input the user has inputted for the sanitation request.
   *
   * @return A SanitationRequest representing the users input.
   */
  private InternalTransportationRequest parseUserITRequest() throws InvalidUserInputException {
    // if input is valid, parse it and return a new SanitationRequest
    if (!descriptiontxt.getText().isEmpty()
        && !nametxt.getText().isEmpty()
        && Objects.nonNull(locationbox.getValue())
        && Objects.nonNull(categorybox.getValue())) {

      LocalDateTime now = LocalDateTime.now();
      Node node = (Node) locationbox.getValue();

      String type = categorybox.getValue().toString().toUpperCase(new Locale("EN"));
      InternalTransportationRequest.InternalTransportationRequestType srt =
          InternalTransportationRequest.InternalTransportationRequestType.valueOf(type);

      String description = descriptiontxt.getText();
      String name = nametxt.getText();

      return new InternalTransportationRequest(now, node, srt, description, name);
    }

    // otherwise, some input was invalid
    showErrorAlert("Error.", "Please make sure all fields are filled out.");
    throw new InvalidUserInputException("Unable to parse Internal Transportation Request.");
  }

}
