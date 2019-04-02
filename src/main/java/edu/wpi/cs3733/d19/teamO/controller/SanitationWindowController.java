package edu.wpi.cs3733.d19.teamO.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;
import edu.wpi.cs3733.d19.teamO.entity.csv.NodeCsvReaderWriter;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class SanitationWindowController extends Controller {

  @FXML
  private Button backButton;
  @FXML
  private Button submitButton;
  @FXML
  private Label titleLabel;
  @FXML
  private TextArea descriptionTextArea;
  @FXML
  private ComboBox locationComboBox;
  @FXML
  private ComboBox<SanitationRequest.SanitationRequestType> categoryComboBox;

  Database db;

  @FXML
  void initialize() throws SQLException, IOException {
    db = new Database();
    //    Set<Node> nodes = db.getAllNodes();

    // read from file for now, until database is populated
    FileChooser chooser = new FileChooser();
    File file = chooser.showOpenDialog(new Stage());
    NodeCsvReaderWriter ncrw = new NodeCsvReaderWriter();
    List<Node> nodes = ncrw.readNodes(Files.newBufferedReader(file.toPath()));
    for (Node n : nodes) {
      locationComboBox.getItems().add(n.getNodeId());
      db.insertNode(n);
    }

    categoryComboBox.getItems().setAll(SanitationRequest.SanitationRequestType.values());
  }


  @FXML
  void onBackButtonAction() {
    switchScenes("MainSanitationWindow.fxml", backButton.getScene().getWindow());
  }


  @FXML
  void onSubmitButtonAction() {
    SanitationRequest sr = parseUserSanitationRequestTest();
    db.insertSanitationRequest(sr);
  }

  /**
   * Parse input the user has inputted for the sanitation request.
   *
   * @return A SanitationRequest representing the users input.
   */
  private SanitationRequest parseUserSanitationRequestTest() {
    LocalDateTime now = LocalDateTime.now();
    String nodeId = locationComboBox.getValue().toString();
    Node node = db.getNode(nodeId).get();

    String type = categoryComboBox.getValue().toString().toUpperCase(new Locale("EN"));
    SanitationRequest.SanitationRequestType srt =
        SanitationRequest.SanitationRequestType.valueOf(type);

    String description = descriptionTextArea.getText();

    return new SanitationRequest(now, node, srt, description);
  }
}
