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

public class MapEditDeleteController extends MapEditController {
  Database database = new Database();
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
    Optional<Node> opt = database.getNode(delNodeID);
    if (!opt.isPresent()) {
      status.setText("ERROR: InvalidNodeID");
    } else {
      Node deleteNode = opt.get();
      database.deleteNode(deleteNode);
      status.setText("Succeed!");
    }
  }

}
