package edu.wpi.cs3733.d19.teamO.controller;

import java.io.IOException;
import java.util.List;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import com.google.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.AppPreferences;
import edu.wpi.cs3733.d19.teamO.component.MapView;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import edu.wpi.cs3733.d19.teamO.entity.pathfinding.IGraphSearchAlgorithm;

@FxmlController(url = "FireAlarm.fxml")
public class FireAlarmController implements Controller{

  @FXML
  BorderPane root;
  @FXML
  MapView map;

  @Inject
  private AppPreferences appPreferences;
  @Inject
  private Database database;

  @FXML
  void initialize() throws IOException {
    IGraphSearchAlgorithm<Node> algorithm = appPreferences.getGraphSearchAlgorithm().getAlgorithm();
    MutableGraph<Node> graph = GraphBuilder.undirected().allowsSelfLoops(false).build();
    database.getAllNodes().forEach(graph::addNode);
    database.getAllEdges().forEach(e -> graph.putEdge(e.getStartNode(), e.getEndNode()));

    List<Node> path = algorithm.getPath(ImmutableGraph.copyOf(graph),
        database.getNode("FRETL00201").get(),
        database.getNode("FEXIT00201").get());

    map.zoomTo(database.getNode("FRETL00201").get());
    map.setPath(path);
    map.drawPath();

  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    FireAlarmController create();
  }
}
