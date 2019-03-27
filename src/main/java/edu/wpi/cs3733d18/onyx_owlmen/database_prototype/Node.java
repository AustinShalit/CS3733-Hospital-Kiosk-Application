package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import java.util.Locale;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

public class Node {
  String nodeID;
  int xcoord;
  int ycoord;
  int floor;
  String building;
  NodeType nodeType;
  String longName;
  String shortName;

  Button editButton; // ????
  Button deleteButton;

  static Node parseNode(String str) {
    String[] fields = str.split(",");
    assert fields.length == 8;

    NodeType nodeType = null;
    switch (str.toLowerCase(Locale.ENGLISH)) {
      case "conf":
        nodeType = NodeType.CONF;
        break;
      case "hall":
        nodeType = NodeType.HALL;
        break;
      case "dept":
        nodeType = NodeType.DEPT;
        break;
      case "info":
        nodeType = NodeType.INFO;
        break;
      case "labs":
        nodeType = NodeType.LABS;
        break;
      case "rest":
        nodeType = NodeType.REST;
        break;
      case "serv":
        nodeType = NodeType.SERV;
        break;
      case "stai":
        nodeType = NodeType.STAI;
        break;
      default:
        break;
    }
    assert nodeType != null;

    return new Node(
        fields[0],
        Integer.parseInt(fields[1]),
        Integer.parseInt(fields[2]),
        Integer.parseInt(fields[3]),
        fields[4],
        nodeType,
        fields[6],
        fields[7]
    );
  }

  /**
   * Constructor for the node class.
   *
   * @param nodeID    The unique identifier for the node
   * @param xcoord    The X coordinate for the node
   * @param ycoord    The Y coordinate for the node
   * @param floor     The floor of the node
   * @param building  The building address of the Node
   * @param nodeType  The type of node
   * @param longName  The name of the node
   * @param shortName A shorter name of the node
   */
  public Node(String nodeID,
              int xcoord,
              int ycoord,
              int floor,
              String building,
              NodeType nodeType,
              String longName,
              String shortName) {
    this.nodeID = nodeID;
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    this.floor = floor;
    this.building = building;
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;
    this.editButton = new Button("Edit");
    this.deleteButton = new Button("Delete");

  }


  /**
   * These methods are called automatically by PropertyValueFactory
   * when populating the table with data.
   */
  public StringProperty nodeIDProperty() {
    return new SimpleStringProperty(nodeID);
  }

  public IntegerProperty xcoordProperty() {
    return new SimpleIntegerProperty(xcoord);
  }

  public IntegerProperty ycoordProperty() {
    return new SimpleIntegerProperty(ycoord);
  }

  public IntegerProperty floorProperty() {
    return new SimpleIntegerProperty(floor);
  }

  public StringProperty buildingProperty() {
    return new SimpleStringProperty(building);
  }

  public SimpleObjectProperty<NodeType> nodeTypeProperty() {
    return new SimpleObjectProperty<>(nodeType);
  }

  public StringProperty longNameProperty() {
    return new SimpleStringProperty(longName);
  }

  public StringProperty shortNameProperty() {
    return new SimpleStringProperty(shortName);
  }

  public SimpleObjectProperty<Button> editButtonProperty() {
    return new SimpleObjectProperty<>(editButton);
  }

  public SimpleObjectProperty<Button> deleteButtonProperty() {
    return new SimpleObjectProperty<>(deleteButton);
  }
}
