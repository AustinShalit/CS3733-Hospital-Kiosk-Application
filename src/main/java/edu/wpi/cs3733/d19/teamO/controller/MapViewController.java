package edu.wpi.cs3733.d19.teamO.controller;

import java.util.Collection;

import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

import edu.wpi.cs3733.d19.teamO.entity.Node;

public class MapViewController {

  @FXML
  private StackPane root;

  @FXML
  private GesturePane gesturePane;

  @FXML
  private ImageView backgroundImage;

  @FXML
  private Group nodeGroup;

  @FXML
  private Group edges;

  @FXML
  void initialize() {
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
  }

  void setMapImage(final Image image) {
    backgroundImage.setImage(image);
  }

  void addNodesToPane(final Collection<Node> nodes) {
    nodes.stream()
        .map(node -> new Circle(node.getXcoord(), node.getYcoord(), 5, Color.RED))
        .forEach(nodeGroup.getChildren()::add);
  }

}
