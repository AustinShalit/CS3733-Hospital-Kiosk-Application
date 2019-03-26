package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class DisplayDatabaseController extends DatabaseController {

  @FXML
  private TableView table;

  @FXML
  private Button addDataButton;

  @FXML
  private Button downloadButton;

  @FXML
  void addDataButtonAction(ActionEvent event) throws IOException {
    if (event.getSource() == addDataButton) {
      Stage stage = (Stage) addDataButton.getScene().getWindow();
      Parent root = FXMLLoader.load(getClass().getResource("addData.fxml"));

      popupWindow(stage, root);
    }
  }

  @FXML
  void downloadButtonAction(ActionEvent event) {
    if (event.getSource() == downloadButton) {
      downloadButton.setText("Button not configured yet");
    }
  }
}
