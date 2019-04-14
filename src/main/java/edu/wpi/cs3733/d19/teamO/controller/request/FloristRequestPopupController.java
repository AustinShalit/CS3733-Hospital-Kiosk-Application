package edu.wpi.cs3733.d19.teamO.controller.request;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.entity.FloristRequest;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "FloristRequestPopup.fxml")
public class FloristRequestPopupController implements Controller {

  private static final Logger logger =
      Logger.getLogger(FloristRequestPopupController.class.getName());

  @FXML
  BorderPane root;
  @FXML
  private JFXTextField nametxt;
  @FXML
  private JFXComboBox<Node> locationbox;
  @FXML
  private JFXComboBox<FloristRequest.FloristRequestType> categorybox;
  @FXML
  private JFXTextArea descriptiontxt;
  @FXML
  private JFXButton submitbtn;

  @Inject
  private Database db;

  @FXML
  void initialize() {
    DialogHelper.populateComboBox(db, locationbox);
    categorybox.getItems().setAll(FloristRequest.FloristRequestType
        .values());

    submitbtn.disableProperty().bind(
        Bindings.isEmpty(nametxt.textProperty())
            .or(Bindings.isNull(locationbox.valueProperty()))
            .or(Bindings.isNull(categorybox.valueProperty()))
    );
  }

  @FXML
  void onSubmitButtonAction() {
    FloristRequest florist = parseUserFloristRequest();
    if (florist == null) {
      logger.log(Level.WARNING,
          "Unable to parse florist Request.",
          "Unable to parse florist Request.");
      return;
    }

    nametxt.setText(null);
    locationbox.getSelectionModel().clearSelection();
    locationbox.setValue(null);
    categorybox.getSelectionModel().clearSelection();
    categorybox.setValue(null);
    descriptiontxt.setText(null);

    if (db.insertFloristRequest(florist)) {
      String message = "Successfully submitted florist request.";
      DialogHelper.showInformationAlert("Success!", message);
    } else {
      DialogHelper.showErrorAlert("Error.",
          "Unable to submit florist request.");
    }
  }

  /**
   * Parse input the user has inputted for the florist request.
   *
   * @return If valid input, A florist Request representing the users input. Otherwise null.
   */
  private FloristRequest parseUserFloristRequest() {
    // if input is valid, parse it and return a new florist request
    if (!nametxt.getText().isEmpty()
        && Objects.nonNull(locationbox.getValue())
        && Objects.nonNull(categorybox.getValue())) {

      LocalDateTime now = LocalDateTime.now();
      Node node = (Node) locationbox.getValue();

      String type = categorybox.getValue().toString().toUpperCase(new Locale("EN"));
      FloristRequest.FloristRequestType floristRequest =
          FloristRequest.FloristRequestType.valueOf(type);

      String description = descriptiontxt.getText();
      String name = nametxt.getText();

      return new FloristRequest(now, node, floristRequest, description, name);
    }

    // otherwise, some input was invalid
    DialogHelper.showErrorAlert("Error.", "Please make sure all fields are filled out.");
    return null;
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    FloristRequestPopupController create();
  }
}
