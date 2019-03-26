package edu.wpi.cs3733d18.onyx_owlmen.databasePrototype;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class DisplayDatabaseController extends DatabaseController {

    @FXML
    private TableView table;

    @FXML
    private Button addDataButton;

    @FXML
    private Button downloadButton;

    @FXML
    private void addDataButtonAction(ActionEvent event) throws IOException {
        if (event.getSource() == addDataButton) {
            Stage stage = (Stage) addDataButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("addData.fxml"));

            popupWindow(stage, root);
        }
    }

    @FXML
    private void downloadButtonAction(ActionEvent event) {
        if (event.getSource() == downloadButton) {
            downloadButton.setText("Button not configured yet");
        }
    }
}
