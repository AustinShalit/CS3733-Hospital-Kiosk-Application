package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SchedulingRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

import static edu.wpi.cs3733.d19.teamO.controller.v2.DialogHelper.showErrorAlert;
import static edu.wpi.cs3733.d19.teamO.controller.v2.DialogHelper.showInformationAlert;

@SuppressWarnings("PMD.TooManyFields")
@FxmlController(url = "Scheduling.fxml")
public class SchedulingController implements Controller {

  @FXML
  BorderPane root;
  @FXML
  JFXButton conferenceButton;
  @FXML
  JFXButton labButton;
  @FXML
  JFXButton francis15Button;
  @FXML
  JFXButton francis45Button;
  @FXML
  JFXButton btmButton;
  @FXML
  JFXButton shapiroButton;
  @FXML
  JFXButton towerButton;
  @FXML
  AnchorPane tableView;
  @FXML
  JFXTextField nameField;
  @FXML
  JFXComboBox<Node> roomComboBox;
  @FXML
  JFXDatePicker date;
  @FXML
  JFXTimePicker startTime;
  @FXML
  JFXTimePicker endTime;
  @FXML
  JFXButton goButton;
  @FXML
  TabPane tabPane;
  @FXML
  Tab mapTab;
  @FXML
  Tab tableTab;

  @Inject
  private Database database;

  @FXML
  void initialize() {
    DialogHelper.populateComboBox(database, roomComboBox);

    // set tab pane to span entire width
    tabPane.widthProperty().addListener((observable, oldValue, newValue) -> {
      tabPane.setTabMinWidth(tabPane.getWidth() / 2);
      tabPane.setTabMaxWidth(tabPane.getWidth() / 2);
    });
  }

  public void showCurrentSchedule() throws IOException {
    AnchorPane content = FXMLLoader.load(getClass().getResource("SchedulingViewWindow.fxml"));
    tableTab.setContent(content);
  }

  /**
   * Check to make sure Scheduling Request is valid.
   */
  @FXML
  void onSubmitButtonAction() {
    SchedulingRequest request = parseUserSchedulingRequestTest();
    if (Objects.isNull(request)) {
      return;
    }

    if (database.schedulingRequestWouldConflict(request)) {
      showErrorAlert("Error.", "Please choose different times.");
      return;
    }

    if (database.insertSchedulingrequest(request)) {
      String message = "Successfully submitted scheduling request.";
      showInformationAlert("Success!", message);
    } else {
      showErrorAlert("Error.", "Unable to submit scheduling request.");
    }
  }

  /**
   * Parse input the user has inputted for the scheduling request.
   *
   * @return A SchedulingRequest representing the users input. If invalid input, null.
   */
  private SchedulingRequest parseUserSchedulingRequestTest() {
    // if input is valid, parse it and return a new SanitationRequest
    if (Objects.nonNull(startTime.getValue())
        && Objects.nonNull(endTime.getValue())
        && Objects.nonNull(date.getValue())
        && (!(nameField.getText().isEmpty()))
        && (!(roomComboBox.getValue().getNodeId().isEmpty()))) {

      LocalDateTime start = LocalDateTime.of(date.getValue(), startTime.getValue());
      LocalDateTime end = LocalDateTime.of(date.getValue(), endTime.getValue());
      LocalDateTime now = LocalDateTime.now();
      String name = nameField.getText();
      Node roomNode = roomComboBox.getValue();

      return new SchedulingRequest(start, end, now, now, name, roomNode);
    }

    // otherwise, some input was invalid
    showErrorAlert("Error.", "Please make sure all fields are filled out.");
    return null;
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    SchedulingController create();
  }
}
