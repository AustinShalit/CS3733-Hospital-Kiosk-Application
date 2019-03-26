package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class DisplayDatabaseController extends DatabaseController implements Initializable {

  @FXML
  private TableView<Node> table;

  @FXML
  private TableColumn<Node, String> nodeID;

  @FXML
  private TableColumn<Node, Integer> xcoord;

  @FXML
  private TableColumn<Node, Integer> ycoord;

  @FXML
  private TableColumn<Node, Integer> floor;

  @FXML
  private TableColumn<Node, String> building;

  @FXML
  private TableColumn<Node, NodeType> nodeType;

  @FXML
  private TableColumn<Node, String> longName;

  @FXML
  private TableColumn<Node, String> shortName;



  @FXML
  private Button addDataButton;

  @FXML
  private Button downloadButton;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    populateTableView();
  }

  /**
   * Fetch and display data to the table.
   */
  void populateTableView() {
    Database db = new Database();

    // Create a list of data to put in our table
    ObservableList<Node> list = FXCollections.observableArrayList();
    list.addAll(db.getNodes());

    // Link to the data values
    nodeID.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
    xcoord.setCellValueFactory(new PropertyValueFactory<>("xcoord"));
    ycoord.setCellValueFactory(new PropertyValueFactory<>("ycoord"));
    floor.setCellValueFactory(new PropertyValueFactory<>("floor"));
    building.setCellValueFactory(new PropertyValueFactory<>("building"));
    nodeType.setCellValueFactory(new PropertyValueFactory<>("nodeType"));
    longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
    shortName.setCellValueFactory(new PropertyValueFactory<>("shortName"));

    table.setItems(list);
  }

  @FXML
  void addDataButtonAction(ActionEvent event) throws IOException {
    if (event.getSource() == addDataButton) {
      Stage stage = (Stage) addDataButton.getScene().getWindow();
      Parent root = FXMLLoader.load(getClass().getResource("addData.fxml"));

      popupWindow(stage, root);
    }
  }

  @FXML
  void downloadButtonAction(ActionEvent event) {
    if (event.getSource() == downloadButton) {
      downloadButton.setText("Button not configured yet");
    }
  }
}
