package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;

public class MapEditConnectController extends MapEditController {
  @FXML
  private TextField connectNodeID1;
  @FXML
  private TextField connectNodeID2;
  @FXML
  private TextField edgeID;
  @FXML
  private Button connectButton;
  @FXML
  private Label status;

  public MapEditConnectController() throws SQLException {
  }

  /**
   * check if all field are filled.
   */
  @FXML
  public void validateButton() {
    if (connectNodeID1.getText().isEmpty() || connectNodeID2.getText().isEmpty()
        || edgeID.getText().isEmpty()) {
      connectButton.setDisable(true);
    } else {
      connectButton.setDisable(false);
    }
  }

  @FXML
  void connectNodeAction() {
    String connect1 = connectNodeID1.getText();
    String connect2 = connectNodeID2.getText();
    Node connectN1 = null;
    Node connectN2 = null;
    for (Node node : database.getAllNodes()) {
      if (connect1.equals(node.getNodeId())) {
        connectN1 = node;
      } else if (connect2.equals(node.getNodeId())) {
        connectN2 = node;
      }
    }
    if (connectN1 != null && connectN2 != null) {
      Edge newEdge = new Edge(edgeID.getText(), connectN1, connectN2);
      database.insertEdge(newEdge);
      status.setText("Succeed!");
    } else if (connectN1 == null || connectN2 == null) {
      status.setText("ERROR: InvalidNode");
    }
  }
}
