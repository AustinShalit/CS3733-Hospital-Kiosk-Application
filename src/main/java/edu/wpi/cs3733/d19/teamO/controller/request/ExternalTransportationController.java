package edu.wpi.cs3733.d19.teamO.controller.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.RequestController;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.ExternalTransportationRequest;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "ExternalTransportation.fxml")
public class ExternalTransportationController implements Controller {

  private static final Logger logger =
      Logger.getLogger(ExternalTransportationController.class.getName());

  @FXML
  private BorderPane root;
  @FXML
  private JFXTextField nametxt;
  @FXML
  private JFXComboBox<Node> locationbox;
  @FXML
  private JFXComboBox<ExternalTransportationRequest.ExternalTransportationRequestType> categorybox;
  @FXML
  private JFXTextArea descriptiontxt;
  @FXML
  private JFXButton submitbtn;
  @FXML
  private JFXButton backbtn;
  @FXML
  private JFXButton btn;
  @FXML
  private Label reportLabel;
  @FXML
  private JFXDatePicker datePicker;
  @FXML
  private JFXTimePicker timePicker;

  @Inject
  private EventBus eventBus;
  @Inject
  private Database db;
  @Inject
  private RequestController.Factory requestControllerFactory;


  @FXML
  void initialize() {
    DialogHelper.populateComboBox(db, locationbox);
    categorybox.getItems().setAll(ExternalTransportationRequest.ExternalTransportationRequestType
        .values());
  }

  @FXML
  void onbackButtonAction() {
    eventBus.post(new ChangeMainViewEvent(requestControllerFactory.create()));
  }

  @FXML
  void onSubmitButtonAction() {
    ExternalTransportationRequest external = parseUserITRequest();
    if (external == null) {
      logger.log(Level.WARNING,
          "Unable to parse External transportation Request.",
          "Unable to parse External Transportation Request.");
      return;
    }

    if (db.insertExternalTransportationRequest(external)) {
      String message = "Successfully submitted External transportation request.";
      DialogHelper.showInformationAlert("Success!", message);
    } else {
      DialogHelper.showErrorAlert("Error.",
          "Unable to submit External transportation request.");
    }
  }

  @FXML
  void onReportAction() {
    int ambulance = db.getExternalTransportationRequestsByCategory("AMBULANCE").size();
    int helicopter = db.getExternalTransportationRequestsByCategory("HELICOPTER").size();
    int others = db.getExternalTransportationRequestsByCategory("OTHERS").size();
    reportLabel.setText("The number of requests for transporting by ambulance: " + ambulance
        + "by helicopter: " + helicopter
        + "by other transportation: " + others
        + generateTimeReport());
  }

  /**
   * Parse input the user has inputted for the sanitation request.
   *
   * @return If valid input, A SanitationRequest representing the users input. Otherwise null.
   */
  private ExternalTransportationRequest parseUserITRequest() {
    // if input is valid, parse it and return a new SanitationRequest
    if (!descriptiontxt.getText().isEmpty()
        && !nametxt.getText().isEmpty()
        && Objects.nonNull(locationbox.getValue())
        && Objects.nonNull(categorybox.getValue())) {

      LocalDateTime now = LocalDateTime.now();
      Node node = (Node) locationbox.getValue();

      String type = categorybox.getValue().toString().toUpperCase(new Locale("EN"));
      ExternalTransportationRequest.ExternalTransportationRequestType externalRequestType =
          ExternalTransportationRequest.ExternalTransportationRequestType.valueOf(type);

      String description = descriptiontxt.getText();
      String name = nametxt.getText();

      LocalTime time = timePicker.getValue();
      LocalDate date = datePicker.getValue();

      return new ExternalTransportationRequest(now, node, externalRequestType,
          description, name, date, time);
    }

    // otherwise, some input was invalid
    DialogHelper.showErrorAlert("Error.", "Please make sure all fields are filled out.");
    return null;
  }

  private String generateTimeReport() {
    int twoHourCounter = 0;
    int oneHourCounter = 0;
    int halfHourCounter = 0;
    int lessHalfHourCounter = 0;
    int invalidCounter = 0;
    int cnt = 0;
    for (ExternalTransportationRequest external : db.getAllExternalTransportationRequests()) {
      cnt = external.getTimeDifference();
      switch (cnt) {
        case 1:
          twoHourCounter++;
          break;
        case 2:
          oneHourCounter++;
          break;
        case 3:
          halfHourCounter++;
          break;
        case 4:
          lessHalfHourCounter++;
          break;
        default:
          invalidCounter++;
          break;
      }
    }
    return "Invalid time request: " + invalidCounter
        + "Request over two hours" + twoHourCounter
        + "Request from an hour to two hours" + oneHourCounter
        + "Request from half hour to an hour" + halfHourCounter
        + "Request less than half hour" + lessHalfHourCounter;
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    ExternalTransportationController create();
  }
}
