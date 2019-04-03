package edu.wpi.cs3733.d19.teamO.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.Stack;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;

import edu.wpi.cs3733.d19.teamO.BreadthFirstSearchAlgorithm;
import edu.wpi.cs3733.d19.teamO.component.MapView;
import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class NavigationWindowController extends Controller {

  @FXML
  private JFXButton backButton;
  @FXML
  private JFXButton addAStopButton;
  @FXML
  private JFXButton genPathButton;

  @FXML
  private JFXRadioButton startCurrLocJFXRadio;
  @FXML
  private JFXRadioButton startSelLocJFXRadio;
  @FXML
  private JFXRadioButton endCurrLocJFXRadio;
  @FXML
  private JFXRadioButton endSelLocJFXRadio;

  @FXML
  private JFXComboBox startSelLocJFXCombo;
  @FXML
  private JFXComboBox endSelLocJFXCombo;

  @FXML
  private ToggleGroup startLocationToggle;
  @FXML
  private ToggleGroup endLocationToggle;

  @FXML
  private MapView map;

  private Set<Node> nodeSet;

  private Group BFSpath;

  @FXML
  void initialize() throws SQLException, IOException {
    Database database = new Database();
    nodeSet = database.getAllNodes();

    BFSpath = new Group();

    Image image = new Image(getClass().getResource("01_thefirstfloor.png").openStream());
    map.setMapImage(image);
    map.addNodesToPane(database.getAllNodes());
    //map.addEdgesToPane(database.getAllEdges());

    ObservableList<Node> locations = FXCollections.observableArrayList();
    for (Node node : nodeSet) {
      locations.add(node);
    }
    for (Node node : locations) {
      startSelLocJFXCombo.getItems().add(node);
      endSelLocJFXCombo.getItems().add(node);
    }

    StringConverter<Node> str = new StringConverter<Node>() {
      @Override
      public String toString(Node object) {
        if (object != null) {
          return object.getLongName();
        }
        return null;
      }

      @Override
      public Node fromString(String string) {
        for(Node node: nodeSet) {
          if(node.getLongName().equals(string)) {
            return node;
          }
        }
        return null;
      }
    };

    startSelLocJFXCombo.setConverter(str);
    endSelLocJFXCombo.setConverter(str);
  }

  @FXML
  void onStartCurrLocRadioAction() {
    startSelLocJFXCombo.setDisable(true);
    startCurrLocJFXRadio.setText("Not implemented yet.");
  }

  @FXML
  void onStartSelLocRadioAction() {
    startCurrLocJFXRadio.setText("Current Location");
    startSelLocJFXCombo.setDisable(false);
  }

  @FXML
  void onStartSelLocComboAction() {
    validateGenButton();
  }

  @FXML
  void onEndCurrLocRadioAction() {
    endSelLocJFXCombo.setDisable(true);
    endCurrLocJFXRadio.setText("Not implemented yet.");
  }

  @FXML
  void onEndSelLocRadioAction() {
    endCurrLocJFXRadio.setText("Current Location");
    endSelLocJFXCombo.setDisable(false);
  }

  @FXML
  void onEndSelLocComboAction() {
    validateGenButton();
  }

  @FXML
  void onBackButtonAction() {
    switchScenes("MainWindow.fxml", backButton.getScene().getWindow());
  }

  @FXML
  void onGenPathButtonAction() throws SQLException {

    map.getEdges().getChildren().removeAll(BFSpath);
    System.out.println("Before: " + map.getEdges().getChildren().size());

    BreadthFirstSearchAlgorithm bfsAlgorithm = new BreadthFirstSearchAlgorithm(new Database());
    Stack<Node> path = bfsAlgorithm.getPath(((Node) startSelLocJFXCombo.getValue()), ((Node) endSelLocJFXCombo.getValue()));

    BFSpath = new Group();

    Node lastNode = null;
    for(Node node: path) {
      if(lastNode != null) {
        BFSpath.getChildren().add(new Line(node.getXcoord(), node.getYcoord(), lastNode.getXcoord(), lastNode.getYcoord()));
      }
      else {
        Rectangle s = new Rectangle((node.getXcoord() - 8), (node.getYcoord() - 8), 16, 16);
        s.setFill(Color.RED);
        s.setStroke(Color.BLACK);
        BFSpath.getChildren().add(s);
      }
      lastNode = node;
    }

    Circle r = new Circle(lastNode.getXcoord(), lastNode.getYcoord(), 8, Color.GREEN);
    r.setStroke(Color.BLACK);
    BFSpath.getChildren().add(r);

    map.getEdges().getChildren().addAll(BFSpath);
    System.out.println("After: " + map.getEdges().getChildren().size());
  }

  void validateGenButton() {
    if (startSelLocJFXCombo.getValue() != null && endSelLocJFXCombo.getValue() != null) {
      genPathButton.setDisable(false);
    } else {
      genPathButton.setDisable(true);
    }
  }

  void checkLine(Line line, Stack<Node> nodes) {
    boolean found = false;

    for(Node node: nodes) {
      if(nodes.peek() != null) {
        if(line.getStartX() == node.getXcoord()
            && line.getStartY() == node.getYcoord()
            && nodes.peek().getXcoord() == line.getEndX()
            && nodes.peek().getYcoord() == line.getEndY()) {
          found = true;
        }
        else if(line.getEndX() == node.getXcoord()
            && line.getEndY() == node.getYcoord()
            && line.getStartX() == nodes.peek().getXcoord()
            && line.getStartY() == nodes.peek().getYcoord()) {
          found = true;
        }
      }

    }
    if(found) {
      line.setVisible(true);
    }
  }
}
