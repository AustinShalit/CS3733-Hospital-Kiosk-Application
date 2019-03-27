package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
  private TextField nodeTypeField;

  @FXML
  private TextField longNameField;

  @FXML
  private TextField shortNameField;

  /**
   * Create a new instance of a Modify Data Control.
   */
  public ModifyDataControl() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
        "ModifyData.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    fxmlLoader.load();
  }

  Node getNode() {
    return null;
  }

  void setNode(final Node node) {
    nodeIDField.setText(node.nodeID);
    xcoordField.setText(Integer.toString(node.xcoord));
    ycoordField.setText(Integer.toString(node.ycoord));
    floorField.setText(Integer.toString(node.floor));
    buildingField.setText(node.building);
    nodeTypeField.setText(node.nodeType.name());
    longNameField.setText(node.longName);
    shortNameField.setText(node.shortName);
  }
}
