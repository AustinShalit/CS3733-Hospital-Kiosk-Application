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

import edu.wpi.cs3733.d19.teamO.component.FuzzyWuzzyComboBox;
import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.entity.ITSupportRequest;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "ITSupportPopup.fxml")
public class ITSupportPopupController implements Controller {

  private static final Logger logger = Logger.getLogger(ITSupportPopupController.class.getName());

  @FXML
  BorderPane root;
  @FXML
  private JFXTextField nametxt;
  @FXML
  private FuzzyWuzzyComboBox locationbox;
  @FXML
  private JFXComboBox<ITSupportRequest.ITSupportRequestType> categorybox;
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
    locationbox.setNodes(db.getAllNodes());
    locationbox.setupAutoRefresh();
    locationbox.refresh();
    categorybox.getItems().setAll(ITSupportRequest.ITSupportRequestType
        .values());

    submitbtn.disableProperty().bind(
        Bindings.isEmpty(nametxt.textProperty())
            .or(Bindings.isNull(locationbox.valueProperty()))
            .or(Bindings.isNull(categorybox.valueProperty()))
    );
  }

  @FXML
  void onSubmitButtonAction() {
    ITSupportRequest itSupport = parseUserITSupportRequest();
    if (itSupport == null) {
      logger.log(Level.WARNING,
          "Unable to parse IT Support Request.",
          "Unable to parse IT Support Request.");
      return;
    }

    nametxt.setText(null);
    locationbox.getSelectionModel().clearSelection();
    locationbox.setValue(null);
    categorybox.getSelectionModel().clearSelection();
    categorybox.setValue(null);
    descriptiontxt.setText("");

    if (db.insertITSupportRequest(itSupport)) {
      String message = "Successfully submitted IT Support request.";
      DialogHelper.showInformationAlert("Success!", message);
    } else {
      DialogHelper.showErrorAlert("Error.",
          "Unable to submit IT Support request.");
    }
  }

  /**
   * Parse input the user has inputted for the sanitation request.
   *
   * @return If valid input, A SanitationRequest representing the users input. Otherwise null.
   */
  private ITSupportRequest parseUserITSupportRequest() {
    // if input is valid, parse it and return a new SanitationRequest
    if (!nametxt.getText().isEmpty()
        && Objects.nonNull(locationbox.getNodeValue())
        && Objects.nonNull(categorybox.getValue())) {

      LocalDateTime now = LocalDateTime.now();
      Node node = locationbox.getNodeValue();

      String type = categorybox.getValue().toString().toUpperCase(new Locale("EN"));
      ITSupportRequest.ITSupportRequestType itSupportRequestType =
          ITSupportRequest.ITSupportRequestType.valueOf(type);

      String description = descriptiontxt.getText();
      String name = nametxt.getText();

      return new ITSupportRequest(now, node, itSupportRequestType, description, name);
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
    ITSupportPopupController create();
  }
}
