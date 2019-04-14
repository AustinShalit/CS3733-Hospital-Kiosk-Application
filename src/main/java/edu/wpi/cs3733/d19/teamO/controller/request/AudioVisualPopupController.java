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
import edu.wpi.cs3733.d19.teamO.entity.AudioVisualRequest;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "AudioVisualPopup.fxml")
public class AudioVisualPopupController implements Controller {

  private static final Logger logger = Logger.getLogger(AudioVisualController.class.getName());

  @FXML
  BorderPane root;
  @FXML
  private JFXTextField nametxt;
  @FXML
  private JFXComboBox<Node> locationbox;
  @FXML
  private JFXComboBox<AudioVisualRequest.AudioVisualRequestType> categorybox;
  @FXML
  private JFXTextArea descriptiontxt;
  @FXML
  private JFXButton submitbtn;
  @FXML
  private JFXButton backbtn;

  @Inject
  private Database db;

  @FXML
  void initialize() {
    DialogHelper.populateComboBox(db, locationbox);
    categorybox.getItems().setAll(AudioVisualRequest.AudioVisualRequestType
        .values());

    submitbtn.disableProperty().bind(
        Bindings.isEmpty(nametxt.textProperty())
            .or(Bindings.isNull(locationbox.valueProperty()))
            .or(Bindings.isNull(categorybox.valueProperty()))
            .or(Bindings.isEmpty(descriptiontxt.textProperty()))
    );
  }

  @FXML
  void onSubmitButtonAction() {
    AudioVisualRequest audioVisual = parseUserAVRequest();
    if (audioVisual == null) {
      logger.log(Level.WARNING,
          "Unable to parse audio visual Request.",
          "Unable to parse audio visual Request.");
      return;
    }

    nametxt.setText(null);
    locationbox.getSelectionModel().clearSelection();
    locationbox.setValue(null);
    categorybox.getSelectionModel().clearSelection();
    categorybox.setValue(null);
    descriptiontxt.setText(null);

    if (db.insertAudioVisualRequest(audioVisual)) {
      String message = "Successfully submitted audio visual request.";
      DialogHelper.showInformationAlert("Success!", message);
    } else {
      DialogHelper.showErrorAlert("Error.",
          "Unable to submit audio visual request.");
    }
  }

  /**
   * Parse input the user has inputted for the sanitation request.
   *
   * @return If valid input, A SanitationRequest representing the users input. Otherwise null.
   */
  private AudioVisualRequest parseUserAVRequest() {
    // if input is valid, parse it and return a new SanitationRequest
    if (!descriptiontxt.getText().isEmpty()
        && !nametxt.getText().isEmpty()
        && Objects.nonNull(locationbox.getValue())
        && Objects.nonNull(categorybox.getValue())) {

      LocalDateTime now = LocalDateTime.now();
      Node node = (Node) locationbox.getValue();

      String type = categorybox.getValue().toString().toUpperCase(new Locale("EN"));
      AudioVisualRequest.AudioVisualRequestType audioVisualType =
          AudioVisualRequest.AudioVisualRequestType.valueOf(type);

      String description = descriptiontxt.getText();
      String name = nametxt.getText();

      return new AudioVisualRequest(now, node, audioVisualType, description, name);
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
    AudioVisualPopupController create();
  }
}
