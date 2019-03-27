package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

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
  private TableColumn<Node, Button> editColumn;

  @FXML
  private TableColumn<Node, Button> deleteColumn;

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

    // Add the edit/delete buttons in each row
    addColumnsWithButtons();

    table.setItems(list);
  }


  // this method should open addData and then prefill the fields
  void editButtonAction(Button editDataButton, Node nodeToEdit) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditData.fxml"));
    Parent root = fxmlLoader.load();
    EditDataController controller = fxmlLoader.getController();
    controller.setNode(nodeToEdit);

    Stage dialog = new Stage();
    dialog.initStyle(StageStyle.UTILITY);
    Scene scene = new Scene(root);
    dialog.setScene(scene);
    dialog.show();
  }

  void deleteButtonAction(Node nodeToDelete) {
    Database db = new Database();
    db.deleteNode(nodeToDelete.nodeID);
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

  /**
   * Add the edit and delete columns, then fill each row with an
   * edit and delete button.
   */
  private void addColumnsWithButtons() {
    TableColumn<Node, Void> column = new TableColumn("modify");

    Callback<TableColumn<Node, Void>, TableCell<Node, Void>> cellFactory =
        new Callback<TableColumn<Node, Void>, TableCell<Node, Void>>() {
          @Override
          public TableCell<Node, Void> call(final TableColumn<Node, Void> param) {
            final TableCell<Node, Void> cell = new TableCell<Node, Void>() {

              private final Button editButton = new Button("edit");
              private final Button deleteButton = new Button("delete");
              private final HBox pane = new HBox(editButton, deleteButton);

              {
                editButton.setOnAction((ActionEvent event) -> {
                  Node node = getTableView().getItems().get(getIndex());
                  System.out.println("edit: " + node);

                  try {
                    editButtonAction(editButton, node);
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                });

                deleteButton.setOnAction((ActionEvent event) -> {
                  Node node = getTableView().getItems().get(getIndex());
                  System.out.println("delete: " + node);

                  deleteButtonAction(node);
                });
              }

              @Override
              public void updateItem(Void item, boolean empty) {

                super.updateItem(item, empty);
                if (empty) {
                  setGraphic(null);
                } else {
                  setGraphic(pane);
                }
              }
            };
            return cell;
          }
        };

    column.setCellFactory(cellFactory);

    table.getColumns().add(column);
  }

}
