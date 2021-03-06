package edu.wpi.cs3733.d19.teamO.controller.request;

import java.time.LocalDateTime;
import java.util.Locale;
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

import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.RequestController;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.InternalTransportationRequest;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "InternalTransportation.fxml")
public class InternalTransportationController implements Controller {

  private static final Logger logger = Logger.getLogger(
      InternalTransportationController.class.getName());

  @FXML
  private BorderPane root;
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

  @Inject
  private EventBus eventBus;
  @Inject
  private Database db;
  @Inject
  private RequestController.Factory requestControllerFactory;

  @FXML
  void initialize() {
    DialogHelper.populateComboBox(db, locationbox);
    categorybox.getItems().setAll(InternalTransportationRequest.InternalTransportationRequestType
        .values());
  }

  @FXML
  void onbackButtonAction() {
    eventBus.post(new ChangeMainViewEvent(requestControllerFactory.create()));
  }

  @FXML
  void onSubmitButtonAction() {
    InternalTransportationRequest internal = parseUserITRequest();
    if (internal == null) {
      logger.log(Level.WARNING,
          "Unable to parse internal transportation Request.",
          "Unable to parse Internal Transportation Request.");
      return;
    }

    if (db.insertInternalTransportationRequest(internal)) {
      String message = "Successfully submitted internal transportation request.";
      DialogHelper.showInformationAlert("Success!", message);
    } else {
      DialogHelper.showErrorAlert("Error.",
          "Unable to submit internal transportation request.");
    }
  }

  /**
   * Parse input the user has inputted for the sanitation request.
   *
   * @return If valid input, A InternalTransportationRequest representing the users input
   *         Otherwise null.
   */
  private InternalTransportationRequest parseUserITRequest() {
    // if input is valid, parse it and return a new InternalTransportationRequest
    if (!descriptiontxt.getText().isEmpty()
        && !nametxt.getText().isEmpty()
        && Objects.nonNull(locationbox.getValue())
        && Objects.nonNull(categorybox.getValue())) {

      LocalDateTime now = LocalDateTime.now();
      Node node = (Node) locationbox.getValue();

      String type = categorybox.getValue().toString().toUpperCase(new Locale("EN"));
      InternalTransportationRequest.InternalTransportationRequestType internalRequestType =
          InternalTransportationRequest.InternalTransportationRequestType.valueOf(type);

      String description = descriptiontxt.getText();
      String name = nametxt.getText();

      return new InternalTransportationRequest(now, node, internalRequestType, description, name);
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
    InternalTransportationController create();
  }
}
