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

import edu.wpi.cs3733.d19.teamO.component.MapView;
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
  MapView map;

  @Inject
  private Database database;

  @FXML
  void initialize() throws IOException {
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

    PathfindingContext<Node> pathfindingContext = new PathfindingContext<>();
    MutableGraph<Node> graph = GraphBuilder.undirected().allowsSelfLoops(false).build();
    database.getAllNodes().forEach(graph::addNode);
    database.getAllEdges().forEach(e -> graph.putEdge(e.getStartNode(), e.getEndNode()));

    List<Node> path = pathfindingContext.getPath(ImmutableGraph.copyOf(graph),
        fromComboBox.getValue(),
        toComboBox.getValue());

    map.setPath(path);
    map.drawPath();
  }

  void validateGoButton() {
    if (fromComboBox.getValue() != null && toComboBox.getValue() != null) {
      goButton.setDisable(false);
    } else {
      goButton.setDisable(true);
    }
  }
}
