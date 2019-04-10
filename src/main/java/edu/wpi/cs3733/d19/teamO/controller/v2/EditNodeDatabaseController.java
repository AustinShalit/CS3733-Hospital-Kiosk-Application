package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
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
    final DirectoryChooser directoryChooser =
        new DirectoryChooser();
    final File selectedDirectory =
        directoryChooser.showDialog(getRoot().getScene().getWindow());
    if (selectedDirectory != null) {

      String string = selectedDirectory.getAbsolutePath() + "nodes.csv";
      FileWriter fw = new FileWriter(string);
      BufferedWriter bw = new BufferedWriter(fw);
      NodeCsvReaderWriter nodeCsvReaderWriter = new NodeCsvReaderWriter();
      nodeCsvReaderWriter.writeNodes(bw, database.getAllNodes());

      string = selectedDirectory.getAbsolutePath() + "edges.csv";
      fw = new FileWriter(string);
      bw = new BufferedWriter(fw);
      EdgeCsvReaderWriter edgeCsvReaderWriter = new EdgeCsvReaderWriter(database);
      edgeCsvReaderWriter.writeNodes(bw, database.getAllEdges());

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
