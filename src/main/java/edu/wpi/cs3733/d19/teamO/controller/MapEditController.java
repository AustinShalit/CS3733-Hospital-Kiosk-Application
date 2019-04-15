package edu.wpi.cs3733.d19.teamO.controller;

import java.util.Optional;
import java.util.Set;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.component.MapView2;
import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@SuppressWarnings("PMD.TooManyFields")
@FxmlController(url = "MapEdit.fxml")
public class MapEditController implements Controller {
  private String nodeID;
  private String edgeID;
  private boolean updateMode = true;
  private boolean connectMode;
  private String udNodeID1 = " ";
  // Collection<Node> nodes;

  @FXML
  MapView2 map;
  @FXML
  BorderPane root;
  @FXML
  JFXButton addButton;
  @FXML
  JFXButton deleteButton;
  @FXML
  JFXButton updateButton;
  @FXML
  JFXButton connectButton;
  @FXML
  JFXButton edgeDeleteButton;
  @FXML
  TableView<Node> tableView;
  @FXML
  JFXTextField xcoordField;
  //  @FXML
  //  JFXTextField nodeIDField;
  @FXML
  JFXTextField ycoordField;
  @FXML
  JFXTextField floorField;
  @FXML
  JFXTextField buildingField;
  @FXML
  JFXTextField longNameField;
  @FXML
  JFXTextField shortNameField;
  @FXML
  JFXTextField nodeIDField;
  @FXML
  Label status;
  @FXML
  JFXComboBox<Node.NodeType> nodeTypeComboBox;
  @FXML
  TabPane tabPane;
  @FXML
  Tab mapTab;
  @FXML
  Tab tableTab;

  @FXML
  TableColumn<Node, String> nodeIdCol;
  @FXML
  TableColumn<Node, Integer> coordXcol;
  @FXML
  TableColumn<Node, Integer> coordYcol;
  @FXML
  TableColumn<Node, String> floorCol;
  @FXML
  TableColumn<Node, String> buildingCol;
  @FXML
  TableColumn<Node, String> typeCol;
  @FXML
  TableColumn<Node, String> shortNameCol;
  @FXML
  TableColumn<Node, String> longNameCol;


  @Inject
  private Database database;

  @FXML
  void initialize() {

    // Setup the table view
    setupTableView();

    // Setup side view
    nodeTypeComboBox.getItems().addAll(Node.NodeType.values());
    map.setNodes(database.getAllNodes());
    map.setCurrentNodes(database.getAllNodes());
    map.setDatabaseEdge(database.getAllEdges());
    map.setCurrentEdges(database.getAllEdges());
    map.addNodesToPane(database.getFloor("1"));
    map.addEdgesToPane(database.getEdgeByFloor(map.getLevel()));
    map.selectedNodeProperty().addListener((observable, oldValue, newValue) -> {
      nodeID = newValue.getNodeId();
      nodeIDField.setText(newValue.getNodeId());

      xcoordField.setText(Integer.toString(newValue.getXcoord()));
      ycoordField.setText(Integer.toString(newValue.getYcoord()));
      floorField.setText(newValue.getFloor());
      buildingField.setText(newValue.getBuilding());

      nodeTypeComboBox.setValue(newValue.getNodeType());
      longNameField.setText(newValue.getLongName());
      shortNameField.setText(newValue.getShortName());
      if (updateMode) {
        validateButton();
      }
      //status.setText("");

      if (connectMode) {
        connectNodeAction();
      }
    });

    map.selectedEdgeProperty().addListener((observable, oldValue, newValue) -> {
      edgeID = newValue.getEdgeId();
      status.setText(newValue.getEdgeId());
    });

    // set tab pane to span entire width
    tabPane.widthProperty().addListener((observable, oldValue, newValue) -> {
      tabPane.setTabMinWidth(tabPane.getWidth() / 2);
      tabPane.setTabMaxWidth(tabPane.getWidth() / 2);
    });
  }

  /**
   * Check if all fields filled.
   */
  @FXML
  public void validateButton() {
    if (xcoordField.getText().isEmpty() || ycoordField.getText().isEmpty()
        || floorField.getText().isEmpty() || buildingField.getText().isEmpty()
        || nodeTypeComboBox.getValue() == null || longNameField.getText().isEmpty()

        || shortNameField.getText().isEmpty() || nodeIDField.getText().isEmpty()) {

      addButton.setDisable(true);
      connectButton.setDisable(true);
      deleteButton.setDisable(true);
      updateButton.setDisable(true);
    } else {
      addButton.setDisable(false);
      connectButton.setDisable(false);
      deleteButton.setDisable(false);
      updateButton.setDisable(false);
    }
  }

  @FXML
  void addNodeAction() {
    Node newNode = getNewNode(database.getFreeNodeId());
    database.insertNode(newNode);
    status.setText("Succeed!");

    Set<Node> nodes = database.getAllNodes();
    map.setNodes(nodes);
    map.clearNodes();
    map.addNodesToPane(database.getFloor(map.getLevel()));

    tableView.getItems().setAll(nodes); //todo use observer
  }

  @FXML
  void deleteNodeAction() {
    String delNodeID = nodeID;
    database.getEdgesFor(getNewNode(delNodeID)).forEach(database::deleteEdge);
    Optional<Node> opt = database.getNode(delNodeID);
    if (!opt.isPresent()) {
      status.setText("ERROR: InvalidNodeID");
    } else {
      Node deleteNode = opt.get();
      database.deleteNode(deleteNode);
      status.setText("Succeed!");
      map.setNodes(database.getAllNodes());
      map.clearNodes();
      map.addNodesToPane(database.getFloor(map.getLevel()));
    }
  }


  @FXML
  void updateNodeAction() {
    if (updateMode) {
      map.setCircleVisibility(false);
      String udNodeID = nodeID;
      //database.getEdgesFor(getNewNode(udNodeID)).forEach(database::deleteEdge);
      Optional<Node> opt = database.getNode(udNodeID);
      if (!opt.isPresent()) {
        status.setText("ERROR: InvalidNodeID");
      } else {
        Node deleteNode = opt.get();
        map.setCircleDrag(deleteNode.getXcoord(), deleteNode.getYcoord());
        map.setCircleDragVisibility(true);
        Node updateNode = new Node(nodeIDField.getText(),
            50000,
            50000,
            floorField.getText(),
            buildingField.getText(),
            nodeTypeComboBox.getValue(),
            longNameField.getText(),
            shortNameField.getText());
        database.updateNode(updateNode);
        //database.deleteNode(deleteNode);
        map.setNodes(database.getAllNodes());
        map.clearNodes();
        map.addNodesToPane(database.getFloor(map.getLevel()));
        map.setGesturePane(false);
        map.setDragStatus(true);
        updateButton.setText("Confirm");
        updateMode = false;
        addButton.setDisable(true);
        deleteButton.setDisable(true);
        connectButton.setDisable(true);
      }
    } else {
      Node updateNode = getNewNode(nodeIDField.getText());
      database.updateNode(updateNode);
      status.setText("Succeed!");
      map.setNodes(database.getAllNodes());
      map.clearEdges();
      map.addEdgesToPane(database.getEdgeByFloor(map.getLevel()));
      map.clearNodes();
      map.addNodesToPane(database.getFloor(map.getLevel()));
      updateButton.setText("Update");
      updateMode = true;
      map.setDragStatus(false);
      map.setGesturePane(true);
      map.setCircleDragVisibility(false);
      addButton.setDisable(false);
      deleteButton.setDisable(false);
      connectButton.setDisable(false);
    }

  }


  @FXML
  void connectNodeAction() {

    if (!connectMode) {
      udNodeID1 = nodeIDField.getText();
      Optional<Node> nodeFromDB1 = database.getNode(udNodeID1);
      if (!nodeFromDB1.isPresent()) {
        status.setText("ERROR: InvalidNodeID");
      } else {
        connectMode = true;
        status.setText("Please choose the other node");
      }
    } else {
      String udNodeID2 = nodeIDField.getText();
      Optional<Node> nodeFromDB2 = database.getNode(udNodeID2);
      Optional<Node> nodeFromDB1 = database.getNode(udNodeID1);
      if (!nodeFromDB2.isPresent() || !nodeFromDB1.isPresent()) {
        status.setText("ERROR: InvalidNodeID");
      } else {
        Node node1 = nodeFromDB1.get();
        Node node2 = nodeFromDB2.get();
        Edge newEdge = new Edge(database.getFreeEdgeId(), node1, node2);
        database.insertEdge(newEdge);
        map.clearEdges();
        map.addEdgesToPane(database.getEdgeByFloor(map.getLevel()));
        status.setText("Succeed!");
        connectMode = false;
      }

    }
  }

  @FXML
  void onEnter() {
    map.setCircleDrag( Integer.parseInt(xcoordField.getText()),
        Integer.parseInt(ycoordField.getText()));
  }

  @FXML
  void onEdgeDelete() {
      Optional<Edge> opt = database.getEdge(edgeID);
      if (!opt.isPresent()) {
        status.setText("ERROR: InvalidNodeID");
      } else {
        Edge deleteEdge = opt.get();
        database.deleteEdge(deleteEdge);
        status.setText("Succeed!");
        map.setDatabaseEdge(database.getAllEdges());
        map.clearEdges();
        map.addEdgesToPane(database.getEdgeByFloor(map.getLevel()));
      }
  }


  private Node getNewNode(String s) {
    return new Node(s,
        Integer.parseInt(xcoordField.getText()),
        Integer.parseInt(ycoordField.getText()),
        floorField.getText(),
        buildingField.getText(),
        nodeTypeComboBox.getValue(),
        longNameField.getText(),
        shortNameField.getText());
  }

  /**
   * Setup the table view for use in this controller. Initializes the columns,
   * fills with data, sorts and enables auto resizing.
   */
  private void setupTableView() {
    nodeIdCol.setCellValueFactory(new PropertyValueFactory<>("nodeId"));
    coordXcol.setCellValueFactory(new PropertyValueFactory<>("Xcoord"));
    coordYcol.setCellValueFactory(new PropertyValueFactory<>("Ycoord"));
    floorCol.setCellValueFactory(new PropertyValueFactory<>("Floor"));
    buildingCol.setCellValueFactory(new PropertyValueFactory<>("Building"));
    typeCol.setCellValueFactory(new PropertyValueFactory<>("NodeType"));
    shortNameCol.setCellValueFactory(new PropertyValueFactory<>("ShortName"));
    longNameCol.setCellValueFactory(new PropertyValueFactory<>("LongName"));

    tableView.getItems().setAll(database.getAllNodes());

    // sort by id
    tableView.getSortOrder().add(nodeIdCol);

    // make columns auto-resize
    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    for (TableColumn column : tableView.getColumns()) {
      column.setPrefWidth(1000); // must be a value larger than the starting window size
    }
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    MapEditController create();
  }
}
