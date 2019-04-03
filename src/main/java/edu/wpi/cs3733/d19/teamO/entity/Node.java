package edu.wpi.cs3733.d19.teamO.entity;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.MoreObjects;

public class Node implements Comparator<Node> {

  public enum NodeType {
    CONF("Conference"),
    ELEV("Elevator"),
    EXIT("Exit"),
    HALL("Hall"),
    DEPT("Department"),
    INFO("Information"),
    LABS("Lab"),
    REST("Restroom"),
    RETL("Retail"),
    SERV("Service"),
    STAI("Stair Case");

    private static final Map<String, NodeType> lookup = new ConcurrentHashMap<>();

    static {
      for (NodeType type : values()) {
        lookup.put(type.name(), type);
      }
    }

    private final String name;

    NodeType(final String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }

    /**
     * Get the NodeType for the given string.
     */
    public static NodeType get(final String name) {
      NodeType type = lookup.get(name);
      if (type == null) {
        throw new IllegalArgumentException("Unknown node type: " + name);
      }
      return type;
    }
  }

  private final String nodeId;
  private final int xcoord;
  private final int ycoord;
  private final int floor;
  private final String building;
  private final NodeType nodeType;
  private final String longName;
  private final String shortName;

  /**
   * Create a node.
   */
  public Node(final String nodeId, final int xcoord, final int ycoord, final int floor,
              final String building, final NodeType nodeType, final String longName,
              final String shortName) {
    this.nodeId = nodeId;
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    this.floor = floor;
    this.building = building;
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;
  }

  public String getNodeId() {
    return nodeId;
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

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("nodeId", nodeId)
        .add("xcoord", xcoord)
        .add("ycoord", ycoord)
        .add("floor", floor)
        .add("building", building)
        .add("nodeType", nodeType)
        .add("longName", longName)
        .add("shortName", shortName)
        .toString();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Node node = (Node) o;
    return xcoord == node.xcoord
        && ycoord == node.ycoord
        && floor == node.floor
        && nodeId.equals(node.nodeId)
        && building.equals(node.building)
        && nodeType == node.nodeType
        && longName.equals(node.longName)
        && shortName.equals(node.shortName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeId, xcoord, ycoord, floor, building, nodeType, longName, shortName);
  }

  @Override
  public int compare(Node node1, Node node2) {
    if (getCost(node1) < getCost(node2)) {
      return -1;
    }
    if (getCost(node1) > getCost(node2)) {
      return 1;
    }
    return 0;
  }


  public double getCost(Node node) {
    double dist = Math.sqrt(Math.pow(this.xcoord - node.xcoord, 2) + Math.pow(this.ycoord - node.ycoord, 2));
    return dist;
  }

}
