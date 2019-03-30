package edu.wpi.cs3733.d19.teamO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import javax.xml.soap.Node;

public class BreadthFirstSearchAlgorithm {

  public BreadthFirstSearchAlgorithm() {
  }

  private Set<Node> neighbors(Node aNode){
    Set<Node> neighbors = new HashSet<>();

    //search edges and add nodes connected to aNode

    return neighbors;
  }

  private Stack<Node> makePath(Map<Node, Node> cameFrom, Node goal){
    Stack<Node> path = new Stack<>();
    Node next = goal;
    while (cameFrom.get(next) != null){
      path.push(next);
      next = cameFrom.get(next);
    }
    path.push(next);
    return path;
  }


  public Stack<Node> getPath(Node start, Node goal){
    Queue<Node> frontier = new LinkedList<>();
    frontier.add(start);
    Map<Node, Node> cameFrom = new HashMap<>();
    cameFrom.put(start, null);

    while (!frontier.isEmpty()){
      Node current = frontier.poll();

      if (current.equals(goal)){
        break;
      }

      for (Node next: neighbors(current)){
        if (!cameFrom.containsKey(next)){
          frontier.add(next);
          cameFrom.put(next, current);
        }
      }
    }

    return makePath(cameFrom, goal);
  }








}
