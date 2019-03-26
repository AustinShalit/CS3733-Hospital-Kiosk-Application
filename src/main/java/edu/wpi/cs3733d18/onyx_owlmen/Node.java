package edu.wpi.cs3733d18.onyx_owlmen;

public class Node {
  String nodeID;
  int xcoord;
  int ycoord;
  int floor;
  String building;
  NodeType nodeType;
  String longName;
  String shortName;

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
}
