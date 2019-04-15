package edu.wpi.cs3733.d19.teamO.component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

import edu.wpi.cs3733.d19.teamO.entity.Node;


public class SchedulingMapView extends MapView {

  private Set<Node> availableNodes;
  private Set<Node> unavailableNodes;

  public void setAvailableNodes(Set<Node> availableNodes) {
    this.availableNodes = availableNodes;
  }

  public void setUnavailableNodes(Set<Node> unavailableNodes) {
    this.unavailableNodes = unavailableNodes;
  }


  /**
   * The constructor for the MapView class.
   *
   * @throws IOException Throws in case of xyz.
   */
  public SchedulingMapView() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SchedulingMapView.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);
    fxmlLoader.load();
  }

  @FXML
  @Override
  void initialize() throws IOException {
    availableNodes = new HashSet<>();
    unavailableNodes = new HashSet<>();

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
    gesturePane.setFitMode(GesturePane.FitMode.COVER);
    gesturePane.setScrollBarEnabled(false);

    setMapImage(new Image(getClass().getResource("SchedulingMap.jpg").openStream()));
  }

  /**
   * Redraw the nodes on the map.
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public void redrawNodes() {
    Set<Polygon> polygons = new HashSet<>();

    for (Node availableNode : availableNodes) {
      Polygon polygon = new Polygon();
      polygon.getPoints().setAll(availableNode.getPolygonPoints());
      polygon.setFill(Color.color(0.0, 1.0, 0.0, 0.5));

      polygon.setStroke(Color.color(0.0, 0.0, 0.0));
      polygon.setStrokeWidth(3);

      polygons.add(polygon);
    }

    for (Node unavailableNode : unavailableNodes) {
      Polygon polygon = new Polygon();
      polygon.getPoints().setAll(unavailableNode.getPolygonPoints());
      polygon.setFill(Color.color(1.0, 0.0, 0.0, 0.5));

      polygon.setStroke(Color.color(0.0, 0.0, 0.0));
      polygon.setStrokeWidth(3);

      polygons.add(polygon);
    }

    nodeGroup.getChildren().setAll(polygons);
  }
}
