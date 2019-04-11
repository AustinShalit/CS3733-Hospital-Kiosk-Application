package edu.wpi.cs3733.d19.teamO.controller.request;

import java.time.LocalDateTime;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import animatefx.animation.Shake;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.CheckRequestsController;
import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.InterpreterRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "InterpreterView.fxml")
@SuppressWarnings("PMD.TooManyFields")
public class InterpreterViewController implements Controller {

  @FXML
  private BorderPane root;
  @FXML
  private JFXButton goBackButton;
  @FXML
  private JFXButton assignButton;
  @FXML
  private JFXButton completedButton;
  @FXML
  private Label titleLabel;
  @FXML
  private TableView<InterpreterRequest> requestsTableView;
  @FXML
  private TableColumn<InterpreterRequest, Integer> idTableCol;
  @FXML
  private TableColumn<InterpreterRequest, LocalDateTime> timeRequestedCol;
  @FXML
  private TableColumn<InterpreterRequest, LocalDateTime> timeCompletedCol;
  @FXML
  private TableColumn<InterpreterRequest, String> whoCompletedCol;
  @FXML
  private TableColumn<InterpreterRequest, String> locationTableCol;
  @FXML
  private TableColumn<InterpreterRequest,
      InterpreterRequest.Language> categoryTableCol;
  @FXML
  private TableColumn<InterpreterRequest, String> descriptionCol;

  @Inject
  private EventBus eventBus;
  @Inject
  private Database db;
  @Inject
  private CheckRequestsController.Factory checkRequestsControllerFactory;

  @FXML
  void initialize() {
    idTableCol.setCellValueFactory(new PropertyValueFactory<>("sr_id"));
    timeRequestedCol.setCellValueFactory(new PropertyValueFactory<>("timeRequested"));
    //timeCompletedCol.setCellValueFactory(new PropertyValueFactory<>("timeCompletedString"));
    whoCompletedCol.setCellValueFactory(new PropertyValueFactory<>("whoCompleted"));
    locationTableCol.setCellValueFactory(new PropertyValueFactory<>("locationNodeIdString"));
    categoryTableCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

    requestsTableView.getItems().setAll(db.getAllInterpreterRequests());

    // sort by id
    requestsTableView.getSortOrder().add(idTableCol);

    // make columns auto-resize
    requestsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    for (TableColumn column : requestsTableView.getColumns()) {
      column.setPrefWidth(1000); // must be a value larger than the starting window size
    }
  }

  @FXML
  void goBackButtonAction() {
    eventBus.post(new ChangeMainViewEvent(checkRequestsControllerFactory.create()));
  }

  @FXML
  void onAssignButtonAction() {
    String id = DialogHelper.textInputDialog("Enter Interpreter Request ID",
        "Enter Request ID", "ID: ");
    int idInt = Integer.parseInt(id);
    String name = DialogHelper.textInputDialog("Enter Employee Name",
        "Enter Employee Name", "Employee Name: ");

    InterpreterRequest sr = db.getInterpreterRequest(idInt).get();
    sr.setWhoCompleted(name);
    db.updateInterpreterRequest(sr);

    requestsTableView.getItems().setAll(db.getAllInterpreterRequests());
    requestsTableView.getSortOrder().add(idTableCol); //sort
  }

  @FXML
  void onCompletedButtonAction() {
    if (requestsTableView.getSelectionModel().isEmpty()) {
      new Shake(completedButton).play();
      return;
    }

    InterpreterRequest selectedItem =
        requestsTableView.getSelectionModel().getSelectedItem();
    requestsTableView.getItems().remove(selectedItem);

    db.deleteInterpreterRequest(selectedItem);
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    InterpreterViewController create();
  }
}
