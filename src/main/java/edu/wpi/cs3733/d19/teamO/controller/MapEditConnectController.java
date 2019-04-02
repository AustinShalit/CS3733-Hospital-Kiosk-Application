package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class MapEditConnectController extends MapEditController {
  private Database database = new Database();
  @FXML
  private TextField connectNodeID1;
  @FXML
  private TextField connectNodeID2;
  @FXML
  private TextField edgeID;

  public MapEditConnectController() throws SQLException {
  }

  @FXML
  void connectNodeAcion(ActionEvent event) {
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
    }
  }
}