package edu.wpi.cs3733.d19.teamO.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.csv.EdgeCsvReaderWriter;
import edu.wpi.cs3733.d19.teamO.entity.csv.NodeCsvReaderWriter;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class MainWindowController extends Controller {

  @FXML
  private Button navigationButton;
  @FXML
  private Button mapToolButton;
  @FXML
  private Button sanitationRequestButton;
  @FXML
  private Button securityRequestButton;
  @FXML
  private Button schedulingButton;
  @FXML
  private Button nodeImportButton;
  @FXML
  private Button edgeImportButton;
  @FXML
  private Label nodeImportInProgress;

  private Database database;

  @FXML
  void navigationButtonAction(ActionEvent event) {
    switchScenes("NavigationWindow.fxml", navigationButton.getScene().getWindow());
  }

  @FXML
  void mapToolButtonAction(ActionEvent event) {
    switchScenes("MapEditMain.fxml", mapToolButton.getScene().getWindow());
  }

  @FXML
  void sanitationRequestButtonAction(ActionEvent event) {
    switchScenes("MainSanitationWindow.fxml", sanitationRequestButton.getScene().getWindow());
  }

  @FXML
  void securityRequestButtonAction(ActionEvent event) {
    switchScenes("SecurityWindow.fxml", securityRequestButton.getScene().getWindow());
  }

  @FXML
  void schedulingButtonButtonAction(ActionEvent event) {
    switchScenes("SchedulingWindow.fxml", schedulingButton.getScene().getWindow());
  }

  @FXML
  void nodeImportButtonAction(ActionEvent e) throws IOException {
    // Create and configure file chooser
    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose Node CSV File");
    fileChooser.setInitialDirectory(
        new File(System.getProperty("user.home"))
    );
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("CSV", "*.csv")
    );

    NodeCsvReaderWriter ncrw = new NodeCsvReaderWriter();
    File file = fileChooser.showOpenDialog(new Stage());

    List<Node> nodes = ncrw.readNodes(Files.newBufferedReader(file.toPath()));

    for (Node node : nodes) {
      database.insertNode(node);
    }
    nodeImportInProgress.setText("Nodes Imported");

  }

  @FXML
  void edgeImportButtonAction(ActionEvent e) throws IOException {
    // Create and configure file chooser
    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose Edge CSV File");
    fileChooser.setInitialDirectory(
        new File(System.getProperty("user.home"))
    );
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("CSV", "*.csv")
    );

    EdgeCsvReaderWriter ecrw = new EdgeCsvReaderWriter(database);
    File file = fileChooser.showOpenDialog(new Stage());
    List<Edge> edges = ecrw.readEdges(Files.newBufferedReader(file.toPath()));

    for (Edge edge : edges) {
      database.insertEdge(edge);
    }
    nodeImportInProgress.setText("Edges Imported");
  }

  @FXML
  public void initialize() throws SQLException {
    database = new Database();
  }
}
