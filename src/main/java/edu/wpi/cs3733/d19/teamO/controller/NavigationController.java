package edu.wpi.cs3733.d19.teamO.controller;

import java.util.List;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.component.MapView;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import edu.wpi.cs3733.d19.teamO.entity.pathfinding.PathfindingContext;
import edu.wpi.cs3733.d19.teamO.entity.pathfinding.StepByStep;

@FxmlController(url = "Navigation.fxml")
public class NavigationController implements Controller {

  @FXML
  private BorderPane root;
  @FXML
  private JFXComboBox<Node> fromComboBox;
  @FXML
  private JFXComboBox<Node> toComboBox;
  @FXML
  private JFXButton goButton;
  @FXML
  private MapView map;
  @FXML
  private Label instructionsLabel;

  @Inject
  private Database database;

  private ListProperty<String> instructions = new SimpleListProperty<>();

  @FXML
  void initialize() {
    DialogHelper.populateComboBox(database, fromComboBox);
    DialogHelper.populateComboBox(database, toComboBox);

    goButton.disableProperty().bind(Bindings.createBooleanBinding(
        () -> fromComboBox.getValue() == null || toComboBox.getValue() == null,
        fromComboBox.valueProperty(),
        toComboBox.valueProperty()
    ));

    instructions.addListener(((observable, oldValue, newValue) -> {
      StringBuilder stringBuilder = new StringBuilder();
      for (String s : newValue) {
        stringBuilder.append(s);
        stringBuilder.append('\n');
      }
      instructionsLabel.setText(stringBuilder.toString());
    }));
  }

  @FXML
  void onGoButtonAction() {
    if (toComboBox.getValue().equals(fromComboBox.getValue())) {
      DialogHelper.showInformationAlert("Must Select Different Start/End Destinations",
          "Please select different start and end destinations to generate a valid path.");
      return;
    }

    PathfindingContext<Node> pathfindingContext = new PathfindingContext<>();
    MutableGraph<Node> graph = GraphBuilder.undirected().allowsSelfLoops(false).build();
    database.getAllNodes().forEach(graph::addNode);
    database.getAllEdges().forEach(e -> graph.putEdge(e.getStartNode(), e.getEndNode()));

    List<Node> path = pathfindingContext.getPath(ImmutableGraph.copyOf(graph),
        fromComboBox.getValue(),
        toComboBox.getValue());

    instructions.setValue(FXCollections.observableList(new StepByStep().getStepByStep(path)));

    map.setPath(path);
    map.drawPath();
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    NavigationController create();
  }
}
