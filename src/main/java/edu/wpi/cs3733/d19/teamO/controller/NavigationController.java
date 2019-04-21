package edu.wpi.cs3733.d19.teamO.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.google.common.eventbus.EventBus;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import edu.wpi.cs3733.d19.teamO.AppPreferences;
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
  JFXComboBox<String> fromComboBox;
  @FXML
  JFXComboBox<String> toComboBox;
  @FXML
  JFXButton goButton;
  @FXML
  MapView map;
  @FXML
  Label instructions;
  @FXML
  JFXButton aboutButton;
  @FXML
  Pane buttonPane;

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

  List<String> listOfLongName = new ArrayList<>();
  List<String> listOfSortName = new ArrayList<>();
  Group btnGroup = new Group();

  @FXML
  void initialize() throws IOException {
    turnLongName();
    refreshCombobox();
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
    fromComboBox.setOnKeyReleased(ke -> {
      if (ke.getCode() == KeyCode.TAB) {
        toComboBox.requestFocus();
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
    if (Objects.isNull(searchForNode(toComboBox.getValue()))
        || Objects.isNull(searchForNode(fromComboBox.getValue()))) {
      DialogHelper.showInformationAlert("Must Select Valid Start/End Destinations",
          "Please select valid start and end destinations to generate a valid path.");
      return;
    }

    if (searchForNode(toComboBox.getValue()).equals(searchForNode(fromComboBox.getValue()))) {
      DialogHelper.showInformationAlert("Must Select Different Start/End Destinations",
          "Please select different start and end destinations to generate a valid path.");
      return;
    }


    IGraphSearchAlgorithm<Node> algorithm = appPreferences.getGraphSearchAlgorithm().getAlgorithm();
    MutableGraph<Node> graph = GraphBuilder.undirected().allowsSelfLoops(false).build();
    database.getAllNodes().forEach(graph::addNode);
    database.getAllEdges().forEach(e -> graph.putEdge(e.getStartNode(), e.getEndNode()));

    List<Node> path = algorithm.getPath(ImmutableGraph.copyOf(graph),
        searchForNode(fromComboBox.getValue()),
        searchForNode(toComboBox.getValue()));

    List<List<Node>> separatePath = separatePathByFloor(path);

    int cnt;
    for (cnt = 0; cnt < separatePath.size(); cnt++) {
      Button btn = new JFXButton(separatePath.get(cnt).get(0).getFloor());
      btnGroup.getChildren().add(cnt, btn);

      ArrayList<String> list = stepByStep.getStepByStep(separatePath.get(cnt));
      String instruction;
      StringBuilder stringBuilder = new StringBuilder();
      for (String s : list) {
        stringBuilder.append(s);
        stringBuilder.append('\n');
      }
      instruction = stringBuilder.toString();

      EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        instructions.setText(instruction);
      }
    };

      btn.setOnMouseClicked(handler);
    }
    buttonPane.getChildren().addAll(btnGroup);
    map.zoomTo(searchForNode(fromComboBox.getValue()));
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
  void refreshCombobox() {
    DialogHelper.populateComboBox2(database, fromComboBox, fuzzySearch(fromComboBox.getValue()));
    DialogHelper.populateComboBox2(database, toComboBox, fuzzySearch(toComboBox.getValue()));
    if (toComboBox.isFocused()) {
      toComboBox.show();
    } else if (fromComboBox.isFocused()) {
      fromComboBox.show();
    }
  }

  private Node searchForNode(String string) {
    for (Node n: database.getAllNodes()) {
      if (String.format("%s -- FLOOR %s", n.getLongName(), n.getFloor()).equals(string)) {
        return n;
      }
    }
    return null;
  }

  private List<String> fuzzySearch(String string) {
    if (string == null) {
      string = "";
    }

    class Pair implements Comparable<Pair> {
      String longname;
      int rating;
      String floor;

      Pair(String longname, int rating, String floor) {
        this.longname = longname;
        this.rating = rating;
        this.floor = floor;
      }

      @Override
      public int compareTo(Pair p) {
        return -1 * Integer.compare(this.rating, p.rating);
      }
    }

    ArrayList<Pair> unsorted = new ArrayList<>();
    for (Node n : database.getAllNodesByLongName()) {
      if (!"5".equals(n.getFloor())  && !n.getNodeType().equals(Node.NodeType.HALL)) {
        unsorted.add(new Pair(
            n.getLongName(),
            FuzzySearch.ratio(n.getLongName(), string),
            n.getFloor()
        ));
      }
    }

    Collections.sort(unsorted);
    ArrayList<String> sortedStrings = new ArrayList<>();
    for (Pair p : unsorted) {
      sortedStrings.add(String.format("%s -- FLOOR %s", p.longname,  p.floor));
    }
    return sortedStrings;
  }

  private void turnLongName() {
    for (Node n: database.getAllNodesByLongName()) {
      if (!"5".equals(n.getFloor()) && !Node.NodeType.HALL.equals(n.getNodeType())) {
        listOfLongName.add(n.getLongName());
        listOfSortName.add(n.getLongName());
      }
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
    String floor = path.get(0).getFloor();
    int cnt;
    List<Node> smallPath = new ArrayList<>();
    List<List<Node>> separatePath = new ArrayList<>();

    smallPath.add(path.get(0));

    if (floor != null) {
      for (cnt = 1; cnt < path.size(); cnt++) {
        floor = path.get(cnt).getFloor();
        if (floor.equals(path.get(cnt - 1))) {
          smallPath.add(path.get(cnt));
        }else {
          separatePath.add(smallPath);
          smallPath.clear();
          smallPath.add(path.get(cnt));
        }
      }
    }
    return separatePath;
  }
}
