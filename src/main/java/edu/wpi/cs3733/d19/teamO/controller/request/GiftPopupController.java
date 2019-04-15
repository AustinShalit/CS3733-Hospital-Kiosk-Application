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
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.entity.GiftRequest;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "GiftPopup.fxml")
public class GiftPopupController implements Controller {
  private static final Logger logger = Logger.getLogger(GiftPopupController.class.getName());

  @FXML
  BorderPane root;
  @FXML
  private JFXTextField nametxt;
  @FXML
  private JFXComboBox<Node> locationbox;
  @FXML
  private JFXComboBox<GiftRequest.GiftRequestType> categorybox;
  @FXML
  private JFXTextArea descriptiontxt;
  @FXML
  private JFXButton submitbtn;
  @FXML
  private JFXButton backbtn;
  @FXML
  private Label reportLabel;

  @Inject
  private Database db;

  @FXML
  void initialize() {
    DialogHelper.populateComboBox(db, locationbox);
    categorybox.getItems().setAll(GiftRequest.GiftRequestType
        .values());

    submitbtn.disableProperty().bind(
        Bindings.isEmpty(nametxt.textProperty())
            .or(Bindings.isNull(locationbox.valueProperty()))
            .or(Bindings.isNull(categorybox.valueProperty()))
    );
  }

  @FXML
  void onSubmitButtonAction() {
    GiftRequest gift = parseUserGiftRequest();
    if (gift == null) {
      logger.log(Level.WARNING,
          "Unable to parse gift request.",
          "Unable to parse gift request.");
      return;
    }

    nametxt.setText(null);
    locationbox.getSelectionModel().clearSelection();
    locationbox.setValue(null);
    categorybox.getSelectionModel().clearSelection();
    categorybox.setValue(null);
    descriptiontxt.setText(null);

    if (db.insertGiftRequest(gift)) {
      String message = "Successfully submitted gift request.";
      DialogHelper.showInformationAlert("Success!", message);
    } else {
      DialogHelper.showErrorAlert("Error.",
          "Unable to submit gift request.");
    }
  }

  @FXML
  void onReportAction() {
    int teddybear = db.getNumberbyCategory("TEDDYBEAR").size();
    int candy = db.getNumberbyCategory("CANDY").size();
    int balloons = db.getNumberbyCategory("BALLOONS").size();
    int others = db.getNumberbyCategory("OTHERS").size();
    reportLabel.setText("The number of requests for gift of teddy: " + teddybear
        + "of candy: " + candy
        + "of balloons: " + balloons
        + "of other gifts: " + others
        + generateTimeReport());
  }

  /**
   * Parse input the user has inputted for the sanitation request.
   *
   * @return If valid input, A SanitationRequest representing the users input. Otherwise null.
   */
  private GiftRequest parseUserGiftRequest() {
    // if input is valid, parse it and return a new SanitationRequest
    if (!nametxt.getText().isEmpty()
        && Objects.nonNull(locationbox.getValue())
        && Objects.nonNull(categorybox.getValue())) {

      LocalDateTime now = LocalDateTime.now();
      Node node = (Node) locationbox.getValue();

      String type = categorybox.getValue().toString().toUpperCase(new Locale("EN"));
      GiftRequest.GiftRequestType giftRequestType =
          GiftRequest.GiftRequestType.valueOf(type);

      String description = descriptiontxt.getText();
      String name = nametxt.getText();

      return new GiftRequest(now, node, giftRequestType, description, name);
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
    for (GiftRequest giftRequest : db.getAllGiftRequests()) {
      cnt = giftRequest.getTimeDifference();
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
    GiftPopupController create();
  }
}
