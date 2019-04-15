package edu.wpi.cs3733.d19.teamO.controller;

import java.io.IOException;
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
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.component.NewMapView;
import edu.wpi.cs3733.d19.teamO.entity.Floor;
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
  private NewMapView map;
  @FXML
  private Label instructionsLabel;

  @Inject
  private Database database;

  private final ListProperty<Node> path = new SimpleListProperty<>();

  @FXML
  void initialize() throws IOException {
    DialogHelper.populateComboBox(database, fromComboBox);
    DialogHelper.populateComboBox(database, toComboBox);

    goButton.disableProperty().bind(Bindings.createBooleanBinding(
        () -> fromComboBox.getValue() == null || toComboBox.getValue() == null,
        fromComboBox.valueProperty(),
        toComboBox.valueProperty()
    ));

    path.addListener(((observable, oldValue, newValue) -> {
      List<String> steps = new StepByStep().getStepByStep(newValue);
      StringBuilder stringBuilder = new StringBuilder();
      for (String s : steps) {
        stringBuilder.append(s);
        stringBuilder.append('\n');
      }
      instructionsLabel.setText(stringBuilder.toString());
    }));

    map.getPathView().pathProperty().bind(path);

    Floor floor3 = new Floor(3, "3", new Image(NewMapView.class.getResource("03_thethirdfloor.png").openStream()));
    Floor floor2 = new Floor(2, "2", new Image(NewMapView.class.getResource("02_thesecondfloor.png").openStream()));
    Floor floor1 = new Floor(1, "1", new Image(NewMapView.class.getResource("01_thefirstfloor.png").openStream()));
    Floor floorG = new Floor(0, "G", new Image(NewMapView.class.getResource("00_thegroundfloor.png").openStream()));
    Floor floorL1 = new Floor(-1, "L1", new Image(NewMapView.class.getResource("00_thelowerlevel1.png").openStream()));
    Floor floorL2 = new Floor(-2, "L2", new Image(NewMapView.class.getResource("00_thelowerlevel2.png").openStream()));

    map.setFloors(FXCollections.observableArrayList(floor3, floor2, floor1, floorG, floorL1, floorL2));
    map.setFloor(floor1);
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

    path.setValue(FXCollections.observableArrayList(
        pathfindingContext.getPath(ImmutableGraph.copyOf(graph),
        fromComboBox.getValue(),
        toComboBox.getValue())));

//    map.setPath(path);
//    map.drawPath();
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    NavigationController create();
  }
}
