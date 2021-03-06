package edu.wpi.cs3733.d19.teamO.controller.request;

import java.time.LocalDateTime;
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

import edu.wpi.cs3733.d19.teamO.component.FuzzyWuzzyComboBox;
import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.entity.InterpreterRequest;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "InterpreterPopup.fxml")
public class InterpreterPopupController implements Controller {
  private static final Logger logger = Logger.getLogger(InterpreterPopupController.class.getName());

  @FXML
  BorderPane root;
  @FXML
  private JFXTextField nametxt;
  @FXML
  private FuzzyWuzzyComboBox locationbox;
  @FXML
  private JFXComboBox<InterpreterRequest.Language> categorybox;
  @FXML
  private JFXTextArea descriptiontxt;
  @FXML
  private JFXButton submitbtn;

  @Inject
  private Database db;

  @FXML
  void initialize() {
    locationbox.setNodes(db.getAllNodes());
    locationbox.setupAutoRefresh();
    locationbox.refresh();
    categorybox.getItems().setAll(InterpreterRequest.Language
        .values());

    submitbtn.disableProperty().bind(
        Bindings.isNull(locationbox.valueProperty())
            .or(Bindings.isNull(categorybox.valueProperty()))
            .or(Bindings.isEmpty(nametxt.textProperty()))
    );
  }

  @FXML
  void onSubmitButtonAction() {
    InterpreterRequest interpreter = parseUserInterpreterRequest();
    if (interpreter == null) {
      logger.log(Level.WARNING,
          "Unable to parse interpreter request.",
          "Unable to parse interpreter request.");
      return;
    }

    nametxt.setText(null);
    locationbox.getSelectionModel().clearSelection();
    locationbox.setValue(null);
    categorybox.getSelectionModel().clearSelection();
    categorybox.setValue(null);
    descriptiontxt.setText("");

    if (db.insertInterpreterRequest(interpreter)) {
      String message = "Successfully submitted interpreter request.";
      DialogHelper.showInformationAlert("Success!", message);
    } else {
      DialogHelper.showErrorAlert("Error.",
          "Unable to submit interpreter request.");
    }
  }

  /**
   * Parse input the user has inputted for the sanitation request.
   *
   * @return If valid input, A SanitationRequest representing the users input. Otherwise null.
   */
  private InterpreterRequest parseUserInterpreterRequest() {
    // if input is valid, parse it and return a new SanitationRequest
    if (!nametxt.getText().isEmpty()
        && Objects.nonNull(locationbox.getNodeValue())
        && Objects.nonNull(categorybox.getValue())) {

      LocalDateTime now = LocalDateTime.now();
      Node node = locationbox.getNodeValue();

      InterpreterRequest.Language language = categorybox.getValue();

      String description = descriptiontxt.getText();
      String name = nametxt.getText();

      return new InterpreterRequest(now, node, language, description, name);
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
    InterpreterPopupController create();
  }
}
