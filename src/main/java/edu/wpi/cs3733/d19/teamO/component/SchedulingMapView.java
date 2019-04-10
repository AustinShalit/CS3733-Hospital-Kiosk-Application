package edu.wpi.cs3733.d19.teamO.component;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

  public void redrawNodes() {
    Circle circle = new Circle(442.0, 1179.0, 5, Color.color(0, 0.31, 0.53));
    circle.setStroke(Color.BLACK);
    nodeGroup.getChildren().add(circle);

    Set<Circle> circles = new HashSet<>();

    for (Node availableNode : availableNodes) {
      circles.add(
          new Circle(
              availableNode.getXcoord(),
              availableNode.getYcoord(),
              5,
              Color.color(0, 1.0, 0)
          )
      );
    }

    for (Node unavailableNode : unavailableNodes) {
      circles.add(
          new Circle(
              unavailableNode.getXcoord(),
              unavailableNode.getYcoord(),
              5,
              Color.color(1.0, 0, 0)
          )
      );
    }

    nodeGroup.getChildren().setAll(circles);
  }
}
