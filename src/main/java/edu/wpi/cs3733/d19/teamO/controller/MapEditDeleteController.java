package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import edu.wpi.cs3733.d19.teamO.entity.Node;

public class MapEditDeleteController extends MapEditController {
  @FXML
  private Label status;
  @FXML
  private TextField deleteNodeID;
  @FXML
  private Button deleteButton;

  public MapEditDeleteController() throws SQLException {
  }

  /**
   * check if all field are filled.
   */
  @FXML
  public void validateButton() {
    if (deleteNodeID.getText().isEmpty()) {
      deleteButton.setDisable(true);
    } else {
      deleteButton.setDisable(false);
    }
  }

  @FXML
  void deleteNodeAction() {
    String delNodeID = deleteNodeID.getText();
    Node deleteNode = null;
    for (Node node : database.getAllNodes()) {
      if (delNodeID.equals(node.getNodeId())) {
        deleteNode = node;
        database.deleteNode(deleteNode);
        break;
      }
    }
    if (deleteNode == null) {
      status.setText("ERROR: InvalidNodeID");
    } else {
      status.setText("Succeed!");
    }
  }

}
