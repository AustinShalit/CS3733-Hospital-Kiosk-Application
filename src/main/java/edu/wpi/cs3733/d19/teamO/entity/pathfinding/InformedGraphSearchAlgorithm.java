package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;

/**
 * Abstract class for uninformed graph search algorithms.
 */
abstract class InformedGraphSearchAlgorithm implements IGraphSearchAlgorithm<Node> {


  /**
   * Given a map of nodes and the goal, construct a path of nodes to follow.
   *
   * @param cameFrom A map of nodes
   * @param goal     The goal
   * @return The path
   */
  protected List<Node> buildPath(final Map<Node, Node> cameFrom, final Node goal) {
    LinkedList<Node> path = new LinkedList<>(); // We want Queue interface
    Node next = goal;
    while (cameFrom.get(next) != null) {
      path.push(next);
      next = cameFrom.get(next);
    }
    path.push(next);
    return path;
  }

  public double euclideanDist(Node node1, Node node2) {
    double dist = Math.sqrt(Math.pow(node2.getXcoord() - node1.getXcoord(),2) +
        Math.pow(node2.getYcoord() - node1.getYcoord(), 2));
    return dist;
  }
}
