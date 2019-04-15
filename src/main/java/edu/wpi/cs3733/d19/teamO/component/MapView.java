package edu.wpi.cs3733.d19.teamO.component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
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

@SuppressWarnings({"PMD.TooManyFields", "PMD.ExcessiveImports", "PMD.TooManyMethods"})
public class MapView extends StackPane {

  private int level = 1;

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
  private Button levelL2;
  @FXML
  private Button levelG;
  @FXML
  Label coordX;
  @FXML
  Label coordY;

  Group startAndEndNodes = new Group();

  Group pathEdges = new Group();

  List<Node> path;

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
      coordY.setText(Double.toString((int) pointOnMap.getX()));
      coordX.setText(Double.toString((int) pointOnMap.getY()));
    });
    gesturePane.setFitMode(GesturePane.FitMode.COVER);
    gesturePane.setScrollBarEnabled(false);
    resetButtonBackground(99);
    levelF1.setStyle("-fx-background-color: rgb(1,45,90)");

    onFloorSelectAction(new ActionEvent(levelF1, levelF1));

  }

  void resetButtonBackground(int level) {
    if (level == 1) {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
    } else if (level == 2) {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
    } else if (level == 3) {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
    } else if (level == 0) {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
    } else if (level == -1) {
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
    } else if (level == -2) {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
    } else {
      levelL1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelG.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF1.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF2.setStyle("-fx-background-color: rgb(0,103,177)");
      levelF3.setStyle("-fx-background-color: rgb(0,103,177)");
      levelL2.setStyle("-fx-background-color: rgb(0,103,177)");
    }

  }


  @FXML
  @SuppressWarnings("PMD.CyclomaticComplexity")
  void onFloorSelectAction(ActionEvent e) throws IOException {

    Object src = e.getSource();

    if (src.equals(levelF1) || src.equals(levelF2) || src.equals(levelF3)
        || src.equals(levelG) || src.equals(levelL1) || src.equals(levelL2)) {
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
      filename = "02_thesecondfloor.png";
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
    }

    backgroundImage.setImage(new Image(getClass().getResource(filename).openStream()));

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
    nodeGroup.getChildren().removeAll(startAndEndNodes);
    edges.getChildren().removeAll(pathEdges);

    pathEdges = new Group();
    startAndEndNodes = new Group();

    if (path != null) {
      clearNodes();
      addNodesToPane(path.stream().filter(node -> node.getFloorInt() == level)
          .collect(Collectors.toSet()));

      Node lastNode = null;
      for (Node node : path) {
        if (lastNode != null) {
          addLine(node, lastNode, Color.BLACK, 5);
          addTextField(node, lastNode);
        } else if (node.getFloorInt() == level) {
          Circle circle = new Circle(node.getXcoord(), node.getYcoord(), 8, Color.GREEN);
          circle.setStroke(Color.BLACK);
          startAndEndNodes.getChildren().add(circle);
        }
        lastNode = node;
      }

      if (lastNode.getFloorInt() == level) {
        Rectangle rectangle = new Rectangle(lastNode.getXcoord() - 8, lastNode.getYcoord() - 8,
            16, 16);
        rectangle.setFill(Color.RED);
        rectangle.setStroke(Color.BLACK);
        startAndEndNodes.getChildren().add(rectangle);
      }


      lastNode = null;
      for (Node node : path) {
        if (lastNode != null) {
          addLine(node, lastNode, Color.color(0.96, 0.74, 0.22), 2.5);
        }
        lastNode = node;
      }
    }
    nodeGroup.getChildren().addAll(startAndEndNodes);
    edges.getChildren().addAll(pathEdges);
  }

  private void addLine(Node node, Node lastNode, Paint paint, double width) {
    if (lastNode.getFloorInt() == level && node.getFloorInt() == level) {
      Line line = new Line(node.getXcoord(), node.getYcoord(),
          lastNode.getXcoord(), lastNode.getYcoord());
      line.setStrokeWidth(width);
      line.setStroke(paint);
      line.setStrokeLineCap(StrokeLineCap.ROUND);
      pathEdges.getChildren().add(line);
    }
  }

  private void addTextField(Node node, Node lastNode) {
    if (lastNode.getFloorInt() != node.getFloorInt()) {
      Label label = null;
      if (lastNode.getFloorInt() == level) {
        label = new Label("To Floor " + node.getFloor());
        label.setTranslateX(lastNode.getXcoord() + 8);
        label.setTranslateY(lastNode.getYcoord() + 8);
      } else if (node.getFloorInt() == level) {
        label = new Label("From Floor " + lastNode.getFloor());
        label.setTranslateX(node.getXcoord() + 8);
        label.setTranslateY(node.getYcoord() + 8);
      }

      if (label != null) {
        label.setAlignment(Pos.CENTER);
        label.setPrefWidth(USE_COMPUTED_SIZE);
        label.setTextFill(Color.WHITE);
        label.setPadding(new Insets(0, 5, 0, 5));
        label.setBackground(new Background(new BackgroundFill(Paint.valueOf("0067B1"),
            new CornerRadii(5, 5, 5, 5, false),
            Insets.EMPTY)));
        label.setBorder(new Border(new BorderStroke(Color.rgb(1, 45, 90),
            BorderStrokeStyle.SOLID, new CornerRadii(5, 5, 5, 5, false),
            BorderWidths.DEFAULT)));
        nodeGroup.getChildren().add(label);
      }

    }
  }
}
