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
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.ReligiousServiceRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "ReligiousServicePopup.fxml")
public class ReligiousServicePopupController implements Controller {

  @FXML
  BorderPane root;
  @FXML
  private JFXTextField nametxt;
  @FXML
  private FuzzyWuzzyComboBox locationbox;
  @FXML
  private JFXComboBox<ReligiousServiceRequest.ReligiousServiceRequestType> categorybox;
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
    categorybox.getItems().setAll(ReligiousServiceRequest.ReligiousServiceRequestType
        .values());

    submitbtn.disableProperty().bind(
        Bindings.isEmpty(nametxt.textProperty())
            .or(Bindings.isNull(locationbox.valueProperty()))
            .or(Bindings.isNull(categorybox.valueProperty()))
    );
  }

  @FXML
  void onSubmitButtonAction() {
    ReligiousServiceRequest religious = parseUserRSRequest();
    if (religious == null) {
      Logger logger = Logger.getLogger(ReligiousServicePopupController.class.getName());
      logger.log(Level.WARNING,
          "Unable to parse religious service Request.",
          "Unable to parse religious service Request.");
      return;
    }

    nametxt.setText(null);
    locationbox.getSelectionModel().clearSelection();
    locationbox.setValue(null);
    categorybox.getSelectionModel().clearSelection();
    categorybox.setValue(null);
    descriptiontxt.setText("");

    if (db.insertReligiousServiceRequest(religious)) {
      String message = "Successfully submitted religious service request.";
      DialogHelper.showInformationAlert("Success!", message);
    } else {
      DialogHelper.showErrorAlert("Error.",
          "Unable to submit religious service request.");
    }
  }

  /**
   * Parse input the user has inputted for the religious service request.
   *
   * @return If valid input, A ReligiousServiceRequest representing the users input. Otherwise null.
   */
  private ReligiousServiceRequest parseUserRSRequest() {
    // if input is valid, parse it and return a new ReligiousServiceRequest
    if (!nametxt.getText().isEmpty()
        && Objects.nonNull(locationbox.getNodeValue())
        && Objects.nonNull(categorybox.getValue())) {

      LocalDateTime now = LocalDateTime.now();
      Node node = locationbox.getNodeValue();

      ReligiousServiceRequest.ReligiousServiceRequestType religiousRequestType =
          categorybox.getValue();

      String description = descriptiontxt.getText();
      String name = nametxt.getText();

      return new ReligiousServiceRequest(now, node, religiousRequestType, description, name);
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
    ReligiousServicePopupController create();
  }
}
