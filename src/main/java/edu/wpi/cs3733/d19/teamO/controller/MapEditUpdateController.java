package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import edu.wpi.cs3733.d19.teamO.controller.v2.MapEditController;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class MapEditUpdateController extends MapEditController {
  Database database = new Database();
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
    Optional<Node> nodeFromDB = database.getNode(udNodeID);
    if (!nodeFromDB.isPresent()) {
      status.setText("ERROR: InvalidNodeID");
    } else {
      Node node = nodeFromDB.get();
      Node updateNode = new Node(node.getNodeId(),
          Integer.parseInt(updateX.getText()),
          Integer.parseInt(updateY.getText()),
          node.getFloor(),
          node.getBuilding(),
          node.getNodeType(),
          node.getLongName(),
          node.getShortName());
      database.updateNode(updateNode);
      status.setText("Succeed!");
    }
  }
}
