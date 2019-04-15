package edu.wpi.cs3733.d19.teamO.controller;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import edu.wpi.cs3733.d19.teamO.component.SchedulingMapView;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SchedulingRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

import static edu.wpi.cs3733.d19.teamO.controller.DialogHelper.showErrorAlert;
import static edu.wpi.cs3733.d19.teamO.controller.DialogHelper.showInformationAlert;

@FxmlController(url = "Scheduling.fxml")
public class SchedulingController implements Controller {

  @FXML
  private BorderPane root;
  @FXML
  private JFXTextField nameField;
  @FXML
  private JFXComboBox<Node> roomComboBox;
  @FXML
  private JFXDatePicker date;
  @FXML
  private JFXTimePicker startTime;
  @FXML
  private JFXTimePicker endTime;
  @FXML
  private JFXTabPane tabPane;
  @FXML
  private Tab tableTab;
  @FXML
  private SchedulingMapView schedulingMapView;

  @Inject
  private Database database;

  @Inject
  private SchedulingViewController.Factory schedulingViewControllerFactory;

  @FXML
  void initialize() {
    // DialogHelper.populateComboBox(database, roomComboBox);
    Set<Node> bookableNodes = new HashSet<>();
    for (Node n : database.getAllNodes()) {
      if (n.getNodeType().isSchedulable()) {
        bookableNodes.add(n);
      }
    }
    roomComboBox.getItems().addAll(bookableNodes);
    Callback<ListView<Node>, ListCell<Node>> cellFactory =
        new Callback<ListView<Node>, ListCell<Node>>() {
          @Override
          public ListCell<Node> call(ListView<Node> param) {
            return new ListCell<Node>() {

              @Override
              protected void updateItem(Node item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                  setGraphic(null);
                } else {
                  setText(item.getLongName());
                }
              }
            };
          }
        };
    roomComboBox.setCellFactory(cellFactory);

    // wait for selection
    roomComboBox.valueProperty().addListener((observable, oldValue, newValue)
        -> roomComboBox.setButtonCell(cellFactory.call(null)));

    // set tab pane to span entire width
    tabPane.widthProperty().addListener((observable, oldValue, newValue) -> {
      tabPane.setTabMinWidth(newValue.doubleValue() / 2);
      tabPane.setTabMaxWidth(newValue.doubleValue() / 2);
    });

    date.valueProperty().addListener((observable, oldValue, newValue) -> updateMapViewNodeOverlay());
    startTime.valueProperty().addListener((observable, oldValue, newValue) -> updateMapViewNodeOverlay());
    endTime.valueProperty().addListener((observable, oldValue, newValue) -> updateMapViewNodeOverlay());
  }

  public void showCurrentSchedule() {
    tableTab.setContent(schedulingViewControllerFactory.create().getRoot());
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
      updateMapViewNodeOverlay();
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
        && startTime.getValue().isBefore(endTime.getValue())
        && Objects.nonNull(date.getValue())
        && (!(nameField.getText().isEmpty()))
        && Objects.nonNull(roomComboBox.getValue())
        && (!(roomComboBox.getValue().getNodeId().isEmpty()))) {

      LocalDateTime start = LocalDateTime.of(date.getValue(), startTime.getValue());
      LocalDateTime end = LocalDateTime.of(date.getValue(), endTime.getValue());
      LocalDateTime now = LocalDateTime.now();
      String name = nameField.getText();
      Node roomNode = roomComboBox.getValue();

      return new SchedulingRequest(start, end, now, now, name, roomNode);
    }

    if (startTime.getValue().isAfter(endTime.getValue())) {
      showErrorAlert("Error.",
          "Please make sure the end time is after the start time"
      );
      return null;
    }

    // otherwise, some input was invalid
    showErrorAlert("Error.", "Please make sure all fields are filled out.");
    return null;
  }

  private void updateMapViewNodeOverlay() {
    if (Objects.isNull(date.getValue())
        || Objects.isNull(startTime.getValue())
        || Objects.isNull(endTime.getValue())
        || endTime.getValue().isBefore(startTime.getValue())) {
      return;
    }

    LocalDateTime start = LocalDateTime.of(date.getValue(), startTime.getValue());
    LocalDateTime end = LocalDateTime.of(date.getValue(), endTime.getValue());
    // Set of available nodes
    Set<Node> availableNodes = database.getAllAvailableNodes(start, end);
    // Set of unavailable nodes
    Set<Node> unavailableNodes = new HashSet<>();
    for (Node n : database.getAllNodes()) {
      if (n.getNodeType().isSchedulable()) {
        unavailableNodes.add(n);
      }
    }
    for (Node avNode : availableNodes) {
      unavailableNodes.remove(avNode);
    }
    schedulingMapView.setAvailableNodes(availableNodes);
    schedulingMapView.setUnavailableNodes(unavailableNodes);
    schedulingMapView.redrawNodes();
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    SchedulingController create();
  }
}
