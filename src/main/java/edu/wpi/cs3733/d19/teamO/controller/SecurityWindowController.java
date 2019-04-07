package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SecurityRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@SuppressWarnings("PMD")
public class SecurityWindowController extends Controller {

  private Database database;

  @FXML
  private Button backButton;

  @FXML
  private Button sendHelpButton;

  @FXML
  private Button viewSecurityRequestsButton;

  @FXML
  private ComboBox<Node> locationComboBox;

  @FXML
  void initialize() throws SQLException {
    database = new Database();
    populateComboBox(database, locationComboBox);
  }

  @FXML
  void onBackButtonAction() {
    switchScenes("MainWindow.fxml", backButton.getScene().getWindow());
  }

  @FXML
  void onSendHelpButtonAction() {
    SecurityRequest sr = parseUserSecurityRequest();
    if (sr == null) {
      Logger logger = Logger.getLogger(SanitationWindowController.class.getName());
      logger.log(Level.WARNING,
          "Unable to parse Security Request.",
          "Unable to parse Security Request.");
      return;
    }

    if (database.insertSecurityRequest(sr)) {
      String message = "Successfully submitted security request. Help is on the way.";
      showInformationAlert("Success!", message);
    } else {
      showErrorAlert("Error.", "Unable to submit security request.");
    }
  }

  @FXML
  void onViewSecurityRequestsAction() {
    switchScenes("SecurityViewWindow.fxml", viewSecurityRequestsButton.getScene().getWindow());

  }

  /**
   * Parse input the user has inputted for the security request.
   *
   * @return If valid input, A SecurityRequest representing the users input. Otherwise null.
   */
  private SecurityRequest parseUserSecurityRequest() {
    // if input is valid, parse it and return a new SecurityRequest
    if (Objects.nonNull(locationComboBox.getValue())) {

      LocalDateTime now = LocalDateTime.now();
      Node node = locationComboBox.getValue();

      return new SecurityRequest(now, node);
    }

    // otherwise, some input was invalid
    showErrorAlert("Error.", "Please make sure to select a location.");
    return null;
  }

}
