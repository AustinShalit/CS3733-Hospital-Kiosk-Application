package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import sun.net.NetworkClient;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.first.networktables.NetworkTableInstance;

public class LED_Model {
  private Node endHall;
  private Node elevatorHallDoor;
  private Node topElevator;
  private Node topExit;
  private Node topStair;
  private Node bathroom;
  private Node hallToRooms;
  private Node hallNearRooms;
  private Node shapiro;
  private Node zinner;
  private Node lowerElevator;
  private Node bottomStair;
  private Node bottomElevator;
  private Node bottomExit;
  private Edge longHall;
  private Edge toTopElev;
  private Edge toTopElevHall;
  private Edge toTopStair;
  private Edge toBathroom;
  private Edge toRoomHall;
  private Edge toNearRooms;
  private Edge toShapiro;
  private Edge toZinner;
  private Edge toLowerElev;
  private Edge stairs;
  private Edge elevator;
  private Edge toBottomElev;
  private Edge toBottomStair;
  private Edge toBottomExit;
  private ArrayList<Node> nodes;
  private ArrayList<Edge> allEdges;

  public LED_Model() {
    this.endHall = new Node("GHALL02801", 1270, 1830, "1",
        "Shapiro", Node.NodeType.HALL, "Hallway MapNode 28 Floor 1", "Hall");
    this.elevatorHallDoor = new Node("GHALL02101", 1600, 1830, "1",
        "Shapiro", Node.NodeType.HALL, "Hallway MapNode 21 Floor 1", "Hall");
    this.topElevator = new Node("GELEV00N01", 1647, 1930, "1",
        "Shapiro", Node.NodeType.ELEV, "Elevator MapNode 26 Floor 1", "Hall");
    this.topExit = new Node("GEXIT00101", 1751, 1825, "1",
        "Shapiro", Node.NodeType.EXIT, "Francis Street Exit Floor 1", "Francis Street Exit MapNode 1 Floor 1");
    this.topStair = new Node("GSTAIR01301", 1750, 2014, "1",
        "Shapiro", Node.NodeType.STAI, "Stair Case MapNode Floor 1", "Southbound Stairs Floor 1");
    this.bathroom = new Node("GREST01201", 1671, 2015, "1",
        "Shapiro", Node.NodeType.REST, "Bathroom MapNode Floor 1", "Restroom Shapiro Floor 1");
    this.hallToRooms = new Node("GHALL00701", 1708, 2092, "1",
        "Shapiro", Node.NodeType.HALL, "Hallway MapNode 7 Floor 1", "Hall");
    this.shapiro = new Node("GCONF02001", 1493, 2117, "1",
        "Shapiro", Node.NodeType.CONF, "Shapiro Board Room MapNode 20 Floor 1", "Shapiro Board Room");
    this.hallNearRooms = new Node("GHALL01701", 1605, 2093, "1",
        "Shapiro", Node.NodeType.HALL, "Hallway MapNode 17 Floor 1", "Hall");
    this.zinner = new Node("GDEPT01901", 1566, 2139, "1",
        "Shapiro", Node.NodeType.DEPT, "Zinner Breakout Room MapNode 19 Floor 1", "Hall");
    this.lowerElevator = new Node("GELEV00Q01", 1648, 2161, "1",
        "Shapiro", Node.NodeType.ELEV, "Elevator Q MapNode 18 Floor 1", "Elevator Q Floor 1");
    this.bottomStair = new Node("GSTAI008L1", 1720, 2132, "L1",
        "Shapiro", Node.NodeType.STAI, "Stairs MapNode 8 Floor L1", "L1 Stairs");
    this.bottomElevator = new Node("GELEV00QL1", 1270, 1830, "L1",
        "Shapiro", Node.NodeType.HALL, "Elevator Q MapNode 7 Floor L1", "Elevator Q L1");
    this.bottomExit = new Node("GEXIT001L1", 1702, 2260, "L1",
        "Shapiro", Node.NodeType.EXIT, "Fenwood Road Exit MapNode 1 Floor L1", "Fenwood Raod Exit Floor L1");

    this.longHall = new Edge("longHall", endHall, topExit);
    this.toTopElev = new Edge("toTopElev", elevatorHallDoor, topElevator);
    this.toTopElevHall = new Edge("toTopElevHall", elevatorHallDoor, topExit);
    this.toTopStair = new Edge("toTopStair", topExit, topStair);
    this.toBathroom = new Edge("toBathroom", topStair, bathroom);
    this.toRoomHall = new Edge("toRoomHall", topStair, hallToRooms);
    this.toNearRooms = new Edge("toNearRooms", hallToRooms, hallNearRooms);
    this.toShapiro = new Edge("toShapiro", hallNearRooms, shapiro);
    this.toZinner = new Edge("toZinner", hallNearRooms, zinner);
    this.toLowerElev = new Edge("toLowerElev", hallToRooms, lowerElevator);
    this.stairs = new Edge("stairs", topStair, bottomStair);
    this.elevator = new Edge("elevator", topElevator, bottomElevator);
    this.toBottomElev = new Edge("toBottomElev", bottomElevator, bottomStair);
    this.toBottomExit = new Edge("toBottomExit", bottomStair, bottomExit);

    allEdges.add(longHall);
    allEdges.add(toTopElev);
    allEdges.add(toTopElevHall);
    allEdges.add(toTopStair);
    allEdges.add(toBathroom);
    allEdges.add(toRoomHall);
    allEdges.add(toNearRooms);
    allEdges.add(toShapiro);
    allEdges.add(toZinner);
    allEdges.add(toLowerElev);
    allEdges.add(stairs);
    allEdges.add(elevator);
    allEdges.add(toBottomElev);
    allEdges.add(toBottomExit);

    NetworkTableInstance instance = NetworkTableInstance.getDefault();
    instance.startClient("130.215.173.94"); // ip address for wpi 130.215.173.94


  }

  public void sendPathToPi(Node startNode, Node endNode) {



    ArrayList<Node> path = new ArrayList<>();

    double [] pins = getListPins(path);

    NetworkTableInstance.getDefault().getEntry("/path").setDoubleArray(pins);





  }



  public double[] getListPins(List<Node> nodes) {
    ArrayList<Double> pins = new ArrayList<>();

    ArrayList<Edge> edges = new ArrayList<>();

    for (Node node: nodes) {
      for (Edge edge: allEdges) {
        if (node.equals(edge.getStartNode()) || node.equals(edge.getEndNode())) {
          edges.add(edge);
        }
      }

    }

    for (Edge edge: edges) {
      double pin = edgeToPin(edge);
    }


    return pins.stream().mapToDouble(i -> i).toArray();
  }

  private double edgeToPin(Edge edge){

    if (edge.equals(toTopStair)) {
      return 4;
    } else if (edge.equals(toLowerElev)) {
      return 15;
    } else if (edge.equals(toTopElev)) {
      return 16;
    } else if (edge.equals(toZinner)) {
      return 1;
    } else if (edge.equals(toTopElev)) {
      return 5;
    } else if (edge.equals(toRoomHall)) {
      return 23;
    }  else if (edge.equals(toBathroom)) {
      return 24;
    } else if (edge.equals(toShapiro)) {
      return 25;
    } else if (edge.equals(elevator)) {
      return 27;
    } else if (edge.equals(toBottomExit)) {
      return 28;
    } else if (edge.equals(stairs)) {
      return 29;
    } else if (edge.equals(toNearRooms)) {
      return 0;
    } else if (edge.equals(longHall)) {
      return 2;
    } else if (edge.equals(toLowerElev)) {
      return 3;
    }

    return 30;
  }

}
