package edu.wpi.cs3733.d19.teamO.entity.pathfinding;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.first.networktables.ConnectionNotification;
import edu.wpi.first.networktables.NetworkTableInstance;

@SuppressWarnings({"PMD.TooManyFields", "PMD.SingularField"})
public class LEDModel {
  public final Node endHall;
  private final Node elevatorHallDoor;
  private final Node topElevator;
  private final Node topExit;
  private final Node topStair;
  private final Node bathroom;
  private final Node hallToRooms;
  private final Node hallNearRooms;
  private final Node shapiro;
  private final Node zinner;
  private final Node lowerElevator;
  private final Node bottomStair;
  private final Node bottomElevator;
  public final Node bottomExit;
  private final Edge longHall;
  private final Edge toTopElev;
  private final Edge toTopElevHall;
  private final Edge toTopStair;
  private final Edge toBathroom;
  private final Edge toRoomHall;
  private final Edge toNearRooms;
  private final Edge toShapiro;
  private final Edge toZinner;
  private final Edge toLowerElev;
  private final Edge stairs;
  private final Edge elevator;
  private final Edge toBottomElev;
  private final Edge toBottomExit;
  private final List<Node> allNodes;
  private final List<Edge> allEdges;
  private final MutableGraph<Node> graph;
  private List<Node> displayPath;

  /**
   * Constructor for LEDModel class.
   */
  @SuppressWarnings({"PMD.ExcessiveMethodLength", "PMD.AvoidUsingHardCodedIP"})
  public LEDModel() {
    this.endHall = new Node("GHALL02801", 1270, 1830, "1",
        "Shapiro", Node.NodeType.HALL, "Hallway MapNode 28 Floor 1", "Hall");
    this.elevatorHallDoor = new Node("GHALL02101", 1600, 1830, "1",
        "Shapiro", Node.NodeType.HALL, "Hallway MapNode 21 Floor 1", "Hall");
    this.topElevator = new Node("GELEV00N01", 1647, 1930, "1",
        "Shapiro", Node.NodeType.ELEV, "Elevator MapNode 26 Floor 1", "Hall");
    this.topExit = new Node("GEXIT00101", 1751, 1825, "1",
        "Shapiro", Node.NodeType.EXIT, "Francis Street Exit Floor 1",
        "Francis Street Exit MapNode 1 Floor 1");
    this.topStair = new Node("GSTAIR01301", 1750, 2014, "1",
        "Shapiro", Node.NodeType.STAI, "Stair Case MapNode Floor 1", "Southbound Stairs Floor 1");
    this.bathroom = new Node("GREST01201", 1671, 2015, "1",
        "Shapiro", Node.NodeType.REST, "Bathroom MapNode Floor 1", "Restroom Shapiro Floor 1");
    this.hallToRooms = new Node("GHALL00701", 1708, 2092, "1",
        "Shapiro", Node.NodeType.HALL, "Hallway MapNode 7 Floor 1", "Hall");
    this.shapiro = new Node("GCONF02001", 1493, 2117, "1",
        "Shapiro", Node.NodeType.CONF, "Shapiro Board Room MapNode 20 Floor 1",
        "Shapiro Board Room");
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
        "Shapiro", Node.NodeType.EXIT, "Fenwood Road Exit MapNode 1 Floor L1",
        "Fenwood Raod Exit Floor L1");
    this.allEdges = new ArrayList<Edge>();
    this.allNodes = new ArrayList<Node>();

    allNodes.add(endHall);
    allNodes.add(elevatorHallDoor);
    allNodes.add(topElevator);
    allNodes.add(topExit);
    allNodes.add(topStair);
    allNodes.add(bathroom);
    allNodes.add(hallToRooms);
    allNodes.add(hallNearRooms);
    allNodes.add(shapiro);
    allNodes.add(zinner);
    allNodes.add(lowerElevator);
    allNodes.add(bottomStair);
    allNodes.add(bottomElevator);
    allNodes.add(bottomExit);

    this.longHall = new Edge("longHall", endHall, elevatorHallDoor);
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
    this.elevator = new Edge("elevator", lowerElevator, bottomElevator);
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

    graph = GraphBuilder.undirected().allowsSelfLoops(false).build();
    allNodes.forEach(graph::addNode);
    allEdges.forEach(e -> graph.putEdge(e.getStartNode(), e.getEndNode()));

    displayPath = new ArrayList<Node>();

    NetworkTableInstance instance = NetworkTableInstance.getDefault();
    instance.startClient("130.215.209.224", 1735); // ip address for wpi 130.215.173.94
    instance.addConnectionListener(new Consumer<ConnectionNotification>() {
      @Override
      public void accept(ConnectionNotification connectionNotification) {
        System.out.println("Connected: " + (connectionNotification.connected ? "true" : "false"));
      }
    }, true);

    try {
      Thread.sleep(1000);
    } catch (InterruptedException exception) {
      exception.printStackTrace();
    }
    System.out.println("Connection status after 5000ms: "
        + (instance.isConnected() ? "true" : "false"));
  }

  public List<Node> getAllNodes() {
    return allNodes;
  }

  /**
   * Sends path to RaspberryPi to light up.
   * @param startNode The node the path starts at.
   * @param endNode The node the path ends at.
   */
  public void sendPathToPi(Node startNode, Node endNode) {
    System.out.println("MUST be done initializing by now");
    BreadthFirstSearchAlgorithm bfs = new BreadthFirstSearchAlgorithm();

    List<Node> path = bfs.getPath(graph, startNode, endNode);

    double [] pins = getListPins(path);
    System.out.println(pins.length);
    NetworkTableInstance.getDefault().deleteAllEntries();
    NetworkTableInstance.getDefault().getEntry("/path").setDoubleArray(pins);
    //NetworkTableInstance.getDefault().getEntry("/path").setDoubleArray(new double[]{});

    displayPath = path;
  }

  public List<Node> getDisplayPath() {
    return displayPath;
  }

  /**
   * Gets the list of pins.
   * @param nodes The nodes in a path.
   * @return A list of pins.
   */
  public double[] getListPins(List<Node> nodes) {
    ArrayList<Double> pins = new ArrayList<>();

    ArrayList<Edge> edges = new ArrayList<>();

    for (int i = 0; i < nodes.size() - 1 ; i++) {
      for (Edge edge: allEdges) {
        if (nodes.get(i).equals(edge.getStartNode())
            && nodes.get(i + 1).equals(edge.getEndNode())) {

          edges.add(edge);
        } else if (nodes.get(i).equals(edge.getEndNode())
            && nodes.get(i + 1).equals(edge.getStartNode())) {

          edges.add(edge);
        }
      }

    }

    for (Edge edge: edges) {
      double pin = edgeToPin(edge);
      pins.add(pin);
    }


    return pins.stream().mapToDouble(i -> i).toArray();
  }

  @SuppressWarnings("PMD.CyclomaticComplexity")
  private double edgeToPin(Edge edge) {

    if (edge.equals(toTopStair)) {
      return 4;
    } else if (edge.equals(toLowerElev)) {
      return 15;
    } else if (edge.equals(toTopElev)) {
      return 16;
    } else if (edge.equals(toZinner)) {
      return 1;
    } else if (edge.equals(toTopElevHall)) {
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
