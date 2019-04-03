package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import edu.wpi.cs3733.d19.teamO.controller.exception.InvalidUserInputException;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@SuppressWarnings("PMD")
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
  private ComboBox<Node> locationComboBox;
  @FXML
  private ComboBox<SanitationRequest.SanitationRequestType> categoryComboBox;

  private Database database;

  @FXML
  void initialize() throws SQLException {
    database = new Database();
    populateComboBox(database, locationComboBox);
    categoryComboBox.getItems().setAll(SanitationRequest.SanitationRequestType.values());
  }


  @FXML
  void onBackButtonAction() {
    switchScenes("MainSanitationWindow.fxml", backButton.getScene().getWindow());
  }


  @FXML
  void onSubmitButtonAction() {
    try {
      SanitationRequest sr = parseUserSanitationRequestTest();
      if (database.insertSanitationRequest(sr)) {
        String message = "Successfully submitted sanitation request.";
        showInformationAlert("Success!", message);
      } else {
        showErrorAlert("Error.", "Unable to submit sanitation request.");
      }
    } catch (InvalidUserInputException ex) {
      Logger logger = Logger.getLogger(SanitationWindowController.class.getName());
      logger.log(Level.WARNING, "Unable to parse Sanitation Request.", ex);
    }
  }

  /**
   * Parse input the user has inputted for the sanitation request.
   *
   * @return A SanitationRequest representing the users input.
   */
  private SanitationRequest parseUserSanitationRequestTest() throws InvalidUserInputException {
    // if input is valid, parse it and return a new SanitationRequest
    if (!descriptionTextArea.getText().isEmpty()
        && Objects.nonNull(locationComboBox.getValue())
        && Objects.nonNull(categoryComboBox.getValue())) {

      LocalDateTime now = LocalDateTime.now();
      Node node = locationComboBox.getValue();

      String type = categoryComboBox.getValue().toString().toUpperCase(new Locale("EN"));
      SanitationRequest.SanitationRequestType srt =
          SanitationRequest.SanitationRequestType.valueOf(type);

      String description = descriptionTextArea.getText();

      return new SanitationRequest(now, node, srt, description);
    }

    // otherwise, some input was invalid
    showErrorAlert("Error.", "Please make sure all fields are filled out.");
    throw new InvalidUserInputException("Unable to parse Sanitation Request.");
  }

}
