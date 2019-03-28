package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ModifyDataControl extends Pane {

  @FXML
  private TextField nodeIDField;

  @FXML
  private TextField xcoordField;

  @FXML
  private TextField ycoordField;

  @FXML
  private TextField floorField;

  @FXML
  private TextField buildingField;

  @FXML
  private ComboBox<NodeType> nodeTypeField;

  @FXML
  private TextField longNameField;

  @FXML
  private TextField shortNameField;

  @FXML
  void initialize() {
    nodeTypeField.getItems().setAll(NodeType.values());
  }

  /**
   * Create a new instance of a Modify Data Control.
   */
  public ModifyDataControl() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ModifyData.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    fxmlLoader.load();
  }

  // Todo this needs to check if valid type is entered
  Node getNode() {

    return new Node(nodeIDField.getText(),
        Integer.parseInt(xcoordField.getText()),
        Integer.parseInt(ycoordField.getText()),
        Integer.parseInt(floorField.getText()),
        buildingField.getText(),
        nodeTypeField.getSelectionModel().getSelectedItem().name(),
        longNameField.getText(),
        shortNameField.getText());
  }

  void setNode(final Node node) {
    nodeIDField.setText(node.nodeID);
    xcoordField.setText(Integer.toString(node.xcoord));
    ycoordField.setText(Integer.toString(node.ycoord));
    floorField.setText(Integer.toString(node.floor));
    buildingField.setText(node.building);
    nodeTypeField.getSelectionModel().select(NodeType.get(node.nodeType));
    longNameField.setText(node.longName);
    shortNameField.setText(node.shortName);
  }
}
