package edu.wpi.cs3733.d19.teamO.controller;

import java.io.IOException;
import java.util.List;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import com.google.inject.Inject;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import edu.wpi.cs3733.d19.teamO.AppPreferences;
import edu.wpi.cs3733.d19.teamO.component.MapView;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import edu.wpi.cs3733.d19.teamO.entity.pathfinding.GraphSearchAlgorithm;

@FxmlController (url = "FireAlarm.fxml")
public class FireAlarmController implements Controller {

  @FXML
  BorderPane root;
  @FXML
  MapView map;
  @FXML
  Label exitlbl;
  @FXML
  VBox topBox;
  @FXML
  VBox leftBox;
  @FXML
  VBox rightBox;
  @FXML
  VBox bottomBox;

  @Inject
  private AppPreferences appPreferences;
  @Inject
  private Database database;

  @FXML
  void initialize() throws IOException {
    map.setNodes(database.getAllNodes());
    map.setNavigation(false);
    flash(topBox);
    flash(bottomBox);
    flash(leftBox);
    flash(rightBox);
    GraphSearchAlgorithm<Node> algorithm
        = appPreferences.getGraphSearchAlgorithm().getSupplier().get();
    MutableGraph<Node> graph = GraphBuilder.undirected().allowsSelfLoops(false).build();
    database.getAllNodes().forEach(graph::addNode);
    database.getAllEdges().forEach(e -> graph.putEdge(e.getStartNode(), e.getEndNode()));

    List<Node> path = algorithm.getPath(ImmutableGraph.copyOf(graph),
        database.getNode("FRETL00201").get(),
        database.getNode("FEXIT00201").get());

    map.setPath(path);

    map.drawPath();
    map.fire = true;


  }

  @FXML
  void flash(VBox flasher) {

    FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), flasher);
    fadeOut.setFromValue(1.0);
    fadeOut.setToValue(0.0);
    fadeOut.setAutoReverse(true);
    fadeOut.setCycleCount(Animation.INDEFINITE);
    fadeOut.play();
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    FireAlarmController create();
  }
}
