package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import edu.wpi.cs3733.d19.teamO.controller.v2.MapEditController;
import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class MapEditConnectController extends MapEditController {
  Database database = new Database();
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

    String udNodeID1 = connectNodeID1.getText();
    Optional<Node> nodeFromDB1 = database.getNode(udNodeID1);
    String udNodeID2 = connectNodeID1.getText();
    Optional<Node> nodeFromDB2 = database.getNode(udNodeID2);
    if (!nodeFromDB1.isPresent() || !nodeFromDB2.isPresent()) {
      status.setText("ERROR: InvalidNodeID");
    } else {
      Node node1 = nodeFromDB1.get();
      Node node2 = nodeFromDB2.get();
      Edge newEdge = new Edge(edgeID.getText(), node1, node2);
      database.insertEdge(newEdge);
      status.setText("Succeed!");
    }

  }
}
