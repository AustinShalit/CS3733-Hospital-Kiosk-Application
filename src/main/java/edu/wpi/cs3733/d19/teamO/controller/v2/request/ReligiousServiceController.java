package edu.wpi.cs3733.d19.teamO.controller.v2.request;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.v2.Controller;
import edu.wpi.cs3733.d19.teamO.controller.v2.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.v2.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.v2.RequestController;
import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.ReligiousServiceRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "ReligiousService.fxml")
public class ReligiousServiceController implements Controller {

  @FXML
  private BorderPane root;
  @FXML
  private JFXTextField nametxt;
  @FXML
  private JFXComboBox<Node> locationbox;
  @FXML
  private JFXComboBox<ReligiousServiceRequest.ReligiousServiceRequestType> categorybox;
  @FXML
  private JFXTextArea descriptiontxt;
  @FXML
  private JFXButton submitbtn;
  @FXML
  private JFXButton backbtn;

  @Inject
  private EventBus eventBus;
  @Inject
  private Database db;
  @Inject
  private RequestController.Factory requestControllerFactory;

  @FXML
  void initialize() {
    DialogHelper.populateComboBox(db, locationbox);
    categorybox.getItems().setAll(ReligiousServiceRequest.ReligiousServiceRequestType
        .values());
  }

  @FXML
  void onbackButtonAction() {
    eventBus.post(new ChangeMainViewEvent(requestControllerFactory.create()));
  }

  @FXML
  void onSubmitButtonAction() {
    ReligiousServiceRequest religious = parseUserRSRequest();
    if (religious == null) {
      Logger logger = Logger.getLogger(ReligiousServiceController.class.getName());
      logger.log(Level.WARNING,
          "Unable to parse religious service Request.",
          "Unable to parse religious service Request.");
      return;
    }

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
    if (!descriptiontxt.getText().isEmpty()
        && !nametxt.getText().isEmpty()
        && Objects.nonNull(locationbox.getValue())
        && Objects.nonNull(categorybox.getValue())) {

      LocalDateTime now = LocalDateTime.now();
      Node node = (Node) locationbox.getValue();

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
    ReligiousServiceController create();
  }
}
