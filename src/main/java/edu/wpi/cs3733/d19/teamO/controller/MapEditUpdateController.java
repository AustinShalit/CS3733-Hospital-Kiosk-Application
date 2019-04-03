package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class MapEditUpdateController extends MapEditController {
  private final Database database = new Database();
  @FXML
  private TextField updateNodeID;
  @FXML
  private TextField updateX;
  @FXML
  private TextField updateY;

  public MapEditUpdateController() throws SQLException {
  }

  @FXML
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  void updateNodeAcion(ActionEvent event) {
    String udNodeID = updateNodeID.getText();
    for (Node node : database.getAllNodes()) {
      if (udNodeID.equals(node.getNodeId())) {

        Node updateNode = new Node(node.getNodeId(),
            Integer.parseInt(updateX.getText()),
            Integer.parseInt(updateY.getText()),
            node.getFloor(),
            node.getBuilding(),
            node.getNodeType(),
            node.getLongName(),
            node.getShortName());
        database.updateNode(updateNode);
        break;
      }
    }
  }
}
