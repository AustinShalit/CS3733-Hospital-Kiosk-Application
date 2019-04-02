package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;


public class SanitationViewWindowController extends Controller {

  @FXML
  private Button goBackButton;
  @FXML
  private Label titleLabel;
  @FXML
  private TableView<SanitationRequest> requestsTableView;
  @FXML
  private TableColumn<SanitationRequest, Integer> idTableCol;
  @FXML
  private TableColumn<SanitationRequest, LocalDateTime> timeRequestedCol;
  @FXML
  private TableColumn<SanitationRequest, LocalDateTime> timeCompletedCol;
  @FXML
  private TableColumn<SanitationRequest, String> whoCompletedCol;
  @FXML
  private TableColumn<SanitationRequest, String> locationTableCol;
  @FXML
  private TableColumn<SanitationRequest, SanitationRequest.SanitationRequestType> categoryTableCol;
  @FXML
  private TableColumn<SanitationRequest, String> descriptionCol;

  Database db;

  @FXML
  void initialize() throws SQLException {
    db = new Database();

    idTableCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    timeRequestedCol.setCellValueFactory(new PropertyValueFactory<>("timeRequested"));
    timeCompletedCol.setCellValueFactory(new PropertyValueFactory<>("timeCompletedString"));
    whoCompletedCol.setCellValueFactory(new PropertyValueFactory<>("whoCompleted"));
    locationTableCol.setCellValueFactory(new PropertyValueFactory<>("locationNodeString"));
    categoryTableCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

    requestsTableView.getItems().setAll(db.getAllSanitationRequests());
  }

  @FXML
  void goBackButtonAction() {
    switchScenes("MainSanitationWindow.fxml", goBackButton.getScene().getWindow());
  }
}
