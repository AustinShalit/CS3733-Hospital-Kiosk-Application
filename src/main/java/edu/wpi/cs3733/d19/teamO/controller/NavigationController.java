package edu.wpi.cs3733.d19.teamO.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.google.common.eventbus.EventBus;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import org.apache.commons.collections.CollectionUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.AppPreferences;
import edu.wpi.cs3733.d19.teamO.component.FuzzyWuzzyComboBox;
import edu.wpi.cs3733.d19.teamO.component.MapView;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import edu.wpi.cs3733.d19.teamO.entity.pathfinding.IGraphSearchAlgorithm;
import edu.wpi.cs3733.d19.teamO.entity.pathfinding.StepByStep;

@FxmlController(url = "Navigation.fxml")
@SuppressWarnings({"PMD.TooManyFields", "PMD.RedundantFieldInitializer",
    "PMD.AvoidInstantiatingObjectsInLoops", "PMD.TooManyMethods"})

public class NavigationController implements Controller {

  @FXML
  BorderPane root;

  @FXML
  JFXButton restroomButton;
  @FXML
  JFXButton emergencyButton;
  @FXML
  JFXButton elevatorButton;
  @FXML
  JFXButton informationButton;
  @FXML
  FuzzyWuzzyComboBox fromComboBox;
  @FXML
  FuzzyWuzzyComboBox toComboBox;
  @FXML
  JFXButton goButton;
  @FXML
  MapView map;
  @FXML
  Label instructions;
  @FXML
  JFXButton aboutButton;
  @FXML
  ScrollPane instructionPane;

  StepByStep stepByStep;
  boolean addRest = false;
  boolean addWalk = false;
  boolean addExit = false;
  boolean addInfo = false;


  @Inject
  private AppPreferences appPreferences;
  @Inject
  private Database database;
  @Inject
  private EventBus eventBus;
  @Inject
  private AboutController.Factory aboutControllerFactory;


  @FXML
  void initialize() throws IOException {
    Collection<Node> nodes = database.getAllNodes();
    CollectionUtils.filter(
        nodes,
        object -> ((Node) object).getNodeType() != Node.NodeType.HALL
               && !((Node) object).getFloor().equals("5")
    );

    toComboBox.setNodes(nodes);
    fromComboBox.setNodes(nodes);

    toComboBox.setupAutoRefresh();
    fromComboBox.setupAutoRefresh();


    toComboBox.refresh();
    fromComboBox.refresh();

    map.setNodes(database.getAllNodes());
    stepByStep = new StepByStep();
    validateGoButton();
    map.setNavigation(true);
    map.nodeFromProperty().addListener((observable, oldValue, newValue) -> {
      fromComboBox.setValue(String.format("%s -- FLOOR %s",
          newValue.getLongName(), newValue.getFloor()));
    });
    map.nodeToProperty().addListener((observable, oldValue, newValue) -> {
      toComboBox.setValue(String.format("%s -- FLOOR %s",
          newValue.getLongName(), newValue.getFloor()));
    });
    map.nodeClickedProperty().addListener((observable, oldValue, newValue) -> {
      if (fromComboBox.isFocused()) {
        fromComboBox.setValue(String.format("%s -- FLOOR %s",
            newValue.getLongName(), newValue.getFloor()));
      } else if (toComboBox.isFocused()) {
        toComboBox.setValue(String.format("%s -- FLOOR %s",
            newValue.getLongName(), newValue.getFloor()));
      } else if (fromComboBox.getValue() == null && toComboBox.getValue() == null) {
        fromComboBox.setValue(String.format("%s -- FLOOR %s",
            newValue.getLongName(), newValue.getFloor()));
        fromComboBox.requestFocus();
      }
    });

    fromComboBox.setStyle("-fx-font-size: 12px; -fx-font-style: Palatino Linotype;");
    toComboBox.setStyle("-fx-font-size: 12px; -fx-font-style: Palatino Linotype;");

    instructionPane.setVisible(false);
    instructions.setVisible(false);

    instructionPane.setStyle("-fx-opacity: 0.8; -fx-background-color: #F1F1F1;"
        + "-fx-border-radius: 4px; -fx-border-color: #011E3C");
    instructions.setStyle("-fx-font-size: 15px; -fx-font-style: Palatino Linotype;"
        + "-fx-font-style: BOLD");
  }


  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    NavigationController create();
  }


  @FXML
  void onToComboAction() {
    validateGoButton();

  }

  @FXML
  void onFromComboAction() {
    validateGoButton();
  }

  @FXML
  @SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops", "UseStringBufferForStringAppends"})
  void onGoButtonAction() throws IOException {

    instructionPane.setVisible(true);
    instructions.setVisible(true);

    if (Objects.isNull(toComboBox.getNodeValue())
        || Objects.isNull(fromComboBox.getNodeValue())) {
      DialogHelper.showInformationAlert("Must Select Valid Start/End Destinations",
          "Please select valid start and end destinations to generate a valid path.");
      return;
    }

    if (toComboBox.getNodeValue().equals(fromComboBox.getNodeValue())) {
      DialogHelper.showInformationAlert("Must Select Different Start/End Destinations",
          "Please select different start and end destinations to generate a valid path.");
      return;
    }


    IGraphSearchAlgorithm<Node> algorithm = appPreferences.getGraphSearchAlgorithm().getAlgorithm();
    MutableGraph<Node> graph = GraphBuilder.undirected().allowsSelfLoops(false).build();
    database.getAllNodes().forEach(graph::addNode);
    database.getAllEdges().forEach(e -> graph.putEdge(e.getStartNode(), e.getEndNode()));

    List<Node> path = algorithm.getPath(ImmutableGraph.copyOf(graph),
        fromComboBox.getNodeValue(),
        toComboBox.getNodeValue());

    ArrayList<String> list = stepByStep.getStepByStep(path);
    String instruction;
    StringBuilder stringBuilder = new StringBuilder();
    for (String s: list) {
      stringBuilder.append(s);
      stringBuilder.append('\n');
    }
    instruction = stringBuilder.toString();
    instructions.setText(instruction);
    map.setPath(path);
    map.zoomTo(fromComboBox.getNodeValue());
    map.drawPath();

  }

  private void validateGoButton() {
    if (fromComboBox.getValue() != null && toComboBox.getValue() != null) {
      goButton.setDisable(false);
    } else {
      goButton.setDisable(true);
    }
  }

  @FXML
  void aboutOnAction() {
    eventBus.post(new ChangeMainViewEvent(aboutControllerFactory.create()));
  }

  @FXML
  void nearestLocation(ActionEvent event) throws IOException {
    if (event.getSource() == restroomButton) {
      fromComboBox.setValue("Au Bon Pain -- FLOOR 1");
      toComboBox.setValue("Bathroom 75 Lobby -- FLOOR 1");
      onGoButtonAction();
    } else if (event.getSource() == emergencyButton) {
      fromComboBox.setValue("Au Bon Pain -- FLOOR 1");
      toComboBox.setValue("Emergency Department -- FLOOR 1");
      onGoButtonAction();
    } else if (event.getSource() == informationButton) {
      fromComboBox.setValue("Au Bon Pain -- FLOOR 1");
      toComboBox.setValue("75 Lobby Information Desk -- FLOOR 1");
      onGoButtonAction();
    } else if (event.getSource() == elevatorButton) {
      fromComboBox.setValue("Au Bon Pain -- FLOOR 1");
      toComboBox.setValue("Elevator M Floor 1 -- FLOOR 1");
      onGoButtonAction();
    }

  }
}
