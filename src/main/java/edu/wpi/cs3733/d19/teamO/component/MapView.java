package edu.wpi.cs3733.d19.teamO.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;

import static java.lang.Math.abs;
import static javafx.scene.input.MouseButton.PRIMARY;
import static javafx.scene.input.MouseButton.SECONDARY;

@SuppressWarnings({"PMD.TooManyFields", "PMD.ExcessiveImports", "PMD.TooManyMethods",
    "PMD.CyclomaticComplexity", "PMD.GodClass", "PMD.AvoidDeeplyNestedIfStmts"})
public class MapView extends StackPane {
  private boolean navigation;
  private int level = 1;
  public boolean fire;

  @FXML
  GesturePane gesturePane;
  @FXML
  ImageView backgroundImage;
  @FXML
  Group nodeGroup;
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
  private Circle circle;
  @FXML
  Label coordX;
  @FXML
  Label coordY;

  private boolean rasPI;

  private final SimpleObjectProperty<Node> nodeClicked = new SimpleObjectProperty<>();
  private final SimpleObjectProperty<Node> nodeFrom = new SimpleObjectProperty<>();
  private final SimpleObjectProperty<Node> nodeTo = new SimpleObjectProperty<>();
  private Collection<Node> nodes;
  private final ContextMenu contextMenu = new ContextMenu();
  MenuItem fromHere = new MenuItem("Directions from here");
  MenuItem toHere = new MenuItem("Directions to here");


  Group startAndEndNodes = new Group();

  Group pathEdges = new Group();

  Group labelsGroup = new Group();

  Group buttonsGroup = new Group();

  List<Node> path;

  List<Timeline> antz = new ArrayList<>();

  public boolean isRasPI() {
    return rasPI;
  }

  /**
   * Sets the rasPI boolean and enables/disables buttons according to the set value.
   * @param rasPI The value to set rasPI to.
   * @throws IOException if onFloorSelectAction() throws an IOException
   */
  public void setRasPI(boolean rasPI) throws IOException {
    this.rasPI = rasPI;

    if (rasPI) {
      levelL2.setDisable(true);
      levelG.setDisable(true);
      levelF2.setDisable(true);
      levelF3.setDisable(true);
      levelF4.setDisable(true);

      onFloorSelectAction(new ActionEvent(levelF1, levelF1));
    } else {
      levelL2.setDisable(false);
      levelG.setDisable(false);
      levelF2.setDisable(false);
      levelF3.setDisable(false);
      levelF4.setDisable(false);

      onFloorSelectAction(new ActionEvent(levelF1, levelF1));
    }

  }

  public void setNodes(Collection<Node> nodes) {
    this.nodes = nodes;
  }

  public void setNavigation(boolean navigation) {
    this.navigation = navigation;
  }

  public Node getNodeClicked() {
    return nodeClicked.get();
  }

  public SimpleObjectProperty<Node> nodeClickedProperty() {
    return nodeClicked;
  }

  public void setNodeClicked(Node selectedNode) {
    this.nodeClicked.set(selectedNode);
  }

  public Node getNodeFrom() {
    return nodeFrom.get();
  }

  public SimpleObjectProperty<Node> nodeFromProperty() {
    return nodeFrom;
  }

  public void setNodeFrom(Node nodeFrom) {
    this.nodeFrom.set(nodeFrom);
  }

  public Node getNodeTo() {
    return nodeTo.get();
  }

  public SimpleObjectProperty<Node> nodeToProperty() {
    return nodeTo;
  }

  public void setNodeTo(Node nodeTo) {
    this.nodeTo.set(nodeTo);
  }

  public void setPath(List<Node> path) {
    this.path = path;
  }

  /**
   * The constructor for the MapView class.
   *
   * @throws IOException Throws in case of xyz.
   */
  public MapView() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MapView.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);
    fxmlLoader.load();
  }


  @FXML
  @SuppressWarnings({"PMD.NPathComplexity"})
  void initialize() throws IOException {
    contextMenu.getItems().addAll(fromHere, toHere);
    fromHere.setOnAction(a -> {
      for (Node n : nodes) {
        if (n.getFloorInt() == level && n.getXcoord() == circle.getCenterX()
            && n.getYcoord() == circle.getCenterY()
            && !n.getNodeType().equals(Node.NodeType.HALL)) {
          nodeFrom.set(n);
        }
      }
    });
    toHere.setOnAction(a -> {
      for (Node n : nodes) {
        if (n.getFloorInt() == level && n.getXcoord() == circle.getCenterX()
            && n.getYcoord() == circle.getCenterY()
            && !n.getNodeType().equals(Node.NodeType.HALL)) {
          nodeTo.set(n);
        }
      }
    });
    gesturePane.setMinScale(0.1);
    gesturePane.reset();

    gesturePane.setOnScroll(s -> {
      zoomLabel();
    });


    gesturePane.setOnMouseMoved(e -> {
      if (navigation) {
        Point2D pointOnMap = gesturePane.targetPointAt(new Point2D(e.getX(), e.getY()))
            .orElse(gesturePane.targetPointAtViewportCentre());

        int currentX = (int) pointOnMap.getX();
        int currentY = (int) pointOnMap.getY();
        double min = 9999;
        double distance = 0;
        for (Node n : nodes) {
          distance = Math.sqrt(abs(n.getXcoord() - currentX) * abs(n.getXcoord() - currentX)
              + abs(n.getYcoord() - currentY) * abs(n.getYcoord() - currentY));
          if (n.getFloorInt() == level && distance < min
              && !n.getNodeType().equals(Node.NodeType.HALL)) {
            circle.setCenterX(n.getXcoord());
            circle.setCenterY(n.getYcoord());
            min = distance;
          }
        }
        circle.setVisible(true);
      }
    });

    gesturePane.setOnMouseClicked(e -> {
      if (e.getButton().equals(PRIMARY)) {
        contextMenu.hide();
        if (navigation) {
          Point2D pointOnMap = gesturePane.targetPointAt(new Point2D(e.getX(), e.getY()))
              .orElse(gesturePane.targetPointAtViewportCentre());
          zoomLabel();
          if (e.getEventType() != MouseEvent.DRAG_DETECTED
              && e.isStillSincePress()) {
            int currentX = (int) pointOnMap.getX();
            int currentY = (int) pointOnMap.getY();
            double min = 9999;
            double distance = 0;
            for (Node n : nodes) {
              distance = Math.sqrt(abs(n.getXcoord() - currentX) * abs(n.getXcoord() - currentX)
                  + abs(n.getYcoord() - currentY) * abs(n.getYcoord() - currentY));
              if (n.getFloorInt() == level && distance < min
                  && !n.getNodeType().equals(Node.NodeType.HALL)) {
                nodeClicked.set(n);
                circle.setCenterX(n.getXcoord());
                circle.setCenterY(n.getYcoord());
                min = distance;
              }
            }
            circle.setVisible(true);
          }
          coordY.setText(Double.toString((int) pointOnMap.getX()));
          coordX.setText(Double.toString((int) pointOnMap.getY()));
        }
      } else if (e.getButton().equals(SECONDARY)) {
        contextMenu.show(gesturePane, e.getScreenX(), e.getScreenY());
      }
    });

    gesturePane.setFitMode(GesturePane.FitMode.COVER);
    gesturePane.setScrollBarEnabled(false);
    resetButtonBackground(99);
    levelF1.setStyle("-fx-background-color: rgb(1,45,90)");
    onFloorSelectAction(new ActionEvent(levelF1, levelF1));

  }

  void resetButtonBackground(int level) {
    if (fire) {
      zoomToCoord(1676, 1277);
    }
    if (level == 1) {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF4.setStyle("-fx-background-color: rgb(0,103,177)");
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
    } else if (level == 2) {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF4.setStyle("-fx-background-color: rgb(0,103,177)");
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
    } else if (level == 3) {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF4.setStyle("-fx-background-color: rgb(0,103,177)");
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
    } else if (level == 4) {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
    } else if (level == 0) {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF4.setStyle("-fx-background-color: rgb(0,103,177)");
    } else if (level == -1) {
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF4.setStyle("-fx-background-color: rgb(0,103,177)");
    } else if (level == -2) {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF4.setStyle("-fx-background-color: rgb(0,103,177)");
    } else {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF4.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
    }

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
      ((Button) src).setStyle("-fx-background-color: rgb(1,45,90)"); // style button
    } else {
      // If the src of this ActionEvent is from an unsupported button, return.
      System.out.println(e.getSource());
      return;
    }

    String filename = "";

    if (src.equals(levelF1)) {
      filename = "01_thefirstfloor.png";
      level = 1;
    } else if (src.equals(levelF2)) {
      filename = "02_thesecondfloor_withworkspace.png";
      level = 2;
    } else if (src.equals(levelF3)) {
      filename = "03_thethirdfloor.png";
      level = 3;
    } else if (src.equals(levelL1)) {
      filename = "00_thelowerlevel1.png";
      level = -1;
    } else if (src.equals(levelL2)) {
      filename = "00_thelowerlevel2.png";
      level = -2;
    } else if (src.equals(levelG)) {
      filename = "00_thegroundfloor.png";
      level = 0;
    } else if (src.equals(levelF4)) {
      filename = "04_thefourthfloor.png";
      level = 4;
    }

    backgroundImage.setImage(new Image(getClass().getResource(filename).openStream()));
    resetButtonBackground(level);
    circle.setVisible(false);
    drawPath();

  }

  @FXML
  void onMouseMove(MouseEvent e) {
    Object src = e.getSource();
    resetButtonBackground(level);
    int currentLevel = 1;
    if (src.equals(levelF1)) {
      levelF1.setStyle("-fx-background-color: rgb(0,78,134)");
      currentLevel = 1;
    } else if (src.equals(levelF2)) {
      levelF2.setStyle("-fx-background-color: rgb(0,78,134)");
      currentLevel = 2;
    } else if (src.equals(levelF3)) {
      levelF3.setStyle("-fx-background-color: rgb(0,78,134)");
      currentLevel = 3;
    } else if (src.equals(levelL1)) {
      levelL1.setStyle("-fx-background-color: rgb(0,78,134)");
      currentLevel = -1;
    } else if (src.equals(levelL2)) {
      levelL2.setStyle("-fx-background-color: rgb(0,78,134)");
      currentLevel = -2;
    } else if (src.equals(levelG)) {
      levelG.setStyle("-fx-background-color: rgb(0,78,134)");
      currentLevel = 0;
    } else if (src.equals(levelF4)) {
      levelF4.setStyle("-fx-background-color: rgb(0,78,134)");
      currentLevel = 4;
    } else {
      return;
    }

    if (currentLevel == level) {
      ((Button) src).setStyle("-fx-background-color: rgb(1,45,90)"); // style button
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
   * @param nodes nodes are the for positions
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public void addNodesToPane(final Collection<Node> nodes) {

    for (Node node : nodes) {
      Circle circle = new Circle(node.getXcoord(), node.getYcoord(), 5, Color.color(0, 0.31, 0.53));
      circle.setStroke(Color.BLACK);
      nodeGroup.getChildren().add(circle);
    }
  }


  public void clearNodes() {
    nodeGroup.getChildren().clear();
  }


  public void setEdges(Group edges) {
    this.edges = edges;
  }

  /**
   * Get the edges field of this MapView.
   *
   * @return The edges Group field of this MapView.
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
    for (Edge edge : edgeCollection) {
      Line line = new Line(edge.getStartNode().getXcoord(), edge.getStartNode().getYcoord(),
          edge.getEndNode().getXcoord(), edge.getEndNode().getYcoord());
      edges.getChildren().add(line);
    }
  }

  public void clearEdges() {
    edges.getChildren().clear();
  }

  /**
   * Deletes and redraws (if applicable) the Navigation path
   * given to the map based on the current floor.
   */
  @SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops"})
  public void drawPath() {
    nodeGroup.getChildren().removeAll(labelsGroup);
    nodeGroup.getChildren().removeAll(startAndEndNodes);
    nodeGroup.getChildren().removeAll(pathEdges);
    nodeGroup.getChildren().removeAll(buttonsGroup);

    pathEdges = new Group();
    startAndEndNodes = new Group();
    labelsGroup = new Group();
    buttonsGroup = new Group();

    if (path != null) {
      clearNodes();
      addNodesToPane(path.stream().filter(node -> node.getFloorInt() == level)
          .collect(Collectors.toSet()));

      Node lastNode = null;
      for (Node node : path) {
        if (lastNode != null) {
          addLine(node, lastNode, Color.valueOf("#012D5A"), 14);
          addFloorChangeLabel(node, lastNode);
        } else if (node.getFloorInt() == level) {
          Circle circle = new Circle(node.getXcoord(), node.getYcoord(), 8, Color.GREEN);
          circle.setStroke(Color.BLACK);
          startAndEndNodes.getChildren().add(circle);

          Label label = new Label("From " + node.getLongName());
          label.setTranslateX(node.getXcoord() + 10);
          label.setTranslateY(node.getYcoord() - 9);
          label.getStyleClass().add("navlabel");

          labelsGroup.getChildren().add(label);
        }
        lastNode = node;
      }

      if (lastNode.getFloorInt() == level) {
        Rectangle rectangle = new Rectangle(lastNode.getXcoord() - 8, lastNode.getYcoord() - 8,
            16, 16);
        rectangle.setFill(Color.RED);
        rectangle.setStroke(Color.BLACK);
        startAndEndNodes.getChildren().add(rectangle);
        if (lastNode.getFloorInt() == path.get(path.indexOf(lastNode) - 1).getFloorInt()) {
          Label label2 = new Label("To " + lastNode.getLongName());
          label2.setTranslateX(lastNode.getXcoord() + 10);
          label2.setTranslateY(lastNode.getYcoord() - 9);
          label2.getStyleClass().add("navlabel");

          labelsGroup.getChildren().add(label2);
        }

      }


      lastNode = null;
      //Arraylist of lines to add animation
      ArrayList<Line> antline = new ArrayList<>();
      for (Node node : path) {
        if (lastNode != null) {
          Line seg = addLine(node, lastNode, Color.color(0.96, 0.74, 0.22), 10);
          if (seg != null) {
            //If the line segment is valid, add it to the set fo lines to animate
            antline.add(seg);
          }
        }
        lastNode = node;
      }
      pixars_A_Bugs_Life(antline);
    }

    nodeGroup.getChildren().addAll(pathEdges);
    nodeGroup.getChildren().addAll(startAndEndNodes);
    nodeGroup.getChildren().addAll(labelsGroup);
    nodeGroup.getChildren().addAll(buttonsGroup);

  }

  /**
   * Takes in a set of lines drawn by the path.
   * Removes old animations and adds new ones.
   */
  public void pixars_A_Bugs_Life(List<Line> trail) {
    //Stops all old animations before they are removed
    for (Timeline tl2Remove : antz) {
      tl2Remove.stop();
    }
    //Resets the set of ant timelines
    antz = new ArrayList<>();
    for (Line segment : trail) {
      Timeline timeline = addAntimation(segment);
      antz.add(timeline);
    }
    //Plays newly added timelines
    for (Timeline tl2Play : antz) {
      tl2Play.play();
    }
  }

  /**
   * adds an antimation on the line.
   *
   * @param line line to be animated.
   * @return animated line.
   */
  public Timeline addAntimation(Line line) {
    line.getStrokeDashArray().setAll(15d, 10d, 15d, 10d);
    line.setStrokeWidth(3);

    final double maxOffset =
        line.getStrokeDashArray().stream().reduce(0d, (a, b) -> a + b);

    Timeline timeline = new Timeline(
        new KeyFrame(
            Duration.ZERO,
            new KeyValue(
                line.strokeDashOffsetProperty(),
                0,
                Interpolator.LINEAR
            )
        ),
        new KeyFrame(
            Duration.seconds(2),
            new KeyValue(
                line.strokeDashOffsetProperty(),
                maxOffset,
                Interpolator.LINEAR
            )
        )
    );
    timeline.setCycleCount(Timeline.INDEFINITE);
    return timeline;
  }

  /**
   * Zoom to the given node.
   *
   * @param n The node need to zoom to.
   * @throws IOException throw if there is error.
   */
  public void zoomTo(Node n) throws IOException {
    gesturePane.reset();
    switchFloor(n.getFloor());
    int id = path.indexOf(n);
    Node lastNodeOnFloor = n;
    for (int jj = id; jj < path.size(); jj++) {
      Node curNode = path.get(jj);
      if (curNode.getFloorInt() == level) {
        lastNodeOnFloor = path.get(jj);
      } else {
        break;
      }
    }
    panMapBetweenNodes(n, lastNodeOnFloor);
  }

  /**
   * Zoom to the given coordinates.
   *
   * @param x X Coord zooming to
   * @param y Y Coord zooming to
   */
  public void zoomToCoord(double x, double y) {
    //gesturePane.reset();
    gesturePane.zoomTo(3, new Point2D(x, y));
    gesturePane.centreOn(new Point2D(x, y));
  }

  private void switchFloor(String s) throws IOException {

    switch (s) {
      case "1":
        onFloorSelectAction(new ActionEvent(levelF1, levelF1));
        break;
      case "2":
        onFloorSelectAction(new ActionEvent(levelF2, levelF2));
        break;
      case "3":
        onFloorSelectAction(new ActionEvent(levelF3, levelF3));
        break;
      case "4":
        onFloorSelectAction(new ActionEvent(levelF4, levelF4));
        break;
      case "G":
        onFloorSelectAction(new ActionEvent(levelG, levelG));
        break;
      case "L1":
        onFloorSelectAction(new ActionEvent(levelL1, levelL1));
        break;
      case "L2":
        onFloorSelectAction(new ActionEvent(levelL2, levelL2));
        break;
      default:
        break;
    }

  }

  /**
   * Adds a line to the path edges, then returns a reference to that line.
   */
  private Line addLine(Node node, Node lastNode, Paint paint, double width) {
    Line line = null;
    if (lastNode.getFloorInt() == level && node.getFloorInt() == level) {
      line = new Line(node.getXcoord(), node.getYcoord(),
          lastNode.getXcoord(), lastNode.getYcoord());
      line.setStrokeWidth(width);
      line.setStroke(paint);
      line.setStrokeLineCap(StrokeLineCap.ROUND);
      pathEdges.getChildren().add(line);
    }
    return line;
  }

  private void addFloorChangeLabel(Node node, Node lastNode) {
    if (lastNode.getFloorInt() != node.getFloorInt()) {
      if (lastNode.getFloorInt() == level) {
        Button button = new JFXButton("To Floor " + node.getFloor());
        button.setTranslateX(lastNode.getXcoord() + 10);
        button.setTranslateY(lastNode.getYcoord() + 15);
        button.getStyleClass().add("navlabel");


        button.setOnAction(event -> {
          if (event.getSource() == button) {
            try {
              switchFloor(node.getFloor());
              // pan map
              int id = path.indexOf(node);
              Node lastNodeOnFloor = node;
              for (int jj = id; jj < path.size(); jj++) {
                Node curNode = path.get(jj);
                if (curNode.getFloorInt() == level) {
                  lastNodeOnFloor = path.get(jj);
                } else {
                  break;
                }
              }
              panMapBetweenNodes(node, lastNodeOnFloor);
              zoomLabel();
            } catch (IOException exception) {
              exception.printStackTrace();
            }
          }
        });

        buttonsGroup.getChildren().add(button);

      } else if (node.getFloorInt() == level) {
        Button button = new JFXButton("Back to Floor " + lastNode.getFloor());
        button.setTranslateX(node.getXcoord() + 10);
        button.setTranslateY(node.getYcoord() + 15);
        button.getStyleClass().add("navlabel");

        button.setOnAction(event -> {
          if (event.getSource() == button) {
            try {
              switchFloor(lastNode.getFloor());
              // pan map
              int ii = path.indexOf(lastNode);
              Node lastNodeOnFloor = lastNode;
              for (int jj = ii; jj > 0; jj--) {
                Node curNode = path.get(jj);
                if (curNode.getFloorInt() == level) {
                  lastNodeOnFloor = path.get(jj);
                } else {
                  break;
                }
              }
              panMapBetweenNodes(lastNode, lastNodeOnFloor);
              zoomLabel();
            } catch (IOException exception) {
              exception.printStackTrace();
            }
          }
        });

        buttonsGroup.getChildren().add(button);
      }
    }

  }

  private void panMapBetweenNodes(Node a, Node b) {
    int midX = (a.getXcoord() + b.getXcoord()) / 2;
    int midY = (a.getYcoord() + b.getYcoord()) / 2;
    int distanceX = abs(a.getXcoord() - b.getXcoord()) + 1;
    int distanceY = abs(a.getYcoord() - b.getYcoord()) + 1;

    if (550. / Math.max(distanceX, distanceY) < 2.5
        && 650. / Math.max(distanceX, distanceY) > 0.32) {
      gesturePane.zoomTo(550. / Math.max(distanceX, distanceY), new Point2D(midX, midY)); //650
    } else if (550. / Math.max(distanceX, distanceY) > 2.5) {
      gesturePane.zoomTo(2.5, new Point2D(midX, midY));
    } else if ( 550. / Math.max(distanceX, distanceY) < 0.32) {
      gesturePane.zoomTo(0.32, new Point2D(midX, midY));
    }
    gesturePane.centreOn(new Point2D(midX, midY));
  }

  /**
   *  Make labels zoom to same scale as gesture pane.
   */
  public void zoomLabel() {
    if (buttonsGroup.getChildren() != null && labelsGroup.getChildren() != null) {
      for (javafx.scene.Node b : buttonsGroup.getChildren()) {
        if (gesturePane.getCurrentScale() < 1.2) {
          b.setScaleX(1.2 / gesturePane.getCurrentScale());
          b.setScaleY(1.2 / gesturePane.getCurrentScale());
        }
      }
      for (javafx.scene.Node l : labelsGroup.getChildren()) {
        if (gesturePane.getCurrentScale() < 1.2) {
          l.setScaleX(1.2 / gesturePane.getCurrentScale());
          l.setScaleY(1.2 / gesturePane.getCurrentScale());
        }
      }
    }
  }

}
