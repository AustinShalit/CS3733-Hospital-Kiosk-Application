package edu.wpi.cs3733.d19.teamO.component;

import java.io.IOException;
import java.util.Collection;



import javafx.animation.Interpolator;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;


import static java.lang.Math.abs;

@SuppressWarnings({"PMD.TooManyFields", "PMD.TooManyMethods"})
public class MapView2 extends StackPane {
  private boolean dragStatus;
  private String level = "1";
  private String currentLevel = "1";
  Collection<Node> nodes;
  Collection<Node> currentNodes;
  Collection<Edge> databaseEdge;
  Collection<Edge> currentEdges;

  public void setCurrentNodes(Collection<Node> currentNodes) {
    this.currentNodes = currentNodes;
  }

  public void setDatabaseEdge(Collection<Edge> databaseEdge) {
    this.databaseEdge = databaseEdge;
  }

  public void setCurrentEdges(Collection<Edge> currentEdges) {
    this.currentEdges = currentEdges;
  }

  @FXML
  private GesturePane gesturePane;
  @FXML
  private ImageView backgroundImage;
  @FXML
  private Group nodeGroup;
  @FXML
  private Group edges;
  @FXML
  private Button levelL1;
  @FXML
  private Button levelF1;
  @FXML
  private Button levelF2;
  @FXML
  private Button levelF3;
  @FXML
  private Button levelF4;
  @FXML
  private Button levelL2;
  @FXML
  private Button levelG;
  @FXML
  private Label coordX;
  @FXML
  private Label coordY;
  @FXML
  private Circle circle;
  @FXML
  private Circle circleDrag;
  @FXML
  private Line lineForEdge;

  private final SimpleObjectProperty<Node> selectedNode = new SimpleObjectProperty<>();
  private final SimpleObjectProperty<String> selectedEdge = new SimpleObjectProperty<>();

  /**
   * The constructor for the MapView2 class.
   *
   * @throws IOException Throws in case of xyz.
   */
  public MapView2() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MapView2.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);
    fxmlLoader.load();
  }

  public Node getSelectedNode() {
    return selectedNode.get();
  }

  public SimpleObjectProperty<Node> selectedNodeProperty() {
    return selectedNode;
  }

  public void setSelectedNode(Node selectedNode) {
    this.selectedNode.set(selectedNode);
  }

  public void setSelectedNode(String selectedEdge) {
    this.selectedEdge.set(selectedEdge);
  }

  public String getSelectedEdge() {
    return selectedEdge.get();
  }

  public SimpleObjectProperty<String> selectedEdgeProperty() {
    return selectedEdge;
  }



  @FXML
  void initialize() throws IOException {
    gesturePane.setMinScale(0.1);
    gesturePane.reset();

    gesturePane.setOnMouseClicked(e -> {
      Point2D pointOnMap = gesturePane.targetPointAt(new Point2D(e.getX(), e.getY()))
          .orElse(gesturePane.targetPointAtViewportCentre());

      if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
        // increment of scale makes more sense exponentially instead of linearly
        gesturePane.animate(Duration.millis(200))
            .interpolateWith(Interpolator.EASE_BOTH)
            .zoomBy(gesturePane.getCurrentScale(), pointOnMap);
      }
      int currentX = (int) pointOnMap.getX();
      int currentY = (int) pointOnMap.getY();
      coordX.setText(Double.toString(currentX));
      coordY.setText(Double.toString(currentY));
      circle.setCenterX(pointOnMap.getX());
      circle.setCenterY(pointOnMap.getY());

      //  Node[] nodesArray = nodes.toArray(new Node[nodes.size()]);

      if (!dragStatus) {
        circle.setVisible(true);
        Node emptyNode = new Node("Auto-Generated", currentX, currentY, level, "",
            null, "", "");
        selectedNode.set(emptyNode);
        findCurrentNode(level);
        for (Node n : currentNodes) {
          if (abs(n.getXcoord() - currentX) < 8 && abs(n.getYcoord() - currentY) < 8) {
            selectedNode.set(n);
            break;
          }
        }
        selectedEdge.set(" ");
        findCurrentEdge(level);
        lineForEdge.setVisible(false);
        for (Edge ed : currentEdges) {
          int edgeX = ed.getStartNode().getXcoord() - ed.getEndNode().getXcoord();
          int edgeY = ed.getStartNode().getYcoord() - ed.getEndNode().getYcoord();
          double kk = 0;
          double bb = 0;
          double result = 0;
          if (edgeX == 0) {
            bb = currentX;
            result = abs(currentX - bb);
          } else {
            kk = (double) edgeY / (double) edgeX;
            bb = ed.getEndNode().getYcoord() - kk * ed.getEndNode().getXcoord();
            result = abs(currentX * kk + bb - currentY);
          }

          if (result < 10
              && currentX <= Math.max(ed.getStartNode().getXcoord(), ed.getEndNode().getXcoord())
              + 2
              && currentX >= Math.min(ed.getStartNode().getXcoord(), ed.getEndNode().getXcoord())
              - 2 && currentY <= Math.max(ed.getStartNode().getYcoord(), ed.getEndNode()
              .getYcoord()) + 2
              && currentY >= Math.min(ed.getStartNode().getYcoord(),

              ed.getEndNode().getYcoord()) - 2 ) {
            selectedEdge.set(ed.getEdgeId());
            lineForEdge.setStartX(ed.getStartNode().getXcoord());
            lineForEdge.setStartY(ed.getStartNode().getYcoord());
            lineForEdge.setEndX(ed.getEndNode().getXcoord());
            lineForEdge.setEndY(ed.getEndNode().getYcoord());
            lineForEdge.setVisible(true);

            break;
          }
        }
      }
    });
    gesturePane.setFitMode(GesturePane.FitMode.COVER);
    gesturePane.setScrollBarEnabled(false);
    resetButtonBackground("99");
    levelF1.setStyle("-fx-background-color: rgb(1,45,90)");
    onFloorSelectAction(new ActionEvent(levelF1, levelF1));


  }

  void resetButtonBackground(String l) {
    if ("1".equals(level)) {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF4.setStyle("-fx-background-color: rgb(0,103,177)");
    } else if ("2".equals(level)) {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF4.setStyle("-fx-background-color: rgb(0,103,177)");
    } else if ("3".equals(level)) {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF4.setStyle("-fx-background-color: rgb(0,103,177)");
    } else if ("G".equals(level)) {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF4.setStyle("-fx-background-color: rgb(0,103,177)");
    } else if ("L1".equals(level)) {
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF4.setStyle("-fx-background-color: rgb(0,103,177)");
    } else if ("L2".equals(level)) {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF4.setStyle("-fx-background-color: rgb(0,103,177)");
    } else if ("4".equals(level)) {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
    } else {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF4.setStyle("-fx-background-color: rgb(0,103,177)");
    }

  }


  public void setNodes(Collection<Node> nodes) {
    this.nodes = nodes;
  }

  @FXML
  @SuppressWarnings("PMD.CyclomaticComplexity")
  void onFloorSelectAction(ActionEvent e) throws IOException {
    Object src = e.getSource();

    if (src.equals(levelF1) || src.equals(levelF2) || src.equals(levelF3)
        || src.equals(levelG) || src.equals(levelL1) || src.equals(levelL2)
        || src.equals(levelF4)) {
      resetButtonBackground(level);
      // If the src of this ActionEvent is from our supported buttons
      ((Button) src).setStyle("-fx-background-color:  rgb(1,45,90)"); // style button
    } else {
      // If the src of this ActionEvent is from an unsupported button, return.
      System.out.println(e.getSource());
      return;
    }

    String filename = "";

    if (src.equals(levelF1)) {
      filename = "01_thefirstfloor.png";
      circle.setVisible(false);
      level = "1";
      updateDisplay();
    } else if (src.equals(levelF2)) {
      filename = "02_thesecondfloor_withworkspace.png";
      circle.setVisible(false);
      level = "2";
      updateDisplay();
    } else if (src.equals(levelF3)) {
      filename = "03_thethirdfloor.png";
      circle.setVisible(false);
      level = "3";
      updateDisplay();
    } else if (src.equals(levelF4)) {
      filename = "04_thefourthfloor.png";
      circle.setVisible(false);
      level = "4";
      updateDisplay();
    } else if (src.equals(levelL1)) {
      filename = "00_thelowerlevel1.png";
      circle.setVisible(false);
      level = "L1";
      updateDisplay();
    } else if (src.equals(levelL2)) {
      filename = "00_thelowerlevel2.png";
      circle.setVisible(false);
      level = "L2";
      updateDisplay();
    } else if (src.equals(levelG)) {
      filename = "00_thegroundfloor.png";
      circle.setVisible(false);
      level = "G";
      updateDisplay();
    }

    backgroundImage.setImage(new Image(getClass().getResource(filename).openStream()));


  }

  @FXML
  void onMouseDrag(MouseEvent event) {
    if (dragStatus) {
      selectedNode.set(new Node(selectedNode.get().getNodeId(), (int) event.getX(),
          (int) event.getY(), level, selectedNode.get().getBuilding(),
          selectedNode.get().getNodeType(), selectedNode.get().getLongName(),
          selectedNode.get().getShortName()));
      circleDrag.setCenterX(event.getX());
      circleDrag.setCenterY(event.getY());

    }
  }

  public void setCircleDrag(int x, int y) {
    circleDrag.setCenterX(x);
    circleDrag.setCenterY(y);
  }

  public void setLineVisible(boolean b) {
    lineForEdge.setVisible(b);
  }

  public void setDragStatus(boolean status) {
    dragStatus = status;
  }

  public void setGesturePane(boolean status) {
    gesturePane.setGestureEnabled(status);
  }

  public void setScrollBar(boolean status) {
    gesturePane.setScrollBarEnabled(status);
  }

  @FXML
  void onMouseMove(MouseEvent e) {
    Object src = e.getSource();
    resetButtonBackground(level);
    if (src.equals(levelF1)) {
      levelF1.setStyle("-fx-background-color:  rgb(0,78,134)");
      currentLevel = "1";
    } else if (src.equals(levelF2)) {
      levelF2.setStyle("-fx-background-color:  rgb(0,78,134)");
      currentLevel = "2";
    } else if (src.equals(levelF3)) {
      levelF3.setStyle("-fx-background-color:  rgb(0,78,134)");
      currentLevel = "3";
    }  else if (src.equals(levelF4)) {
      levelF4.setStyle("-fx-background-color:  rgb(0,78,134)");
      currentLevel = "4";
    } else if (src.equals(levelL1)) {
      levelL1.setStyle("-fx-background-color:  rgb(0,78,134)");
      currentLevel = "Ll";
    } else if (src.equals(levelL2)) {
      levelL2.setStyle("-fx-background-color:  rgb(0,78,134)");
      currentLevel = "L2";
    } else if (src.equals(levelG)) {
      levelG.setStyle("-fx-background-color:  rgb(0,78,134)");
      currentLevel = "G";
    } else {
      return;
    }

    if (currentLevel.equals(level)) {
      ((Button) src).setStyle("-fx-background-color:  rgb(1,45,90)"); // style button
    }
  }

  /**
   * Set the image to the map.
   *
   * @param image image is the map file
   */
  public void setMapImage(final Image image) {
    backgroundImage.setImage(image);
    gesturePane.zoomTo(0.1, new Point2D(backgroundImage.getImage().getWidth() / 2,
        backgroundImage.getImage().getHeight() / 2));
  }

  /**
   * add nodes to the pane as small circles.
   *
   * @param n nodes are the for positions
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public void addNodesToPane(final Collection<Node> n) {
    if (n != null) {
      for (Node node : n) {
        Circle circle = new Circle(node.getXcoord(), node.getYcoord(), 5,
            Color.color(0, 0.31, 0.53));
        circle.setStroke(Color.BLACK);
        nodeGroup.getChildren().add(circle);
        //nodeGroup.getChildren().
      }
    }
  }


  public void clearNodes() {
    nodeGroup.getChildren().clear();
  }

  private void findCurrentNode(String lv) {
    if (currentNodes != null && nodes != null) {
      currentNodes.clear();

      for (Node n : nodes) {
        if (lv.equals(n.getFloor())) {
          currentNodes.add(n);
        }
      }
    }
  }

  private void findCurrentEdge(String lv) {
    if (currentEdges != null && databaseEdge != null) {
      currentEdges.clear();

      for (Edge e : databaseEdge) {
        if (lv.equals(e.getStartNode().getFloor())) {
          currentEdges.add(e);

        }
      }
    }
  }

  public void setEdges(Group edges) {
    this.edges = edges;
  }

  /**
   * Get the edges field of this MapView2.
   *
   * @return The edges Group field of this MapView2.
   */
  public Group getEdges() {
    return edges;
  }

  /**
   * Adds all edges to display on the map.
   *
   * @param edgeCollection The edges to be displayed.
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public void addEdgesToPane(final Collection<Edge> edgeCollection) {
    if (edgeCollection != null) {
      for (Edge edge : edgeCollection) {
        Line line = new Line(edge.getStartNode().getXcoord(), edge.getStartNode().getYcoord(),
            edge.getEndNode().getXcoord(), edge.getEndNode().getYcoord());
        line.setStrokeWidth(4);
        edges.getChildren().add(line);
      }
    }
  }

  public void clearEdges() {
    edges.getChildren().clear();
  }

  public String getLevel() {
    return level;
  }

  public void setCircleVisibility(boolean visibility) {
    circle.setVisible(visibility);
  }

  public void setCircleDragVisibility(boolean visibility) {
    circleDrag.setVisible(visibility);
  }

  private void updateDisplay() {
    clearNodes();
    findCurrentNode(level);
    addNodesToPane(currentNodes);
    clearEdges();
    findCurrentEdge(level);
    addEdgesToPane(currentEdges);
  }
}
