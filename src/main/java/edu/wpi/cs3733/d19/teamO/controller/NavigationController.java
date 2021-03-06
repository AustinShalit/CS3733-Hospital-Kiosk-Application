package edu.wpi.cs3733.d19.teamO.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPopup;

import org.apache.commons.collections.CollectionUtils;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import kotlin.Pair;

import edu.wpi.cs3733.d19.teamO.AppPreferences;
import edu.wpi.cs3733.d19.teamO.component.FuzzyWuzzyComboBox;
import edu.wpi.cs3733.d19.teamO.component.MapView;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import edu.wpi.cs3733.d19.teamO.entity.pathfinding.GraphSearchAlgorithm;
import edu.wpi.cs3733.d19.teamO.entity.pathfinding.LEDModel;
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
  JFXButton creditButton;
  @FXML
  ScrollPane instructionPane;
  @FXML
  FlowPane buttonPane;
  @FXML
  JFXButton reverseButton;
  @FXML
  JFXCheckBox rasPICheck;


  private StepByStep stepByStep;
  private LEDModel ledModel;
  private boolean rasPI = false;

  @Inject
  private AppPreferences appPreferences;
  @Inject
  private Database database;
  @Inject
  private AboutController.Factory aboutControllerFactory;
  @Inject
  private CreditController.Factory creditControllerFactory;

  private JFXPopup aboutPopup;
  private JFXPopup creditPopup;


  @FXML
  @SuppressWarnings("PMD.ExcessiveMethodLength")
  void initialize() {
    aboutPopup = new JFXPopup(aboutControllerFactory.create().root);
    aboutPopup.setOnAutoHide(
        event -> {
          ColorAdjust reset = new ColorAdjust();
          reset.setBrightness(0);
          root.setEffect(reset);
        }
    );
    Collection<Node> nodes = database.getAllNodes();
    CollectionUtils.filter(
        nodes,
        object -> ((Node) object).getNodeType() != Node.NodeType.HALL
            && !((Node) object).getFloor().equals("5")
    );

    creditPopup = new JFXPopup(creditControllerFactory.create().root);
    creditPopup.setOnAutoHide(
        event -> {
          ColorAdjust reset = new ColorAdjust();
          reset.setBrightness(0);
          root.setEffect(reset);
        });

    map.fire = false;

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
    map.nodeFromProperty().addListener((observable, oldValue, newValue)
        -> fromComboBox.setValue(String.format("%s -- FLOOR %s",
        newValue.getLongName(), newValue.getFloor())));
    map.nodeToProperty().addListener((observable, oldValue, newValue)
        -> toComboBox.setValue(String.format("%s -- FLOOR %s",
        newValue.getLongName(), newValue.getFloor())));
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

    reverseButton.disableProperty().bind(fromComboBox.valueProperty().isNull()
        .or(toComboBox.valueProperty().isNull()));

    ledModel = new LEDModel();

    rasPICheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                          Boolean newValue) {
        if (oldValue) {
          rasPI = false;

          restroomButton.setDisable(false);
          emergencyButton.setDisable(false);
          elevatorButton.setDisable(false);
          informationButton.setDisable(false);

          Collection<Node> nodes = database.getAllNodes();
          CollectionUtils.filter(
              nodes,
              object -> ((Node) object).getNodeType() != Node.NodeType.HALL
                  && !((Node) object).getFloor().equals("5")
          );

          toComboBox.setValue("");
          fromComboBox.setValue("");

          toComboBox.setNodes(nodes);
          fromComboBox.setNodes(nodes);

          toComboBox.setupAutoRefresh();
          fromComboBox.setupAutoRefresh();


          toComboBox.refresh();
          fromComboBox.refresh();

          map.setNodes(database.getAllNodes());
          try {
            map.setRasPI(false);
          } catch (IOException exception) {
            exception.printStackTrace();
          }
        } else {
          rasPI = true;

          restroomButton.setDisable(true);
          emergencyButton.setDisable(true);
          elevatorButton.setDisable(true);
          informationButton.setDisable(true);

          Collection<Node> nodes = ledModel.getAllNodes();

          toComboBox.setValue("");
          fromComboBox.setValue("");

          toComboBox.setNodes(nodes);
          fromComboBox.setNodes(nodes);

          toComboBox.setupAutoRefresh();
          fromComboBox.setupAutoRefresh();


          toComboBox.refresh();
          fromComboBox.refresh();

          map.setNodes(ledModel.getAllNodes());
          try {
            map.setRasPI(true);
          } catch (IOException exception) {
            exception.printStackTrace();
          }
        }
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
  void onAboutButtonAction() {
    ColorAdjust colorAdjust = new ColorAdjust();
    colorAdjust.setBrightness(-0.2);
    root.setEffect(colorAdjust);
    aboutPopup.show(getRoot());
    aboutPopup.setX(
        (getRoot().getScene().getWidth() - aboutPopup.getWidth()) / 2
    );
    aboutPopup.setY(
        (getRoot().getScene().getHeight() - aboutPopup.getHeight()) / 2
    );
  }

  @FXML
  void onCreditButtonAction() {
    ColorAdjust colorAdjust = new ColorAdjust();
    colorAdjust.setBrightness(-0.2);
    root.setEffect(colorAdjust);
    creditPopup.show(getRoot());
    creditPopup.setX(
        (getRoot().getScene().getWidth() - creditPopup.getWidth()) / 2
    );
    creditPopup.setY(
        (getRoot().getScene().getHeight() - creditPopup.getHeight()) / 2
    );
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
      "PMD.NPathComplexity",
      "PMD.CyclomaticComplexity"
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

    List<Node> path;

    if (rasPI) {
      ledModel.sendPathToPi(fromComboBox.getNodeValue(), toComboBox.getNodeValue());
      path = ledModel.getDisplayPath();
    } else {
      GraphSearchAlgorithm<Node> algorithm
          = appPreferences.getGraphSearchAlgorithm().getSupplier().get();
      MutableGraph<Node> graph = GraphBuilder.undirected().allowsSelfLoops(false).build();
      database.getAllNodes().forEach(graph::addNode);
      database.getAllEdges().forEach(e -> graph.putEdge(e.getStartNode(), e.getEndNode()));

      // the path
      path = algorithm.getPath(ImmutableGraph.copyOf(graph),
          fromComboBox.getNodeValue(),
          toComboBox.getNodeValue());
    }




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
          map.zoomLabel();
        } catch (IOException exception) {
          exception.printStackTrace();
        }
      });

      if (!buttonPane.getChildren().contains(button)) {
        buttonPane.getChildren().add(button);
      }

      if (i != floors.size() - 1) {
        FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.ARROW_RIGHT);
        iconView.setStyle("-fx-font-family:FontAwesome; -fx-fill: #f6bd38");

        if (!buttonPane.getChildren().contains(iconView)) {
          buttonPane.getChildren().add(iconView);
        }
      }
    }

    instructionsContainer.getChildren().setAll(new ArrayList<>());
    Label tempRef;
    for (Pair<String, Node> curPair : stepByStep.getStepByStep(path)) {
      tempRef = new Label(curPair.getFirst());
      tempRef.setPrefWidth(400);
      tempRef.setOnMouseClicked(event -> {
        if (Objects.nonNull(curPair.getSecond())) {
          try {
            map.zoomTo(curPair.getSecond());
            map.zoomLabel();
          } catch (IOException exception) {
            exception.printStackTrace();
          }
        }
      });
      instructionsContainer.getChildren().add(tempRef);
    }

    map.zoomTo(fromComboBox.getNodeValue());
    map.drawPath();
    map.zoomLabel();
  }

  private void validateGoButton() {
    if (fromComboBox.getValue() != null && toComboBox.getValue() != null) {
      goButton.setDisable(false);
    } else {
      goButton.setDisable(true);
    }
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

  @FXML
  void reverseOnAction() throws IOException {
    String flip = "";
    flip = fromComboBox.getValue();
    fromComboBox.setValue(toComboBox.getValue());
    toComboBox.setValue(flip);
    onGoButtonAction();
  }
}
