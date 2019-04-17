package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.google.common.graph.Graph;

import edu.wpi.cs3733.d19.teamO.entity.Node;

public class AStarAlgorithm extends InformedGraphSearchAlgorithm{

  public AStarAlgorithm() {
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public List<Node> getPath(Graph<Node> graph, Node start, Node goal) {
    PriorityQueue<DijkrasAlgorithm.Pair> frontier = new PriorityQueue<>();

    Map<Node, Node> cameFrom = new HashMap<>();
    cameFrom.put(start, null);
    Map<Node, Double> costSoFar = new HashMap<>();
    DijkrasAlgorithm.Pair newPair = new DijkrasAlgorithm.Pair(start , 0.0);
    frontier.add(newPair);
    costSoFar.put(start, 0.0);

    DijkrasAlgorithm.Pair nextPair = new DijkrasAlgorithm.Pair(start, 0.0);

    while (!frontier.isEmpty()) {
      Node current = frontier.poll().pairNode;

      if (current.equals(goal)) {
        break;
      }

      for (Node next : graph.successors(current)) {
        Double newCost = costSoFar.get(current) + euclideanDist(current,next);
        if (!cameFrom.containsKey(next) || newCost < costSoFar.get(next)) {
          double priority = newCost + heuristic(goal, next);
          nextPair.setPairNode(next);
          nextPair.setPairCost(priority);
          frontier.add(nextPair);
          costSoFar.put(next, newCost);
          cameFrom.put(next, current);
        }
      }
    }


    return buildPath(cameFrom, goal);
  }

  private double heuristic(Node goal, Node next) {
    double dist = Math.abs(goal.getXcoord() -next.getXcoord()) + Math.abs(goal.getYcoord() -
        next.getYcoord());
    return dist;
  }




}
