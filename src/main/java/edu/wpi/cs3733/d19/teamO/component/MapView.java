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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;

public class MapView extends StackPane {
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
      if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
        Point2D pivotOnTarget = gesturePane.targetPointAt(new Point2D(e.getX(), e.getY()))
            .orElse(gesturePane.targetPointAtViewportCentre());
        // increment of scale makes more sense exponentially instead of linearly
        gesturePane.animate(Duration.millis(200))
            .interpolateWith(Interpolator.EASE_BOTH)
            .zoomBy(gesturePane.getCurrentScale(), pivotOnTarget);
      }
    });
    levelF1.setStyle("-fx-background-color: rgba(17,0,255,0.33)");
  }

  void resetButtonBackground() {
    levelL1.setStyle("-fx-background-color: rgba(249,249,255,0)");
    levelL2.setStyle("-fx-background-color: rgba(249,249,255,0)");
    levelF1.setStyle("-fx-background-color: rgba(249,249,255,0)");
    levelF2.setStyle("-fx-background-color: rgba(249,249,255,0)");
    levelF3.setStyle("-fx-background-color: rgba(249,249,255,0)");
    levelG.setStyle("-fx-background-color: rgba(249,249,255,0)");
  }



  @FXML
  @SuppressWarnings("PMD.CyclomaticComplexity")
  void onFloorSelectAction(ActionEvent e) throws IOException {
    Object src = e.getSource();

    if (src.equals(levelF1) || src.equals(levelF2) || src.equals(levelF3)
        || src.equals(levelG) || src.equals(levelL1) || src.equals(levelL2)) {
      resetButtonBackground();
      // If the src of this ActionEvent is from our supported buttons
      ((Button) src).setStyle("-fx-background-color:  rgba(17,0,255,0.33)"); // style button
    } else {
      // If the src of this ActionEvent is from an unsupported button, return.
      System.out.println(e.getSource());
      return;
    }

    String filename = "";

    if (src.equals(levelF1)) {
      filename = "01_thefirstfloor.png";
    } else if (src.equals(levelF2)) {
      filename = "02_thesecondfloor.png";
    } else if (src.equals(levelF3)) {
      filename = "03_thethirdfloor.png";
    } else if (src.equals(levelL1)) {
      filename = "00_thelowerlevel1.png";
    } else if (src.equals(levelL2)) {
      filename = "00_thelowerlevel2.png";
    } else if (src.equals(levelG)) {
      filename = "00_thegroundfloor.png";
    }

    if (filename.length() > 0) {
      setMapImage(
          new Image(getClass().getResource(filename).openStream())
      );
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
