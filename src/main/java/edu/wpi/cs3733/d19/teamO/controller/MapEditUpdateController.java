package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import edu.wpi.cs3733.d19.teamO.entity.Node;

public class MapEditUpdateController extends MapEditController {
  @FXML
  private TextField updateNodeID;
  @FXML
  private TextField updateX;
  @FXML
  private TextField updateY;
  @FXML
  private Label status;
  @FXML
  private Button updateButton;

  public MapEditUpdateController() throws SQLException {
  }

  /**
   * check if all field are filled.
   */
  @FXML
  public void validateButton() {
    if (updateNodeID.getText().isEmpty() || updateX.getText().isEmpty()
        || updateY.getText().isEmpty()) {
      updateButton.setDisable(true);
    } else {
      updateButton.setDisable(false);
    }
  }

  @FXML
  void updateNodeAction() {
    String udNodeID = updateNodeID.getText();
    Node updateNode = null;
    for (Node node : database.getAllNodes()) {
      if (udNodeID.equals(node.getNodeId())) {
        updateNode = new Node(node.getNodeId(),
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
    if (updateNode == null) {
      status.setText("ERROR: InvalidNodeID");
    } else {
      status.setText("Succeed!");
    }

  }
}
