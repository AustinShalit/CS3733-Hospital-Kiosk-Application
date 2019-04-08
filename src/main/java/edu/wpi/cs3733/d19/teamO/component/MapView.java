package edu.wpi.cs3733.d19.teamO.component;

import java.io.IOException;
import java.util.Collection;

import javafx.animation.Interpolator;
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


public class MapView extends StackPane {
  private int level = 1;
  private int currentLevel = 1;

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
  private Button levelL2;
  @FXML
  private Button levelG;
  @FXML
  private Label coordX;
  @FXML
  private Label coordY;


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
  void initialize() {
    gesturePane.setMinScale(0.1);
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
    resetButtonBackground(99);
    levelF1.setStyle("-fx-background-color: rgba(17,0,255,0.4)");

  }

  void resetButtonBackground(int level) {
    if (level == 1) {
      levelL1.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelL2.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelF2.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelF3.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelG.setStyle("-fx-background-color: rgba(249,249,255,0)");
    } else if (level == 2) {
      levelL1.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelL2.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelF1.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelF3.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelG.setStyle("-fx-background-color: rgba(249,249,255,0)");
    } else if (level == 3) {
      levelL1.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelL2.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelF1.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelF2.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelG.setStyle("-fx-background-color: rgba(249,249,255,0)");
    } else if (level == 0) {
      levelL1.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelL2.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelF1.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelF2.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelF3.setStyle("-fx-background-color: rgba(249,249,255,0)");
    } else if (level == -1) {
      levelG.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelL2.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelF1.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelF2.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelF3.setStyle("-fx-background-color: rgba(249,249,255,0)");
    } else if (level == -2) {
      levelL1.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelG.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelF1.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelF2.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelF3.setStyle("-fx-background-color: rgba(249,249,255,0)");
    } else {
      levelL1.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelG.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelF1.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelF2.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelF3.setStyle("-fx-background-color: rgba(249,249,255,0)");
      levelL2.setStyle("-fx-background-color: rgba(249,249,255,0)");
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
      ((Button) src).setStyle("-fx-background-color:  rgba(17,0,255,0.4)"); // style button
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


  }

  @FXML
  void onMouseMove(MouseEvent e)  {
    Object src = e.getSource();
    resetButtonBackground(level);
    if (src.equals(levelF1)) {
      levelF1.setStyle("-fx-background-color:  rgba(17,0,255,0.2)");
      currentLevel = 1;
    } else if (src.equals(levelF2)) {
      levelF2.setStyle("-fx-background-color:  rgba(17,0,255,0.2)");
      currentLevel = 2;
    } else if (src.equals(levelF3) ) {
      levelF3.setStyle("-fx-background-color:  rgba(17,0,255,0.2)");
      currentLevel = 3;
    } else if (src.equals(levelL1) ) {
      levelL1.setStyle("-fx-background-color:  rgba(17,0,255,0.2)");
      currentLevel = -1;
    } else if (src.equals(levelL2) ) {
      levelL2.setStyle("-fx-background-color:  rgba(17,0,255,0.2)");
      currentLevel = -2;
    } else if (src.equals(levelG) ) {
      levelG.setStyle("-fx-background-color:  rgba(17,0,255,0.2)");
      currentLevel = 0;
    } else {
      return;
    }

<<<<<<< HEAD
=======
    if (currentLevel == level) {
      ((Button) src).setStyle("-fx-background-color:  rgba(17,0,255,0.4)"); // style button
    }
  }

>>>>>>> master
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
  public void addNodesToPane(final Collection<Node> nodes) {
    nodes.stream()
        .map(node -> new Circle(node.getXcoord(), node.getYcoord(), 5, Color.RED))
        .forEach(nodeGroup.getChildren()::add);
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

}
