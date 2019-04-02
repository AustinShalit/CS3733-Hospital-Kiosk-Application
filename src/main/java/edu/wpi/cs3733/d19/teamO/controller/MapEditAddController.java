package edu.wpi.cs3733.d19.teamO.controller;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class MapEditAddController extends MapEditController {
  private Database database = new Database();
  @FXML
  private Label addLabel;
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
  private ComboBox<Node.NodeType> nodeType;

  public MapEditAddController() throws SQLException {
  }

  @FXML
  private void comboBox() {
    nodeType.getItems().addAll(Node.NodeType.values());
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
    addLabel.setText("Succeed!");
  }






}