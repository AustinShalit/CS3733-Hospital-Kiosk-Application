package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class EditDataController {

  @FXML
  private ModifyDataControl modifyData;

  @FXML
  private Button saveButton;

  @FXML
  private Button cancelButton;

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

  Node getNode() {
    return modifyData.getNode();
  }

  void setNode(final Node node) {
    modifyData.setNode(node);
  }
}
