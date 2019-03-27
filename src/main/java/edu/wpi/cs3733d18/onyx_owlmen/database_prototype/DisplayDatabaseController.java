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
  private TableView<UINode> table;

  @FXML
  private TableColumn<UINode, String> nodeID;

  @FXML
  private TableColumn<UINode, Integer> xcoord;

  @FXML
  private TableColumn<UINode, Integer> ycoord;

  @FXML
  private TableColumn<UINode, Integer> floor;

  @FXML
  private TableColumn<UINode, String> building;

  @FXML
  private TableColumn<UINode, NodeType> nodeType;

  @FXML
  private TableColumn<UINode, String> longName;

  @FXML
  private TableColumn<UINode, String> shortName;

  @FXML
  private TableColumn<UINode, Button> editColumn;

  @FXML
  private TableColumn<UINode, Button> deleteColumn;

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
   * Also handles displaying edit and delete button.
   */
  void populateTableView() {
    Database db = new Database();

    // Create a list of data to put in our table
    ObservableList<UINode> list = FXCollections.observableArrayList();
    for (Node n : db.getNodes()) {
      list.add(n.asUINode());
    }

    // Link to the data values
    nodeID.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
    xcoord.setCellValueFactory(new PropertyValueFactory<>("xcoord"));
    ycoord.setCellValueFactory(new PropertyValueFactory<>("ycoord"));
    floor.setCellValueFactory(new PropertyValueFactory<>("floor"));
    building.setCellValueFactory(new PropertyValueFactory<>("building"));
    nodeType.setCellValueFactory(new PropertyValueFactory<>("nodeType"));
    longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
    shortName.setCellValueFactory(new PropertyValueFactory<>("shortName"));

    // now we need to create edit buttons in each row
    editColumn.setCellValueFactory(new PropertyValueFactory<>("editButton"));
    deleteColumn.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));

    table.setItems(list);
  }

  // this method should open addData and then prefill the fields
  void editButtonAction() throws IOException {
    Stage stage = (Stage) addDataButton.getScene().getWindow();
    Parent root = FXMLLoader.load(getClass().getResource("addData.fxml"));


    popupWindow(stage, root);


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
