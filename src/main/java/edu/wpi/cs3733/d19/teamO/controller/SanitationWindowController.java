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
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.util.Callback;

import edu.wpi.cs3733.d19.teamO.controller.exception.InvalidUserInputException;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@SuppressWarnings("PMD")
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
  private ComboBox<Node> locationComboBox;
  @FXML
  private ComboBox<SanitationRequest.SanitationRequestType> categoryComboBox;

  @FXML
  void initialize() throws SQLException {
    database = new Database();
    locationComboBox.getItems().setAll(database.getAllNodes());
    // Super verbose, but makes it so that just the long name for each node is displayed
    locationComboBox.setCellFactory(new Callback<ListView<Node>, ListCell<Node>>() {
      @Override
      public ListCell<Node> call(ListView<Node> param) {
        return new ListCell<Node>() {
          private final Text text;

          {
            setContentDisplay(ContentDisplay.CENTER);
            text = new Text();
          }

          @Override
          protected void updateItem(Node item, boolean empty) {

            super.updateItem(item, empty);

            if (item == null || empty) {
              setGraphic(null);
            } else {
              text.setText(item.getLongName());
              setGraphic(text);
            }
          }
        };
      }
    });
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
      String nodeId = locationComboBox.getValue().toString();
      Node node = database.getNode(nodeId).get();

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
