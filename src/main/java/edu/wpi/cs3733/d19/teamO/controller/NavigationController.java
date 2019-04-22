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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
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
    "PMD.AvoidInstantiatingObjectsInLoops", "PMD.TooManyMethods", "PMD.ExcessiveImports"})

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
    buttonPane.setVgap(7);
    buttonPane.getChildren().clear();

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

    List<List<Node>> separatePath = separatePathByFloor(path);

    int cnt;
    for (cnt = 0; cnt < separatePath.size(); cnt++) {
      Button btn = new JFXButton(separatePath.get(cnt).get(0).getFloor());
      if (!buttonPane.getChildren().contains(btn)) {
        buttonPane.getChildren().add(btn);
        if (cnt != separatePath.size() - 1) {
          Glyph arrow = new Glyph("FontAwesome", "ARROW_RIGHT");
          arrow.size(10);
          Label label = new Label("", arrow);
          buttonPane.getChildren().add(label);
        }
      }

      EventHandler<MouseEvent> handler = event -> {
        int cnt1;
        for (cnt1 = 0; cnt1 < separatePath.size(); cnt1++) {
          if (event.getSource() == buttonPane.getChildren().get(cnt1)) {
            try {
              map.zoomTo(separatePath.get(cnt1).get(0));
            } catch (IOException exception) {
              exception.printStackTrace();
            }
          }
        }
      };
      btn.setOnMouseClicked(handler);
    }

    ArrayList<String> list = stepByStep.getStepByStep(path);
    String instruction;
    StringBuilder stringBuilder = new StringBuilder();
    for (String s: list) {
      stringBuilder.append(s);
      stringBuilder.append('\n');
    }
    instruction = stringBuilder.toString();
    instructions.setText(instruction);
    map.zoomTo(fromComboBox.getNodeValue());
    map.setPath(path);
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

  private List<List<Node>> separatePathByFloor(List<Node> path) {
    String floor;
    int cnt;

    List<List<Node>> separatePath = new ArrayList<>();
    List<Node> smallPath = new ArrayList<>();

    smallPath.add(path.get(0));

    for (cnt = 1; cnt < path.size(); cnt++) {
      floor = path.get(cnt).getFloor();
      if (floor.equals(path.get(cnt - 1).getFloor())) {
        smallPath.add(path.get(cnt));
      } else if (!floor.equals(path.get(cnt - 1).getFloor())) {
        separatePath.add(smallPath);
        smallPath = new ArrayList<>();
        smallPath.add(path.get(cnt));
      }
    }
    separatePath.add(smallPath);
    return separatePath;
  }
}
