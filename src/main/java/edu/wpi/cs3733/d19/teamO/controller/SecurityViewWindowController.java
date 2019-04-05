package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import edu.wpi.cs3733.d19.teamO.entity.SecurityRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class SecurityViewWindowController extends Controller {

  @FXML
  private Button goBackButton;
  @FXML
  private Button assignButton;
  @FXML
  private Button completedButton;
  @FXML
  private TableView<SecurityRequest> requestsTableView;
  @FXML
  private TableColumn<SecurityRequest, Integer> idTableCol;
  @FXML
  private TableColumn<SecurityRequest, LocalDateTime> timeRequestedCol;

  // todo right now this uses whoCompleted since there is no who_assigned_to in the database
  @FXML
  private TableColumn<SecurityRequest, String> whoAssignedToCol;

  @FXML
  void initialize() throws SQLException {
    final Database db = new Database();

    idTableCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    timeRequestedCol.setCellValueFactory(new PropertyValueFactory<>("timeRequested"));
    whoAssignedToCol.setCellValueFactory(new PropertyValueFactory<>("whoCompleted")); // todo

    requestsTableView.getItems().setAll(db.getAllSecurityRequests());

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
    switchScenes("SecurityWindow.fxml", goBackButton.getScene().getWindow());
  }

  @FXML
  void onAssignButtonAction() throws SQLException {
    String id = textInputDialog("Enter Security Request ID",
        "Enter Security Request ID", "ID: ");
    int idInt = Integer.parseInt(id);
    String name = textInputDialog("Enter Employee Name",
        "Enter Employee Name", "Employee Name: ");

    Database db = new Database();
    SecurityRequest sr = db.getSecurityRequest(idInt).get();
    sr.setWhoCompleted(name);
    db.updateSecurity(sr);

    requestsTableView.getItems().setAll(db.getAllSecurityRequests());
    requestsTableView.getSortOrder().add(idTableCol); // sort

  }

  @FXML
  void onCompletedButtonAction() throws SQLException {
    String id = textInputDialog("Enter Security Request ID",
        "Enter Security Request ID", "ID: ");
    int idInt = Integer.parseInt(id);

    SecurityRequest sr = new SecurityRequest(idInt);

    Database db = new Database();
    if (!db.deleteSecurityRequest(sr)) {
      showErrorAlert("Unable to find that SecurityRequest.",
          "Please check that the ID exists in the table.");

    }

    requestsTableView.getItems().setAll(db.getAllSecurityRequests());
    requestsTableView.getSortOrder().add(idTableCol); // sort
  }
}
