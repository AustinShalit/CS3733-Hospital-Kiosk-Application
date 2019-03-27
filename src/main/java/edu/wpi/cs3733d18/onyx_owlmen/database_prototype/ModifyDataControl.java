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

  public ModifyDataControl() {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
        "ModifyData.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  void setNode(final Node node) {
    nodeIDField.setText(node.nodeID);
  }

  Node getNode() {
    return null;
  }
}
