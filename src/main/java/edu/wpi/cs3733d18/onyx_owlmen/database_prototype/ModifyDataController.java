package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.xml.soap.Text;

public class ModifyDataController {

  Node nodeToEdit;

  public ModifyDataController (Node nodeToEdit) {
    this.nodeToEdit = nodeToEdit;
  }

  @FXML
  private Button saveButton;

  @FXML
  private Button cancelButton;

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


  @FXML
  void initialize() {
    nodeIDField.setText(nodeToEdit.nodeID);
    xcoordField.setText(Integer.toString(nodeToEdit.xcoord));
    ycoordField.setText(Integer.toString(nodeToEdit.ycoord));
    floorField.setText(Integer.toString(nodeToEdit.floor));
    buildingField.setText(nodeToEdit.building);
    nodeTypeField.setText(nodeToEdit.nodeType.name());
    longNameField.setText(nodeToEdit.longName);
    shortNameField.setText(nodeToEdit.shortName);
  }

  @FXML
  void saveButtonAction(ActionEvent event) {
    if (event.getSource() == saveButton) {
      saveButton.setText("Button not configured yet");
    }
  }

  @FXML
  void cancelButtonAction(ActionEvent event) {
    if (event.getSource() == cancelButton) {
      Stage stage = (Stage) cancelButton.getScene().getWindow();
      stage.close();
    }
  }
}
