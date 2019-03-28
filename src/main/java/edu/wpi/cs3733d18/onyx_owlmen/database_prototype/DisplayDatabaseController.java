package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.opencsv.bean.CsvToBeanBuilder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import edu.wpi.cs3733d18.onyx_owlmen.NodeCSV;

public class DisplayDatabaseController {

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


  private final Database database = Database.getInstance();

  @FXML
  void initialize() {
    populateTableView();
    addColumnsWithButtons();
  }

  /**
   * Fetch and display data to the table.
   */
  void populateTableView() {
    // Create a list of data to put in our table
    ObservableList<Node> list = FXCollections.observableArrayList();
    list.addAll(database.getNodes());

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

  /**
   * Runs when the edit button is clicked. Prompts the user to enter new values for the
   * entry. Updates the table and database with the new value if save is clicked.
   *
   * @param nodeToEdit The node the user wants to edit
   * @throws IOException Throws an IOException
   */
  void editButtonAction(Node nodeToEdit) throws IOException {
    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.setTitle("Edit node");
    dialog.getDialogPane().setHeaderText("Edit node");
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);

    ModifyDataControl mdc = new ModifyDataControl();
    mdc.setNode(nodeToEdit);

    dialog.getDialogPane().setContent(mdc); // add our content

    // Wait for the dialog box to close
    Optional<ButtonType> result = dialog.showAndWait();

    result.ifPresent(event -> {
      if (!event.getButtonData().isCancelButton()) {
        // Update entry in the database
        database.updateNode(nodeToEdit.nodeID, nodeToEdit);

        Node modifiedNode = mdc.getNode();

        // Get the index of this node in the table
        int index = 0;
        for (Node n : table.getItems()) {
          if (n.nodeID.equals(nodeToEdit.nodeID)) {
            break;
          }
          index++;
        }

        // Update the table with the new node
        table.getItems().set(index, modifiedNode);
        table.refresh();

      }
    });
  }

  /**
   * Runs when the delete button is pressed. Prompts the user to confirm, if they do,
   * the node is deleted from the table and database.
   *
   * @param nodeToDelete The node the user wants to delete
   */
  void deleteButtonAction(Node nodeToDelete) {
    database.deleteNode(nodeToDelete.nodeID);

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Are you sure?");
    alert.setHeaderText("Are you sure you want to delete this node");

    // Wait for the dialog box to close
    Optional<ButtonType> result = alert.showAndWait();

    if (result.get() == ButtonType.OK) {
      // Delete from the database
      database.deleteNode(nodeToDelete.nodeID);

      // Update the table
      table.getItems().remove(nodeToDelete);
      table.refresh();
    }
  }

  @FXML
  void addDataButtonAction(ActionEvent event) throws FileNotFoundException {
    FileChooser fileChooser = new FileChooser();
    String path = fileChooser.showOpenDialog(table.getScene().getWindow()).getPath();
    if ("".equals(path)) {
      return;
    }

    List<Node> nodeList = new CsvToBeanBuilder<Node>(new FileReader(path))
        .withType(Node.class).build().parse();
    nodeList.forEach(node -> {
      Database.getInstance().addNode(node);
      table.getItems().add(node);
    });
  }

  @FXML
  void downloadButtonAction(ActionEvent event) {
    downloadButton.setText("Button not configured yet");
  }

  /**
   * Add the edit and delete columns, then fill each row with an
   * edit and delete button.
   */
  @SuppressWarnings("PMD.NonStaticInitializer")
  private void addColumnsWithButtons() {
    TableColumn<Node, Void> column = new TableColumn<>("modify");

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

                  try {
                    editButtonAction(node);
                  } catch (IOException ex) {
                    ex.printStackTrace();
                  }
                });

                deleteButton.setOnAction((ActionEvent event) -> {
                  Node node = getTableView().getItems().get(getIndex());

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
