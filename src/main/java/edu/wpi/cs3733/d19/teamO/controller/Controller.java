package edu.wpi.cs3733.d19.teamO.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.csv.NodeCsvReaderWriter;

class Controller {

  final LocalDateTime defaultTime = LocalDateTime.of(1, 1, 1, 1, 1);

  /**
   * Try to switch scenes to the fxml file.
   *
   * @param fxmlFile   String representing the fxml file to switch scenes to
   * @param currWindow The current window
   */
  protected void switchScenes(String fxmlFile, Window currWindow) {
    Stage stage = (Stage) currWindow.getScene().getWindow();
    Parent root = null;
    try {
      root = FXMLLoader.load(getClass().getResource(fxmlFile));
    } catch (IOException exception) {
      exception.printStackTrace();
    }

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Get a list of nodes from a csv file. Used in testing.
   */
  @FXML
  List<Node> getNodesFromFile() throws IOException {
    FileChooser chooser = new FileChooser();
    NodeCsvReaderWriter ncrw = new NodeCsvReaderWriter();
    chooser.setTitle("Choose CSV File");
    File file = chooser.showOpenDialog(new Stage());

    return ncrw.readNodes(file.toPath());
  }
}
