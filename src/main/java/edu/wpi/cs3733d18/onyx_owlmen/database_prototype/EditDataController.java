package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import javafx.fxml.FXML;

public class EditDataController extends ModifyDataController {
  private Node nodeToEdit;

  public EditDataController(Node nodeToEdit) {
    this.nodeToEdit = nodeToEdit;
  }

  @FXML
  void initialize() {
    nodeIDField.setText(nodeToEdit.nodeID);


  }
}
