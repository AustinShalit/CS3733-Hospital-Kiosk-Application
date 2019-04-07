package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import edu.wpi.cs3733.d19.teamO.entity.SchedulingRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;


public class SchedulingViewWindowController extends Controller {

  @FXML
  private JFXButton goBackButton;
  @FXML
  private Label titleLabel;
  @FXML
  private TableView<SchedulingRequest> requestsTableView;
  @FXML
  private TableColumn<SchedulingRequest, Integer> idTableCol;
  @FXML
  private TableColumn<SchedulingRequest, LocalDateTime> startTimeCol;
  @FXML
  private TableColumn<SchedulingRequest, LocalDateTime> endTimeCol;
  @FXML
  private TableColumn<SchedulingRequest, LocalDateTime> timeRequestedCol;
  @FXML
  private TableColumn<SchedulingRequest, LocalDateTime> timeCompletedCol;
  @FXML
  private TableColumn<SchedulingRequest, String> whoRequestedCol;
  @FXML
  private TableColumn<SchedulingRequest, String> locationTableCol;


  @FXML
  void initialize() throws SQLException {
    final Database db = new Database();

    idTableCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
    endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    timeRequestedCol.setCellValueFactory(new PropertyValueFactory<>("timeRequested"));
    timeCompletedCol.setCellValueFactory(new PropertyValueFactory<>("timeCompleted"));
    whoRequestedCol.setCellValueFactory(new PropertyValueFactory<>("whoReserved"));
    locationTableCol.setCellValueFactory(new PropertyValueFactory<>("room"));


    requestsTableView.getItems().setAll(db.getAllSchedulingRequests());

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
    switchScenes("SchedulingWindow.fxml", goBackButton.getScene().getWindow());
  }
}
