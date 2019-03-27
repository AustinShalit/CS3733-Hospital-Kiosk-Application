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

  void setNode(final UINode uinode) {
    nodeIDField.setText(uinode.node.nodeID);
    xcoordField.setText(Integer.toString(uinode.node.xcoord));
    ycoordField.setText(Integer.toString(uinode.node.ycoord));
    floorField.setText(Integer.toString(uinode.node.floor));
    buildingField.setText(uinode.node.building);
    nodeTypeField.setText(uinode.node.nodeType.name);
    longNameField.setText(uinode.node.longName);
    shortNameField.setText(uinode.node.shortName);
  }
}
