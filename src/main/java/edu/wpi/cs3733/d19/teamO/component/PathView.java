package edu.wpi.cs3733.d19.teamO.component;

import java.util.logging.Logger;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;

import edu.wpi.cs3733.d19.teamO.entity.Node;

public class PathView extends MapElementGroup {

  private static final Logger logger = Logger.getLogger(PathView.class.getName());

  private final ListProperty<Node> path = new SimpleListProperty<>();

  public PathView() {
    path.addListener((observable, oldValue, newValue) -> redrawPath());
  }

  private void redrawPath() {
    getChildren().clear(); // Remove old path

    if (path.size() < 2) {
      logger.warning("Cannot draw path of less than 2 nodes");
      return;
    }

    Node previous = path.get(0);
    addElement(previous.getFloorInt(), createStartNode(previous));
    for (int i = 1; i < path.size() - 1; i++) {
      if (previous.getFloorInt() == path.get(i).getFloorInt()) {
        addElement(path.get(i).getFloorInt(), createEdge(previous, path.get(i)));
      } else {
        addElement(previous.getFloorInt(),
            createTextFloorHint(previous, "To floor " + path.get(i).getFloor()));
        addElement(path.get(i).getFloorInt(),
            createTextFloorHint(path.get(i), "From floor " + previous.getFloor()));
      }
      addElement(path.get(i).getFloorInt(), createNode(path.get(i)));
      previous = path.get(i);

    }
    if (previous.getFloorInt() == path.get(path.size() - 1).getFloorInt()) {
      addElement(path.get(path.size() - 1).getFloorInt(),
          createEdge(previous, path.get(path.size() - 1)));
    }
    previous = path.get(path.size() - 1);
    addElement(previous.getFloorInt(), createEndNode(previous));
  }

  private Shape createStartNode(final Node node) {
    Circle circle = new Circle(node.getXcoord(), node.getYcoord(), 8, Color.GREEN);
    circle.setStroke(Color.BLACK);
    circle.setUserData(node);
    return circle;
  }

  private Shape createEndNode(final Node node) {
    Rectangle rectangle = new Rectangle(node.getXcoord() - 8, node.getYcoord() - 8,
        16, 16);
    rectangle.setFill(Color.RED);
    rectangle.setStroke(Color.BLACK);
    rectangle.setUserData(node);
    return rectangle;
  }

  private Shape createNode(final Node node) {
    Circle circle = new Circle(node.getXcoord(), node.getYcoord(), 5, Color.color(0, 0.31, 0.53));
    circle.setStroke(Color.BLACK);
    circle.setUserData(node);
    return circle;
  }

  private Shape createEdge(final Node start, final Node end) {
    Line line = new Line(start.getXcoord(), start.getYcoord(),
        end.getXcoord(), end.getYcoord());
    line.setStrokeWidth(2.5);
    line.setStroke(Color.color(0.96, 0.74, 0.22));
    line.setStrokeLineCap(StrokeLineCap.ROUND);
    return line;
  }

  private Label createTextFloorHint(final Node node, final String hint) {
    Label label = new Label(hint);
    label.setTranslateX(node.getXcoord() + 8);
    label.setTranslateY(node.getYcoord() + 8);
    label.setAlignment(Pos.CENTER);
    label.setTextFill(Color.WHITE);
    label.setPadding(new Insets(0, 5, 0, 5));
    label.setBackground(new Background(new BackgroundFill(Paint.valueOf("0067B1"),
        new CornerRadii(5, 5, 5, 5, false),
        Insets.EMPTY)));
    label.setBorder(new Border(new BorderStroke(Color.rgb(1, 45, 90),
        BorderStrokeStyle.SOLID, new CornerRadii(5, 5, 5, 5, false),
        BorderWidths.DEFAULT)));
    return label;
  }

  public ObservableList<Node> getPath() {
    return path.get();
  }

  public ListProperty<Node> pathProperty() {
    return path;
  }

  public void setPath(ObservableList<Node> path) {
    this.path.set(path);
  }
}
