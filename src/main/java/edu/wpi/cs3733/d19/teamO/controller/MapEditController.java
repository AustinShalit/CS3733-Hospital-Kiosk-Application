package edu.wpi.cs3733.d19.teamO.controller;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import edu.wpi.cs3733.d19.teamO.component.MapView;
import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@SuppressWarnings("PMD")
public class MapEditController extends Controller {

  private Database database = new Database();

  @FXML
  private Button add;
  @FXML
  private Button delete;
  @FXML
  private Button connect;
  @FXML
  private Button update;
  @FXML
  private Button backButton;
  @FXML
  private Button cancel;
  @FXML
  private StackPane cover;
  @FXML
  private Label coordX;
  @FXML
  private Label coordY;
  @FXML
  private Label deleteError;
  @FXML
  private Label addLabel;
  @FXML
  private TextField addNodeID;
  @FXML
  private TextField addX;
  @FXML
  private TextField addY;
  @FXML
  private TextField addBuilding;
  @FXML
  private TextField addFloor;
  @FXML
  private TextField addNodeType;
  @FXML
  private TextField shortName;
  @FXML
  private TextField longName;
  @FXML
  private TextField deleteNodeID;
  @FXML
  private TextField updateNodeID;
  @FXML
  private TextField connectNodeID1;
  @FXML
  private TextField connectNodeID2;
  @FXML
  private TextField updateX;
  @FXML
  private TextField updateY;
  @FXML
  private TextField edgeID;
  @FXML
  private AnchorPane tower1;
  @FXML
  private ComboBox<Node.NodeType> nodeType;
  @FXML
  private MapView map;

  public MapEditController() throws SQLException {
  }

  @FXML
  void initialize() throws IOException {
    if (map != null) {
      Image image = new Image(getClass().getResource("01_thefirstfloor.png").openStream());
      map.setMapImage(image);
    }
  }

  @FXML
  private void comboBox() {
    nodeType.getItems().addAll(Node.NodeType.values());
  }

  @FXML
  void mainButtonAction(ActionEvent event) throws IOException {
    Stage stage;
    Parent root;
    if (event.getSource() == add) {
      stage = new Stage();
      root = FXMLLoader.load(getClass().getResource("MapEditAdd.fxml"));
      stage.setScene(new Scene(root));
      stage.setTitle("Team O Kiosk Application");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(add.getScene().getWindow());
      stage.showAndWait();

    } else if (event.getSource() == delete) {
      stage = new Stage();
      root = FXMLLoader.load(getClass().getResource("MapEditDelete.fxml"));
      stage.setScene(new Scene(root));
      stage.setTitle("Team O Kiosk Application");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(delete.getScene().getWindow());
      stage.showAndWait();
    } else if (event.getSource() == connect) {
      stage = new Stage();
      root = FXMLLoader.load(getClass().getResource("MapEditConnect.fxml"));
      stage.setScene(new Scene(root));
      stage.setTitle("Team O Kiosk Application");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(connect.getScene().getWindow());
      stage.showAndWait();
    } else if (event.getSource() == update) {
      stage = new Stage();
      root = FXMLLoader.load(getClass().getResource("MapEditUpdate.fxml"));
      stage.setScene(new Scene(root));
      stage.setTitle("Team O Kiosk Application");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(update.getScene().getWindow());
      stage.showAndWait();
    } else {
      stage = (Stage) cancel.getScene().getWindow();
      stage.close();
    }
  }

  @FXML
  void onBackButtonAction(ActionEvent event) {
    switchScenes("MainWindow.fxml", backButton.getScene().getWindow());
  }

  @FXML
  void tower1Action(MouseEvent event) {
    //if(event.getSource()==tower1){
    //tower1.onc
    //  coord.setText("gg");
    // event.getSource().
    coordX.setText(Double.toString(event.getSceneX()));
    coordY.setText(Double.toString(event.getSceneY()));
    // }
  }

  @FXML
  void addNodeAction(ActionEvent event) {
    Node newNode = new Node(addNodeID.getText(),
        Integer.parseInt(addX.getText()),
        Integer.parseInt(addY.getText()),
        addFloor.getText(),
        addBuilding.getText(),
        nodeType.getValue(),
        longName.getText(),
        shortName.getText());
    database.insertNode(newNode);
    addLabel.setText("Succeed!");
  }

  @FXML
  void deleteNodeAcion(ActionEvent event) {
    String delNodeID = deleteNodeID.getText();
    Node deleteNode = null;
    for (Node node : database.getAllNodes()) {
      if (delNodeID.equals(node.getNodeId())) {
        deleteNode = node;
        database.deleteNode(deleteNode);
        break;
      }
    }
    if (deleteNode == null) {
      deleteError.setText("ERROR: InvalidNodeID");
    }
  }

  @FXML
  void connectNodeAcion(ActionEvent event) {
    String connect1 = connectNodeID1.getText();
    String connect2 = connectNodeID2.getText();
    Node connectN1 = null;
    Node connectN2 = null;
    for (Node node : database.getAllNodes()) {
      if (connect1.equals(node.getNodeId())) {
        connectN1 = node;
      } else if (connect2.equals(node.getNodeId())) {
        connectN2 = node;
      }
    }
    if (connectN1 != null && connectN2 != null) {
      Edge newEdge = new Edge(edgeID.getText(), connectN1, connectN2);
      database.insertEdge(newEdge);
    }
  }

  @FXML
  void updateNodeAcion(ActionEvent event) {
    String udNodeID = updateNodeID.getText();
    for (Node node : database.getAllNodes()) {
      if (udNodeID.equals(node.getNodeId())) {
        Node updateNode = new Node(node.getNodeId(),
            Integer.parseInt(updateX.getText()),
            Integer.parseInt(updateY.getText()),
            node.getFloor(),
            node.getBuilding(),
            node.getNodeType(),
            node.getLongName(),
            node.getShortName());
        database.updateNode(updateNode);
        break;
      }
    }
  }
}
