package edu.wpi.cs3733.d19.teamO;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class SearchAlgorithm {
  protected final Database database;

  public SearchAlgorithm(Database database) {
    this.database = database;
  }

  protected Set<Node> neighbors(Node aNode) {
    Set<Node> neighbors = new HashSet<>();


    Set<Edge> edges = database.getEdgesFor(aNode);

    for (Edge edge: edges) {
      if (edge.getStartNode().equals(aNode)) {
        neighbors.add(edge.getEndNode());
      } else if (edge.getEndNode().equals(aNode)) {
        neighbors.add(edge.getStartNode());
      }
    }
    return neighbors;
  }

  protected Stack<Node> makePath(Map<Node, Node> cameFrom, Node goal) {
    Stack<Node> path = new Stack<>();
    Node next = goal;
    while (cameFrom.get(next) != null) {
      path.push(next);
      next = cameFrom.get(next);
    }
    path.push(next);
    return path;
  }
}
