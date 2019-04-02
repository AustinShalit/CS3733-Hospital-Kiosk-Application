package edu.wpi.cs3733.d19.teamO.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;

import edu.wpi.cs3733.d19.teamO.entity.SchedulingRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.csv.NodeCsvReaderWriter;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class SchedulingWindowController extends Controller {

  private Database database;

  @FXML
  private Button backButton;
  @FXML
  private Button submitButton;
  @FXML
  private ComboBox<String> roomComboBox;
  @FXML
  private TextField nameBox;
  @FXML
  private JFXTimePicker startChoiceBox;
  @FXML
  private JFXTimePicker endChoiceBox;
  @FXML
  private JFXDatePicker datePicker;
  @FXML
  private Button filePicker;
  @FXML
  private Label submitStatus;


  @FXML
  void onBackButtonAction(ActionEvent event) {
    if (event.getSource() == backButton) {
      switchScenes("MainWindow.fxml", backButton.getScene().getWindow());
    }
  }

  /**
   * Check to make sure Scheduling Request is valid
   * @param e Action Event from Submit button
   */
  @FXML
  void onSubmitButtonAction(ActionEvent e) {
    if (e.getSource() == submitButton
        && nameBox != null
        && startChoiceBox.getValue() != null
        && endChoiceBox.getValue() != null
        && datePicker.getValue() != null
        && roomComboBox.getValue() != null) {
      submitStatus.setText("All fields are filled. Nice!");
    } else {
      submitStatus.setText("Make sure all fields are filled in.");
    }



  }

  /**
   * Picks a CSV file to read in location nodes from
   * @param e Action Event from File Picker button
   * @throws IOException
   */
  @FXML
  void pickFile(ActionEvent e) throws IOException {
    if (e.getSource() == filePicker) {
      FileChooser chooser = new FileChooser();
      NodeCsvReaderWriter ncrw = new NodeCsvReaderWriter();
      chooser.setTitle("Choose CSV File");
      File file = chooser.showOpenDialog(new Stage());

      List<Node> nodes = ncrw.readNodes(Files.newBufferedReader(file.toPath()));
      for (Node node : nodes) {
        database.insertNode(node);
        roomComboBox.getItems().add(node.getLongName());
      }
    }
  }

  @FXML
  public void initialize() throws SQLException {
    database = new Database();

  }
}
