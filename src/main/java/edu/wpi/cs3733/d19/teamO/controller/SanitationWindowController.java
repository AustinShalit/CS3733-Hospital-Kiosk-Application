package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class SanitationWindowController extends Controller {

  @FXML
  private Button backButton;
  @FXML
  private Button submitButton;
  @FXML
  private Label titleLabel;
  @FXML
  private TextArea descriptionTextArea;
  @FXML
  private ComboBox locationComboBox;
  @FXML
  private ComboBox<SanitationRequest.SanitationRequestType> categoryComboBox;

  Database db;

  @FXML
  void initialize() throws SQLException {
    categoryComboBox.getItems().setAll(SanitationRequest.SanitationRequestType.values());
    db = new Database();
  }


  @FXML
  void onBackButtonAction() {
    switchScenes("MainSanitationWindow.fxml", backButton.getScene().getWindow());
  }


  @FXML
  void onSubmitButtonAction() {
    SanitationRequest sr = parseUserSanitationRequest();
    db.insertSanitationRequest(sr);
  }

  /**
   * Todo this method should parse data from user input. Right now it is using test data
   * @return
   */
  private SanitationRequest parseUserSanitationRequest() {
    Node testNode = new Node("123", 0, 1, "3", "Fuller",
        Node.NodeType.DEPT, "yolo", "swagger");
    db.insertNode(testNode);

    SanitationRequest testSR = new SanitationRequest(12345, LocalDateTime.now(),
        LocalDateTime.now(), testNode, "Jill",
        SanitationRequest.SanitationRequestType.BEDDING,
        "These sheets is messed up");

    return testSR;
  }
}
