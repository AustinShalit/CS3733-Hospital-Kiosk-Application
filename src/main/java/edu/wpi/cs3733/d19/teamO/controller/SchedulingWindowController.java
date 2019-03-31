package edu.wpi.cs3733.d19.teamO.controller;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.csv.NodeCsvReaderWriter;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
  void onBackButtonAction(ActionEvent event) {
    if (event.getSource() == backButton) {
      switchScenes("MainWindow.fxml", backButton.getScene().getWindow());
    }
  }

  @FXML
  void pickFile(ActionEvent e) throws IOException {
    if (e.getSource() == filePicker) {
      FileChooser chooser = new FileChooser();
      NodeCsvReaderWriter ncrw = new NodeCsvReaderWriter();
      chooser.setTitle("Choose CSV File");
      File file = chooser.showOpenDialog(new Stage());

      List<Node> nodes = ncrw.readNodes(file.toPath());
      System.out.println(nodes);
      for (Node node : nodes) {
        database.insertNode(node);
        roomComboBox.getItems().add(node.getLongName());
        System.out.println(node.getLongName());
      }
    }
  }

  @FXML
  public void initialize() throws SQLException {
        database = new Database();

  }
}
