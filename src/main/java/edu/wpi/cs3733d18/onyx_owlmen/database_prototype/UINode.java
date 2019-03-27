package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

public class UINode {
  Node node;

  Button editButton;
  Button deleteButton;


  /**
   * Create a UI Node which is a wrapper for Node.
   * @param node The node containing the information
   */
  public UINode(Node node) {
    this.node = node;
    this.editButton = new Button("Edit");
    this.deleteButton = new Button("Delete");
  }

  /**
   * These methods are called automatically by PropertyValueFactory
   * when populating the table with data.
   */
  public StringProperty nodeIDProperty() {
    return new SimpleStringProperty(node.nodeID);
  }

  public IntegerProperty xcoordProperty() {
    return new SimpleIntegerProperty(node.xcoord);
  }

  public IntegerProperty ycoordProperty() {
    return new SimpleIntegerProperty(node.ycoord);
  }

  public IntegerProperty floorProperty() {
    return new SimpleIntegerProperty(node.floor);
  }

  public StringProperty buildingProperty() {
    return new SimpleStringProperty(node.building);
  }

  public SimpleObjectProperty<NodeType> nodeTypeProperty() {
    return new SimpleObjectProperty<>(node.nodeType);
  }

  public StringProperty longNameProperty() {
    return new SimpleStringProperty(node.longName);
  }

  public StringProperty shortNameProperty() {
    return new SimpleStringProperty(node.shortName);
  }

  public SimpleObjectProperty<Button> editButtonProperty() {
    return new SimpleObjectProperty<>(editButton);
  }

  public SimpleObjectProperty<Button> deleteButtonProperty() {
    return new SimpleObjectProperty<>(deleteButton);
  }
}
