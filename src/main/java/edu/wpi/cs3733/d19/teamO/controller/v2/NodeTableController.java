package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.csv.NodeCsvReaderWriter;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

import static java.nio.charset.StandardCharsets.UTF_8;

@FxmlController(url = "NodeTable.fxml")
public class NodeTableController implements Controller {

  private Database db;

  @FXML
  private BorderPane root;

  @FXML
  private Button downloadCsvBtn;

//  @FXML
//  void downloadCsvOnAction() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException,
//      FileNotFoundException, IOException {
//    NodeCsvReaderWriter ncrw = new NodeCsvReaderWriter();
//    Path path = Paths.get("src/main/resources/edu/wpi/cs3733/d19/teamO/csv");
//    List<Node> nodes = ncrw.writeNodes(Files.newBufferedWriter(path.resolve("nodesv4.csv");
//    System.out.println(path.resolve("export.csv"));
//
//  }

  @FXML
  public void initialize() throws SQLException {
    db = new Database();
  }



  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    NodeTableController create();
  }
}
