package edu.wpi.cs3733.d19.teamO.controller.v2.request;

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
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.v2.Controller;
import edu.wpi.cs3733.d19.teamO.controller.v2.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.v2.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.v2.RequestController;
import edu.wpi.cs3733.d19.teamO.controller.v2.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.GiftRequest;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "Gift.fxml")
public class GiftController implements Controller {
  private static final Logger logger = Logger.getLogger(GiftController.class.getName());

  @FXML
  private BorderPane root;
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
  private EventBus eventBus;
  @Inject
  private Database db;
  @Inject
  private RequestController.Factory requestControllerFactory;

  @FXML
  void initialize() {
    DialogHelper.populateComboBox(db, locationbox);
    categorybox.getItems().setAll(GiftRequest.GiftRequestType
        .values());
  }

  @FXML
  void onbackButtonAction() {
    eventBus.post(new ChangeMainViewEvent(requestControllerFactory.create()));
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

    if (db.insertGiftRequest(gift)) {
      String message = "Successfully submitted gift request.";
      DialogHelper.showInformationAlert("Success!", message);
    } else {
      DialogHelper.showErrorAlert("Error.",
          "Unable to submit gift request.");
    }
  }

  @FXML
  void onReportAction(){
    int tSize = db.getNumberbyCategory("TEDDYBEAR").size();
    int cSize = db.getNumberbyCategory("CANDY").size();
    int bSize = db.getNumberbyCategory("BALLOONS").size();
    int oSize = db.getNumberbyCategory("OTHERS").size();
    reportLabel.setText("The number of requests for gift of teddy: " + tSize
        + "of candy: " + cSize
        + "of balloons: " + bSize
        + "of other gifts: " + oSize
        + generateTimeReport());
  }

  /**
   * Parse input the user has inputted for the sanitation request.
   *
   * @return If valid input, A SanitationRequest representing the users input. Otherwise null.
   */
  private GiftRequest parseUserGiftRequest() {
    // if input is valid, parse it and return a new SanitationRequest
    if (!descriptiontxt.getText().isEmpty()
        && !nametxt.getText().isEmpty()
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

  private String generateTimeReport(){
    int i = 0;
    int j = 0;
    int k = 0;
    int x = 0;
    int y = 0;
    int cnt = 0;
    for(GiftRequest gR: db.getAllGiftRequests()){
      cnt = gR.getTimeDifference();
      switch(cnt){
        case 1:
          i++;
          break;
        case 2:
          j++;
          break;
        case 3:
          k++;
          break;
        case 4:
          x++;
          break;
        default:
          y++;
          break;
      }
    }
    return ("Invalid time request: " + y
        + "Request over two hours" + i
        + "Request from an hour to two hours" + j
        + "Request from half hour to an hour" + k
        + "Request less than half hour" + x);
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    GiftController create();
  }
}
