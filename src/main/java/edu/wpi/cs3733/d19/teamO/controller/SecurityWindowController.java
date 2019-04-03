package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;

import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SecurityRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@SuppressWarnings("PMD")
public class SecurityWindowController extends Controller {

  private Database database;

  @FXML
  private Button backButton;

  @FXML
  private Button alertbutton;

  @FXML
  private Button getnodesbutton;

  @FXML
  private Button viewSecurityRequestButton;
  private Button filePicker;

  @FXML
  private Text securityTitle;

  @FXML
  private JFXComboBox insertlocationdropdown;

  @FXML
  void chooseLocation(ActionEvent event) {

  }

  @FXML
  void initialize() {
    insertlocationdropdown.getItems().addAll(
        "Place 1",
        "Place 2",
        "Place 3",
        "Place 4"
    );
  }



  @FXML
  void initialize() throws SQLException {
    database = new Database();
    populateComboBox(database, insertlocationdropdown);
  }

  @FXML
  void onBackButtonAction(ActionEvent event) {
    if (event.getSource() == backButton) {
      switchScenes("MainWindow.fxml", backButton.getScene().getWindow());
    }
  }

  @FXML
  void onViewSecurityAction(ActionEvent event) {
    if (event.getSource() == viewSecurityRequestButton) {
      switchScenes("SecurityEmployeeOverviewWindow.fxml",
          viewSecurityRequestButton.getScene().getWindow());
    }
  }

  @FXML
  void onAlertAction(ActionEvent event) throws SQLException {
    if (event.getSource() == alertbutton) {
      Database db = new Database();
      Random r = new Random();
      int rand = r.nextInt();
      Node node1 = new Node(""+r, 1, 1, 2, "fuller",
          Node.NodeType.DEPT, "fuller commons","fc");
      db.insertNode(node1);
      db.insertSecurityRequest(new SecurityRequest(rand, LocalDateTime.now(), LocalDateTime.now(),
          "A", "idk", node1));
    }
  }


  void sendAlert(ActionEvent e) {
    if (e.getSource() == alertbutton) {
      Node node = (Node) insertlocationdropdown.getValue();
      database.insertSecurityRequest(
          new SecurityRequest(
              LocalDateTime.now(),
              SecurityRequest.defaultTime(),
              null,
              "",
              node
          ));
    }
  }

}
