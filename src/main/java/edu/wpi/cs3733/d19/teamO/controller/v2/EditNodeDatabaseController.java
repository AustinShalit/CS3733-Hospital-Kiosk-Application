package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;

import edu.wpi.cs3733.d19.teamO.entity.csv.EdgeCsvReaderWriter;
import edu.wpi.cs3733.d19.teamO.entity.csv.NodeCsvReaderWriter;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "EditNodeDatabase.fxml")
public class EditNodeDatabaseController implements Controller {

  @FXML
  GridPane root;

  @FXML
  private JFXButton exportButton;

  @Inject
  private Database database;

  @FXML
  void initialize() {

  }

  @FXML
  public void onExportButtonAction() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
    final DirectoryChooser directoryChooser = new DirectoryChooser();
    final File selectedDirectory = directoryChooser.showDialog(getRoot().getScene().getWindow());
    final File nodeFile = new File(selectedDirectory, "nodes.csv");
    final File edgesFile = new File(selectedDirectory, "edges.csv");

    if (selectedDirectory != null) {
      FileWriter fw = new FileWriter(nodeFile);
      NodeCsvReaderWriter nodeCsvReaderWriter = new NodeCsvReaderWriter();
      nodeCsvReaderWriter.writeNodes(fw, database.getAllNodes());

      fw = new FileWriter(edgesFile);
      EdgeCsvReaderWriter edgeCsvReaderWriter = new EdgeCsvReaderWriter(database);
      edgeCsvReaderWriter.writeNodes(fw, database.getAllEdges());

    }
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    EditNodeDatabaseController create();
  }
}
