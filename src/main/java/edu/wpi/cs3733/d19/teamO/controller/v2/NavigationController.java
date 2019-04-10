package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

import edu.wpi.cs3733.d19.teamO.component.MapView2;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import edu.wpi.cs3733.d19.teamO.entity.pathfinding.PathfindingContext;

@FxmlController(url = "Navigation.fxml")
public class NavigationController implements Controller {

  @FXML
  BorderPane root;

  @FXML
  JFXButton bathroomButton;
  @FXML
  JFXButton elevatorButton;
  @FXML
  JFXButton exitButton;
  @FXML
  JFXButton informationButton;
  @FXML
  JFXComboBox<Node> fromComboBox;
  @FXML
  JFXComboBox<Node> toComboBox;
  @FXML
  JFXButton goButton;
  @FXML
  MapView2 map;

  Group bfsPath;

  @Inject
  private Database database;

  @FXML
  void initialize() throws IOException {

    bfsPath = new Group();

    map.addNodesToPane(database.getAllNodes());

    DialogHelper.populateComboBox(database, fromComboBox);
    DialogHelper.populateComboBox(database, toComboBox);
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    NavigationController create();
  }

  @FXML
  void onComboAction() {
    validateGoButton();
  }

  @FXML
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  void onGoButtonAction() throws SQLException {
    map.getEdges().getChildren().removeAll(bfsPath);

    PathfindingContext<Node> pathfindingContext = new PathfindingContext<>();
    MutableGraph<Node> graph = GraphBuilder.undirected().allowsSelfLoops(false).build();
    database.getAllNodes().forEach(graph::addNode);
    database.getAllEdges().forEach(e -> graph.putEdge(e.getStartNode(), e.getEndNode()));

    List<Node> path = pathfindingContext.getPath(ImmutableGraph.copyOf(graph),
        fromComboBox.getValue(),
        toComboBox.getValue());

    bfsPath = new Group();

    Node lastNode = null;
    for (Node node : path) {
      if (lastNode != null) {
        Line line = new Line(node.getXcoord(), node.getYcoord(),
            lastNode.getXcoord(), lastNode.getYcoord());
        line.setStrokeWidth(5);
        line.setStroke(Color.BLACK);
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        bfsPath.getChildren().add(line);
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
    bfsPath.getChildren().add(0, circle);

    lastNode = null;
    for (Node node : path) {
      if (lastNode != null) {
        Line line = new Line(node.getXcoord(), node.getYcoord(),
            lastNode.getXcoord(), lastNode.getYcoord());
        line.setStrokeWidth(2.5);
        line.setStroke(Color.color(0.96, 0.74, 0.22));
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        bfsPath.getChildren().add(line);
      }
      lastNode = node;
    }

    map.getEdges().getChildren().addAll(bfsPath);
  }

  void validateGoButton() {
    if (fromComboBox.getValue() != null && toComboBox.getValue() != null) {
      goButton.setDisable(false);
    } else {
      goButton.setDisable(true);
    }
  }
}
