package edu.wpi.cs3733.d19.teamO.controller;

import com.jfoenix.controls.JFXComboBox;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SecurityRequest;
import edu.wpi.cs3733.d19.teamO.entity.csv.NodeCsvReaderWriter;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SecurityWindowController extends Controller {

  private Database database;

  @FXML
  private Button backButton;

  @FXML
  private Button alertbutton;

  @FXML
  private Button filePicker;

  @FXML
  private Text securityTitle;

  @FXML
  private JFXComboBox insertlocationdropdown;

  @FXML
  void chooseLocation(ActionEvent event) {

  }

  @FXML
  void initialize() throws SQLException {
    database = new Database();
    insertlocationdropdown
        .getItems()
        .addAll(
            database
                .getAllNodes()
                .stream()
                .map(Node::getNodeId)
                .collect(Collectors.toList())
      );
  }


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

      List<Node> nodes = ncrw.readNodes(Files.newBufferedReader(file.toPath()));
      System.out.println(nodes);
      for (Node node : nodes) {
        database.insertNode(node);
        System.out.println(node.getLongName());
      }

    }
  }

  @FXML
  void sendAlert(ActionEvent e) {
    if (e.getSource() == alertbutton) {
      Node node = database.getNode(insertlocationdropdown.getValue().toString()).get();
      database.insertSecurityRequest(
          new SecurityRequest(
              LocalDateTime.now(),
              null,
              null,
              null,
              node
          ));
    }
  }


}
