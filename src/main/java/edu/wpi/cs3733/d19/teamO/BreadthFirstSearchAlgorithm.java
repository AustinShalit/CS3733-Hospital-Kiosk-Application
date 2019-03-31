package edu.wpi.cs3733.d19.teamO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import javax.xml.soap.Node;

public class BreadthFirstSearchAlgorithm {

  public BreadthFirstSearchAlgorithm() {
  }

  private Set<Node> neighbors(Node aNode) {
    Set<Node> neighbors = new HashSet<>();

    neighbors.add(aNode);

    return neighbors;
  }


  private Stack<Node> makePath(Map<Node, Node> cameFrom, Node goal) {
    Stack<Node> path = new Stack<>();
    Node next = goal;
    while (cameFrom.get(next) != null) {
      path.push(next);
      next = cameFrom.get(next);
    }
    path.push(next);
    return path;
  }

  /**
   * This returns a path from the start node to the goal.
   * @param start the node the user starts at.
   * @param goal the node the user wants to end at.
   * @return a stack containg the path to be traveled.
   */
  public Stack<Node> getPath(Node start, Node goal) {
    Queue<Node> frontier = new LinkedList<>();
    frontier.add(start);
    Map<Node, Node> cameFrom = new HashMap<>();
    cameFrom.put(start, null);

    while (!frontier.isEmpty()) {
      Node current = frontier.poll();

      if (current.equals(goal)) {
        break;
      }

      for (Node next : neighbors(current)) {
        if (!cameFrom.containsKey(next)) {
          frontier.add(next);
          cameFrom.put(next, current);
        }
      }
    }

    return makePath(cameFrom, goal);
  }


}
