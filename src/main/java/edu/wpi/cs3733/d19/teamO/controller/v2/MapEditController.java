package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.util.Optional;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.component.MapView2;
import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@SuppressWarnings("PMD.TooManyFields")
@FxmlController(url = "MapEdit.fxml")
public class MapEditController implements Controller {
  String nodeID;
  int newID = 100;
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
  AnchorPane tableView;
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
  JFXTextField nodeIDField2;
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

  @Inject
  private Database database;

  @FXML
  void initialize() {

    map.setNodes(database.getAllNodes());
    map.setCurrentNodes(database.getAllNodes());
    map.addNodesToPane(database.getFloor("1"));
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
      validateButton();
      status.setText("");
      nodeIDField2.setText("");
    });

    // set tab pane to span entire width
    tabPane.widthProperty().addListener((observable, oldValue, newValue) -> {
      tabPane.setTabMinWidth(tabPane.getWidth() / 2);
      tabPane.setTabMaxWidth(tabPane.getWidth() / 2);
    });
  }

  /**
   *  A cool comboBox of nodetype.
   */
  @SuppressWarnings("PMD.UnusedPrivateMethod")
  @FXML
  private void comboBox() {
    nodeTypeComboBox.getItems().addAll(Node.NodeType.values());
  }

  /**
   *  Check if all fields filled.
   */
  @FXML
  public void validateButton() {
    if ( xcoordField.getText().isEmpty() || ycoordField.getText().isEmpty()
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
    Node newNode = getNewNode(Integer.toString(newID));
    database.insertNode(newNode);
    status.setText("Succeed!");
    map.setNodes(database.getAllNodes());
    map.clearNodes();
    map.addNodesToPane(database.getFloor(map.getLevel()));
    newID++;
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
    String udNodeID = nodeID;
    Optional<Node> nodeFromDB = database.getNode(udNodeID);
    if (!nodeFromDB.isPresent()) {
      status.setText("ERROR: InvalidNodeID");
    } else {
      Node updateNode = getNewNode(nodeID);
      database.updateNode(updateNode);
      status.setText("Succeed!");
      map.setNodes(database.getAllNodes());
      map.clearNodes();
      map.addNodesToPane(database.getFloor(map.getLevel()));
    }
  }

  @FXML
  void connectNodeAction() {
    String udNodeID1 = nodeIDField.getText();
    Optional<Node> nodeFromDB1 = database.getNode(udNodeID1);
    String udNodeID2 = nodeIDField2.getText();
    Optional<Node> nodeFromDB2 = database.getNode(udNodeID2);
    if (!nodeFromDB1.isPresent() || !nodeFromDB2.isPresent()) {
      status.setText("ERROR: InvalidNodeID");
    } else {
      Node node1 = nodeFromDB1.get();
      Node node2 = nodeFromDB2.get();
      Edge newEdge = new Edge("Ken" +  Integer.toString(newID), node1, node2);
      database.insertEdge(newEdge);
      status.setText("Succeed!");
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

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    MapEditController create();
  }
}
