package edu.wpi.cs3733d18.onyx_owlmen.databasePrototype;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AddDataController extends DatabaseController {

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void saveButtonAction(ActionEvent event) {
        if (event.getSource() == saveButton) {
            saveButton.setText("Button not configured yet");
        }
    }

    @FXML
    private void cancelButtonAction(ActionEvent event) {
        if (event.getSource() == cancelButton){
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }
    }
}
