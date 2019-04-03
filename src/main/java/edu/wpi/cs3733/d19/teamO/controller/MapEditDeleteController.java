package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class MapEditDeleteController extends MapEditController {
  private final Database database = new Database();
  @FXML
  private Label deleteError;
  @FXML
  private TextField deleteNodeID;

  public MapEditDeleteController() throws SQLException {
  }


  @FXML
  void deleteNodeAcion(ActionEvent event) {
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
      deleteError.setText("ERROR: InvalidNodeID");
    }
  }

}
