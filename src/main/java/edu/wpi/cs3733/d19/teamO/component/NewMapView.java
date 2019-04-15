package edu.wpi.cs3733.d19.teamO.component;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import javafx.animation.Interpolator;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

@SuppressWarnings("Duplicates")
public class NewMapView extends StackPane {

  private static PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");

  @FXML
  private GesturePane gesturePane;
  @FXML
  private ImageView backgroundImage;
  @FXML
  private VBox floorButtonBox;
  @FXML
  private Group pathGroup;
  @FXML
  private Label coordX;
  @FXML
  private Label coordY;

  private ObjectProperty<Floor> floor = new SimpleObjectProperty<>();
  private ListProperty<Floor> floors = new SimpleListProperty<>();

  /**
   * The constructor for the MapView class.
   *
   * @throws IOException Throws in case of xyz.
   */
  public NewMapView() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewMapView.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);
    fxmlLoader.load();
  }

  @FXML
  void initialize() {
    gesturePane.setMinScale(0.1);
    gesturePane.setOnMouseMoved(event -> {
      Point2D pointOnMap = gesturePane.targetPointAt(new Point2D(event.getX(), event.getY()))
          .orElse(gesturePane.targetPointAtViewportCentre());
      coordY.setText(Double.toString((int) pointOnMap.getX()));
      coordX.setText(Double.toString((int) pointOnMap.getY()));
    });

    gesturePane.setOnMouseClicked(e -> {
      Point2D pointOnMap = gesturePane.targetPointAt(new Point2D(e.getX(), e.getY()))
          .orElse(gesturePane.targetPointAtViewportCentre());

      if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {

        // increment of scale makes more sense exponentially instead of linearly
        gesturePane.animate(Duration.millis(200))
            .interpolateWith(Interpolator.EASE_BOTH)
            .zoomBy(gesturePane.getCurrentScale(), pointOnMap);
      }
    });
    gesturePane.setFitMode(GesturePane.FitMode.COVER);
    gesturePane.setScrollBarEnabled(false);

    floors.addListener(((observable, oldValue, newValue) -> {
      floorButtonBox.getChildren().clear();
      newValue.forEach(f -> {
        JFXButton button = new JFXButton(f.id.get());
        button.setPrefSize(55, 55);
        button.setOnAction(event -> floor.setValue(f));
        button.getStyleClass().add("map-floor-btn");
        button.setUserData(f);
        floorButtonBox.getChildren().add(button);
      });
    }));

    floor.addListener(((observable, oldValue, newValue) ->  {
      backgroundImage.setImage(newValue.mapImage.getValue());
//      if (oldValue == null) {
//        gesturePane.zoomTo(0.1, new Point2D(backgroundImage.getImage().getWidth() / 2,
//            backgroundImage.getImage().getHeight() / 2));
//      }
      floorButtonBox.getChildren().forEach(floorButton
          -> floorButton.pseudoClassStateChanged(SELECTED,
          newValue.equals(floorButton.getUserData())));
    }));
  }

  public Floor getFloor() {
    return floor.get();
  }

  public ObjectProperty<Floor> floorProperty() {
    return floor;
  }

  public void setFloor(Floor floor) {
    this.floor.set(floor);
  }

  public ObservableList<Floor> getFloors() {
    return floors.get();
  }

  public ListProperty<Floor> floorsProperty() {
    return floors;
  }

  public void setFloors(ObservableList<Floor> floors) {
    this.floors.set(floors);
  }

  public static class Floor {
    private final StringProperty id;
    private final ObjectProperty<Image> mapImage;

    public Floor(String id, Image mapImage) {
      this.id = new SimpleStringProperty(id);
      this.mapImage = new SimpleObjectProperty<>(mapImage);
    }

    public String getId() {
      return id.get();
    }

    public StringProperty idProperty() {
      return id;
    }

    public void setId(String id) {
      this.id.set(id);
    }

    public Image getMapImage() {
      return mapImage.get();
    }

    public ObjectProperty<Image> mapImageProperty() {
      return mapImage;
    }

    public void setMapImage(Image mapImage) {
      this.mapImage.set(mapImage);
    }
  }
}
