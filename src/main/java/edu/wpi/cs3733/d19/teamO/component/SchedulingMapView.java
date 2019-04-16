package edu.wpi.cs3733.d19.teamO.component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javafx.animation.Interpolator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
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
  private final ObjectProperty<Node> selectedNode = new SimpleObjectProperty<>();

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

  public Node getSelectedNode() {
    return selectedNode.get();
  }

  public ObjectProperty<Node> selectedNodeProperty() {
    return selectedNode;
  }

  @FXML
  @Override
  void initialize() throws IOException {
    availableNodes = new HashSet<>();
    unavailableNodes = new HashSet<>();

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

    backgroundImage.setImage(new Image(getClass().getResource("SchedulingMap.jpg").openStream()));
  }

  /**
   * Redraw the nodes on the map.
   */
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public void redrawPolygons() {
    Set<Polygon> polygons = new HashSet<>();

    for (Node availableNode : availableNodes) {
      Polygon polygon = new Polygon();
      polygon.getPoints().setAll(availableNode.getPolygonPoints());
      polygon.setFill(Color.rgb(0, 255, 0, 0.5));

      polygon.setStroke(Color.rgb(0, 0, 0));
      polygon.setStrokeWidth(3);

      // hover effects
      polygon.hoverProperty().addListener(
          (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {
            if (polygon.isHover()) {
              polygon.setFill(Color.rgb(0, 180, 0, 0.5));

              // set the combobox to the selected node
              polygon.setOnMouseClicked(mouseEvent ->
                  selectedNode.setValue(availableNode)
              );

            } else {
              polygon.setFill(Color.rgb(0, 255, 0, 0.5));

            }
          });

      polygons.add(polygon);
    }

    for (Node unavailableNode : unavailableNodes) {
      Polygon polygon = new Polygon();
      polygon.getPoints().setAll(unavailableNode.getPolygonPoints());
      polygon.setFill(Color.rgb(255, 0, 0, 0.5));

      polygon.setStroke(Color.rgb(0, 0, 0));
      polygon.setStrokeWidth(3);

      polygons.add(polygon);

      // hover effects
      polygon.hoverProperty().addListener(
          (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {
            if (polygon.isHover()) {
              polygon.setFill(Color.rgb(180, 0, 0, 0.5));

              // set the combobox to the selected node
              polygon.setOnMouseClicked(mouseEvent ->
                  selectedNode.setValue(unavailableNode)
              );

            } else {
              polygon.setFill(Color.rgb(255, 0, 0, 0.5));

            }
          });
    }

    nodeGroup.getChildren().setAll(polygons);
  }

}
