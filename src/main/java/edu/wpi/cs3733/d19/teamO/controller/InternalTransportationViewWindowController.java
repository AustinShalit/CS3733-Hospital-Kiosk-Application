package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import edu.wpi.cs3733.d19.teamO.entity.InternalTransportationRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class InternalTransportationViewWindowController extends Controller {

  @FXML
  private JFXButton goBackButton;
  @FXML
  private JFXButton assignButton;
  @FXML
  private JFXButton completedButton;
  @FXML
  private Label titleLabel;
  @FXML
  private TableView<InternalTransportationRequest> requestsTableView;
  @FXML
  private TableColumn<InternalTransportationRequest, Integer> idTableCol;
  @FXML
  private TableColumn<InternalTransportationRequest, LocalDateTime> timeRequestedCol;
  @FXML
  private TableColumn<InternalTransportationRequest, LocalDateTime> timeCompletedCol;
  @FXML
  private TableColumn<InternalTransportationRequest, String> whoCompletedCol;
  @FXML
  private TableColumn<InternalTransportationRequest, String> locationTableCol;
  @FXML
  private TableColumn<InternalTransportationRequest,
      InternalTransportationRequest.InternalTransportationRequestType> categoryTableCol;
  @FXML
  private TableColumn<InternalTransportationRequest, String> descriptionCol;
  @FXML
  private TableColumn<InternalTransportationRequest, String> nameCol;

  @FXML
  void initialize() throws SQLException {
    final Database db = new Database();

    idTableCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    timeRequestedCol.setCellValueFactory(new PropertyValueFactory<>("timeRequested"));
    //timeCompletedCol.setCellValueFactory(new PropertyValueFactory<>("timeCompletedString"));
    whoCompletedCol.setCellValueFactory(new PropertyValueFactory<>("whoCompleted"));
    locationTableCol.setCellValueFactory(new PropertyValueFactory<>("locationNodeIdString"));
    categoryTableCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    nameCol.setCellValueFactory(new PropertyValueFactory<>("person"));

    requestsTableView.getItems().setAll(db.getAllInternalTransportationRequests());

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
    switchScenes("MainWindow.fxml", goBackButton.getScene().getWindow());
  }

  @FXML
  void onAssignButtonAction() throws SQLException {
    String id = textInputDialog("Enter Security Request ID",
        "Enter Request ID", "ID: ");
    int idInt = Integer.parseInt(id);
    String name = textInputDialog("Enter Employee Name",
        "Enter Employee Name", "Employee Name: ");

    Database db = new Database();
    InternalTransportationRequest sr = db.getInternalTransportationRequest(idInt).get();
    sr.setWhoCompleted(name);
    db.updateInternalTransportationRequest(sr);

    requestsTableView.getItems().setAll(db.getAllInternalTransportationRequests());
    requestsTableView.getSortOrder().add(idTableCol); //sort
  }

  @FXML
  void onCompletedButtonAction() {
    InternalTransportationRequest selectedItem =
        requestsTableView.getSelectionModel().getSelectedItem();
    requestsTableView.getItems().remove(selectedItem);
  }
}
