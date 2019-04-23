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
import org.controlsfx.glyphfont.Glyph;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import kotlin.Pair;

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
    "PMD.AvoidInstantiatingObjectsInLoops", "PMD.TooManyMethods", "PMD.ExcessiveImports"})

public class NavigationController implements Controller {

  @FXML
  BorderPane root;
  @FXML
  VBox sidebar;

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
  VBox instructionsContainer;
  @FXML
  JFXButton aboutButton;
  @FXML
  ScrollPane instructionPane;
  @FXML
  FlowPane buttonPane;

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
  void initialize() {
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


    root.widthProperty().addListener(
        (observable, oldValue, newValue) ->
            map.setMaxWidth(newValue.doubleValue() - sidebar.getWidth()));

    root.heightProperty().addListener(
        (observable, oldValue, newValue) -> map.setPrefHeight(newValue.doubleValue())
    );

    fromComboBox.setStyle("-fx-font-size: 12px; -fx-font-family: Palatino Linotype;");
    toComboBox.setStyle("-fx-font-size: 12px; -fx-font-family: Palatino Linotype;");

    instructionPane.setVisible(false);

    instructionPane.setStyle("-fx-font-size: 15px; -fx-font-family: Palatino Linotype; "
        + "-fx-font-weight: BOLD");
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
  @SuppressWarnings({
      "PMD.AvoidInstantiatingObjectsInLoops",
      "UseStringBufferForStringAppends",
      "PMD.NPathComplexity"
  })
  void onGoButtonAction() throws IOException {
    instructionPane.setVisible(true);

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

    // the path
    List<Node> path = algorithm.getPath(ImmutableGraph.copyOf(graph),
        fromComboBox.getNodeValue(),
        toComboBox.getNodeValue());

    // set map
    map.setPath(path);

    // buttons for the floors traversed
    buttonPane.getChildren().clear();
    buttonPane.setVgap(7);
    buttonPane.setHgap(4);

    List<Node> floors = getFloors(path);
    for (int i = 0; i < floors.size(); i++) {
      Node node = floors.get(i);

      Button button = new JFXButton(node.getFloor());
      button.setId("map-button");
      button.setOnAction(event -> {
        try {
          map.zoomTo(node);
        } catch (IOException exception) {
          exception.printStackTrace();
        }
      });

      if (!buttonPane.getChildren().contains(button)) {
        buttonPane.getChildren().add(button);
      }

      if (i != floors.size() - 1) {
        Label label;

        try {
          Glyph arrow = new Glyph("FontAwesome", "ARROW_RIGHT");
          arrow.size(12);
          arrow = arrow.duplicate();
          arrow.setStyle("-fx-text-fill: #f6bd38; -fx-fill: #f6bd38;");
          label = new Label("", arrow);
        } catch (IllegalArgumentException iac){
            label = new Label(">");
        }

        if (!buttonPane.getChildren().contains(label)) {
          buttonPane.getChildren().add(label);
        }
      }
    }

    instructionsContainer.getChildren().setAll(new ArrayList<>());
    Label tempRef;
    for (Pair<String, Node> curPair: stepByStep.getStepByStep(path)) {
      tempRef = new Label(curPair.getFirst());
      tempRef.setPrefWidth(400);
      tempRef.setOnMouseClicked(event -> {
        if (Objects.nonNull(curPair.getSecond())) {
          try {
            map.zoomTo(curPair.getSecond());
          } catch (IOException exception) {
            exception.printStackTrace();
          }
        }
      });
      instructionsContainer.getChildren().add(tempRef);
    }

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

  /**
   * Get the a list of the floors traversed on the path.
   */
  private List<Node> getFloors(List<Node> path) {
    List<Node> floorNodes = new ArrayList<>();
    for (int i = 0; i < path.size(); i++) {
      Node currNode = path.get(i);
      Node prevNode;

      if (i > 0) {
        prevNode = path.get(i - 1);
        if (!currNode.getFloor().equals(prevNode.getFloor())) {
          floorNodes.add(currNode);
        }
      } else {
        floorNodes.add(currNode);
      }
    }

    return floorNodes;
  }
}
