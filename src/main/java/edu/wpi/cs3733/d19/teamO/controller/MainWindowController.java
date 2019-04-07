package edu.wpi.cs3733.d19.teamO.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

@SuppressWarnings("PMD.TooManyMethods")
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
  private Button logoutButton;
  @FXML
  private Button nodeImportButton;
  @FXML
  private Button edgeImportButton;
  @FXML
  private Button itrequestButton;
  @FXML
  private Button itrequestviewButton;
  @FXML
  private Label nodeImportInProgress;


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
  void itButtonButtonAction(ActionEvent event) {
    switchScenes("InternalTransportationWindow.fxml", itrequestButton.getScene().getWindow());
  }

  @FXML
  void itviewButtonButtonAction(ActionEvent event) {
    switchScenes("InternalTransportationViewWindow.fxml",
        itrequestviewButton.getScene().getWindow());
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
    edgeImportButton.setDisable(false);

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
      Node start = edge.getStartNode();
      Node end = edge.getEndNode();
      if (start.getFloor().equals("1") && end.getFloor().equals("1")) {
        database.insertEdge(edge);
      }
      if (!(edge.getStartNode().getFloor().equals("1"))) {
        database.deleteNode(start);
      }
      if (!(edge.getEndNode().getFloor().equals("1"))) {
        database.deleteNode(end);
      }
    }
    nodeImportInProgress.setText("Edges Imported");
  }

  @FXML
  void logoutButtonAction() {
    switchScenes("LoginWindow.fxml", schedulingButton.getScene().getWindow());
  }

  @FXML
  public void initialize() {
  }
}

