package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import java.util.Locale;

public class Node {
  String nodeID;
  int xcoord;
  int ycoord;
  int floor;
  String building;
  NodeType nodeType;
  String longName;
  String shortName;

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
   * @param nodeID The unique identifier for the node
   * @param xcoord The X coordinate for the node
   * @param ycoord The Y coordinate for the node
   * @param floor The floor of the node
   * @param building The building address of the Node
   * @param nodeType The type of node
   * @param longName The name of the node
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
  }


  /**
   * These getters are called by PropertyValueFactory when populating the
   * table with data. I am looking for a more elegant solution.
   * @return
   */
  public String getNodeID() {
    return nodeID;
  }

  public int getXcoord() {
    return xcoord;
  }

  public int getYcoord() {
    return ycoord;
  }

  public int getFloor() {
    return floor;
  }

  public String getBuilding() {
    return building;
  }

  public NodeType getNodeType() {
    return nodeType;
  }

  public String getLongName() {
    return longName;
  }

  public String getShortName() {
    return shortName;
  }
}
