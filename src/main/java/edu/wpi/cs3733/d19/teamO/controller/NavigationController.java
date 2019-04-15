package edu.wpi.cs3733.d19.teamO.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.AppPreferences;
import edu.wpi.cs3733.d19.teamO.component.MapView;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import edu.wpi.cs3733.d19.teamO.entity.pathfinding.IGraphSearchAlgorithm;
import edu.wpi.cs3733.d19.teamO.entity.pathfinding.StepByStep;

@FxmlController(url = "Navigation.fxml")
@SuppressWarnings({"PMD.TooManyFields", "PMD.RedundantFieldInitializer"})

public class NavigationController implements Controller {


  @FXML
  BorderPane root;

  @FXML
  JFXButton restroomButton;
  @FXML
  JFXButton walkwayButton;
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
  @FXML
  Label instructions;

  StepByStep stepByStep;
  boolean addRest = false;
  boolean addWalk = false;
  boolean addExit = false;
  boolean addInfo = false;


  @Inject
  private AppPreferences appPreferences;
  @Inject
  private Database database;

  List<Node.NodeType> filteredNodeTypes;

  @FXML
  void initialize() throws IOException {
    filteredNodeTypes = new ArrayList<>(Arrays.asList(Node.NodeType.values()));
    refreshCombobox();
    stepByStep = new StepByStep();
    validateGoButton();
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
  @SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops", "UseStringBufferForStringAppends"})
  void onGoButtonAction() {

    if (toComboBox.getValue().equals(fromComboBox.getValue())) {
      DialogHelper.showInformationAlert("Must Select Different Start/End Destinations",
          "Please select different start and end destinations to generate a valid path.");
      return;
    }

    IGraphSearchAlgorithm<Node> algorithm = appPreferences.getGraphSearchAlgorithm().getAlgorithm();
    MutableGraph<Node> graph = GraphBuilder.undirected().allowsSelfLoops(false).build();
    database.getAllNodes().forEach(graph::addNode);
    database.getAllEdges().forEach(e -> graph.putEdge(e.getStartNode(), e.getEndNode()));

    List<Node> path = algorithm.getPath(ImmutableGraph.copyOf(graph),
        fromComboBox.getValue(),
        toComboBox.getValue());

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
    map.drawPath();
  }

  void validateGoButton() {
    if (fromComboBox.getValue() != null && toComboBox.getValue() != null) {
      goButton.setDisable(false);
    } else {
      goButton.setDisable(true);
    }
  }

  @FXML
  void setRestroomButton() {
    if (addRest) {
      filteredNodeTypes.add(Node.NodeType.REST);
      restroomButton.setStyle("-fx-background-color: #0067B1");
      refreshCombobox();
      addRest = false;
    } else {
      filteredNodeTypes.remove(Node.NodeType.REST);
      refreshCombobox();
      restroomButton.setStyle("-fx-background-color: #012d5a");
      addRest = true;
    }
  }

  @FXML
  void setWalkwayButton() {
    if (addWalk) {
      filteredNodeTypes.add(Node.NodeType.ELEV);
      filteredNodeTypes.add(Node.NodeType.STAI);
      filteredNodeTypes.add(Node.NodeType.HALL);
      walkwayButton.setStyle("-fx-background-color: #0067B1");
      refreshCombobox();
      addWalk = false;
    } else {
      filteredNodeTypes.remove(Node.NodeType.ELEV);
      filteredNodeTypes.remove(Node.NodeType.STAI);
      filteredNodeTypes.remove(Node.NodeType.HALL);
      refreshCombobox();
      walkwayButton.setStyle("-fx-background-color: #012d5a");
      addWalk = true;
    }
  }

  @FXML
  void setExitButton() {
    if (addExit) {
      filteredNodeTypes.add(Node.NodeType.EXIT);
      exitButton.setStyle("-fx-background-color: #0067B1");
      refreshCombobox();
      addExit = false;
    } else {
      filteredNodeTypes.remove(Node.NodeType.EXIT);
      refreshCombobox();
      exitButton.setStyle("-fx-background-color: #012d5a");
      addExit = true;
    }
  }

  @FXML
  void setInformationButton() {
    if (addInfo) {
      filteredNodeTypes.add(Node.NodeType.INFO);
      informationButton.setStyle("-fx-background-color: #0067B1");
      refreshCombobox();
      addInfo = false;
    } else {
      filteredNodeTypes.remove(Node.NodeType.INFO);
      refreshCombobox();
      informationButton.setStyle("-fx-background-color: #012d5a");
      addInfo = true;
    }
  }

  void refreshCombobox() {
    DialogHelper.populateComboBox(database, fromComboBox, filteredNodeTypes);
    DialogHelper.populateComboBox(database, toComboBox, filteredNodeTypes);
  }
}
