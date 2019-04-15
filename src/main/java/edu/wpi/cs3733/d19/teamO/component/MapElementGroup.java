package edu.wpi.cs3733.d19.teamO.component;

import java.util.LinkedList;

import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Group;
import javafx.scene.Node;

import edu.wpi.cs3733.d19.teamO.entity.Floor;

public abstract class MapElementGroup extends Group {

  private ObjectProperty<Floor> floor = new SimpleObjectProperty<>();
  private MapProperty<Integer, ObservableList<Node>> elements = new SimpleMapProperty<>(FXCollections.observableHashMap());

  public MapElementGroup() {
    floor.addListener(((observable, oldValue, newValue) -> redraw(newValue)));
    elements.addListener((MapChangeListener<Integer, ObservableList<Node>>) change -> {
      if (change.wasAdded()) {
        change.getValueAdded().addListener((ListChangeListener<? super Node>) observable
            -> redraw());
      }
    });
  }

  private void redraw(final Floor floor) {
    getChildren().clear();
    getChildren().addAll(elements.getOrDefault(floor.getNumber(),
        FXCollections.emptyObservableList()));
  }

  private void redraw() {
    redraw(floor.get());
  }

  public void addElement(final Integer floor, final Node element) {
    elements.computeIfAbsent(floor, __
        -> FXCollections.observableList(new LinkedList<>())).add(element);
  }

  public ObservableMap<Integer, ObservableList<Node>> getElements() {
    return elements;
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

  public boolean isForFloor(final int floorNumber) {
    return floorNumber == floor.get().getNumber();
  }
}
