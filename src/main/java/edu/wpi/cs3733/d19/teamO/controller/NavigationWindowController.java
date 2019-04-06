package edu.wpi.cs3733.d19.teamO.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;

import edu.wpi.cs3733.d19.teamO.component.MapView;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import edu.wpi.cs3733.d19.teamO.entity.pathfinding.PathfindingContext;

@SuppressWarnings("PMD.GodClass")
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
  private JFXComboBox<Node> startSelLocJFXCombo;
  @FXML
  private JFXComboBox<Node> endSelLocJFXCombo;

  @FXML
  private ToggleGroup startLocationToggle;
  @FXML
  private ToggleGroup endLocationToggle;

  @FXML
  private MapView map;

  private Set<Node> nodeSet;

  private Database database;
  private Group bfsPath;

  @FXML
  void initialize() throws SQLException, IOException {
    database = new Database();
    nodeSet = database.getAllNodes();

    bfsPath = new Group();

    Image image = new Image(getClass().getResource("01_thefirstfloor.png").openStream());
    map.setMapImage(image);
    map.addNodesToPane(database.getAllNodes());
    //map.addEdgesToPane(database.getAllEdges());

    //    ObservableList<Node> locations = FXCollections.observableArrayList();
    for (Node node : nodeSet) {
      startSelLocJFXCombo.getItems().add(node);
      endSelLocJFXCombo.getItems().add(node);
    }
    //    for (Node node : locations) {
    //
    //    }

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
        for (Node node : nodeSet) {
          if (node.getLongName().equals(string)) {
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
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  void onGenPathButtonAction() throws SQLException {
    map.getEdges().getChildren().removeAll(bfsPath);

    PathfindingContext<Node> pathfindingContext = new PathfindingContext<>();
    MutableGraph<Node> graph = GraphBuilder.undirected().allowsSelfLoops(false).build();
    database.getAllNodes().forEach(graph::addNode);
    database.getAllEdges().forEach(e -> graph.putEdge(e.getStartNode(), e.getEndNode()));

    List<Node> path = pathfindingContext.getPath(ImmutableGraph.copyOf(graph),
        startSelLocJFXCombo.getValue(),
        endSelLocJFXCombo.getValue());

    bfsPath = new Group();

    Node lastNode = null;
    for (Node node : path) {
      if (lastNode != null) {
        bfsPath.getChildren().add(new Line(node.getXcoord(), node.getYcoord(),
            lastNode.getXcoord(), lastNode.getYcoord()));
      } else {
        Rectangle rectangle = new Rectangle(node.getXcoord() - 8, node.getYcoord() - 8, 16, 16);
        rectangle.setFill(Color.RED);
        rectangle.setStroke(Color.BLACK);
        bfsPath.getChildren().add(rectangle);
      }
      lastNode = node;
    }

    Circle circle = new Circle(lastNode.getXcoord(), lastNode.getYcoord(), 8, Color.GREEN);
    circle.setStroke(Color.BLACK);
    bfsPath.getChildren().add(circle);

    map.getEdges().getChildren().addAll(bfsPath);
  }

  void validateGenButton() {
    if (startSelLocJFXCombo.getValue() != null && endSelLocJFXCombo.getValue() != null) {
      genPathButton.setDisable(false);
    } else {
      genPathButton.setDisable(true);
    }
  }
}
