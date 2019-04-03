package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import edu.wpi.cs3733.d19.teamO.entity.Node;

public class MapEditAddController extends MapEditController {
  @FXML
  private Label status;
  @FXML
  private TextField addNodeID;
  @FXML
  private TextField addX;
  @FXML
  private TextField addY;
  @FXML
  private TextField addBuilding;
  @FXML
  private TextField addFloor;
  @FXML
  private TextField shortName;
  @FXML
  private TextField longName;
  @FXML
  private Button addButton;
  @FXML
  private ComboBox<Node.NodeType> nodeType;

  public MapEditAddController() throws SQLException {
  }

  @FXML
  private void comboBoxAction() { // NOPMD
    nodeType.getItems().addAll(Node.NodeType.values());
  }

  /**
   * check if all field are filled.
   */
  @FXML
  public void validateButton() {
    if (addNodeID.getText().isEmpty() || addX.getText().isEmpty() || addY.getText().isEmpty()
        || addFloor.getText().isEmpty() || addBuilding.getText().isEmpty()
        || nodeType.getValue() == null || longName.getText().isEmpty()
        || shortName.getText().isEmpty()) {
      addButton.setDisable(true);
    } else {
      addButton.setDisable(false);
    }
  }

  @FXML
  void addNodeAction(ActionEvent event) {
    Node newNode = new Node(addNodeID.getText(),
        Integer.parseInt(addX.getText()),
        Integer.parseInt(addY.getText()),
        addFloor.getText(),
        addBuilding.getText(),
        nodeType.getValue(),
        longName.getText(),
        shortName.getText());
    database.insertNode(newNode);
    status.setText("Succeed!");
  }


}
