package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import edu.wpi.cs3733.d19.teamO.entity.Node;
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
    timeCompletedCol.setCellValueFactory(new PropertyValueFactory<>("timeCompleted"));
    whoCompletedCol.setCellValueFactory(new PropertyValueFactory<>("whoCompleted"));

    // todo get just the node id
    locationTableCol.setCellValueFactory(new PropertyValueFactory<>("locationNode"));

    categoryTableCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));


    /**
     * todo
     * i am insertting test data using this method. we need to move the insert code to
     * SanitationWindowController, in onSubmitButtonAction().
     * We need to somehow pass the data between windows
     */
    insertTestData();
  }

  void insertTestData() {
    Node testNode = new Node("123", 0, 1, "3", "Fuller",
        Node.NodeType.DEPT, "yolo", "swagger");
    db.insertNode(testNode);

    SanitationRequest testSR = new SanitationRequest(12345, LocalDateTime.now(),
        LocalDateTime.now(), testNode, "Jill",
        SanitationRequest.SanitationRequestType.BEDDING,
        "These sheets is messed up");

    db.insertSanitationRequest(testSR);

    requestsTableView.getItems().setAll(db.getAllSanitationRequests());
  }

  @FXML
  void goBackButtonAction() {
    switchScenes("MainSanitationWindow.fxml", goBackButton.getScene().getWindow());
  }
}
