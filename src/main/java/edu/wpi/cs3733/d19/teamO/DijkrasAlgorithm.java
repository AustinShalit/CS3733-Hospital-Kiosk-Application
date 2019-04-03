package edu.wpi.cs3733.d19.teamO;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class DijkrasAlgorithm extends SearchAlgorithm {

  public DijkrasAlgorithm(Database database) {
    super(database);
  }

  private static final class Pair implements Comparable<Pair> {
    Node pairNode;
    double pairCost;

    Pair(Node pairNode, Double pairCost) {
      this.pairNode = pairNode;
      this.pairCost = pairCost;
    }

    @Override
    public int compareTo(Pair pair) {
      return Double.compare(pairCost, pair.pairCost);
    }
  }

  Stack<Node> getPath(Node start, Node goal) {
    PriorityQueue<Pair> frontier = new PriorityQueue<>();

    Map<Node, Node> cameFrom = new HashMap<>();
    cameFrom.put(start, null);
    Map<Node, Double> costSoFar = new HashMap<>();
    Pair newPair = new Pair(start , 0.0);
    frontier.add(newPair);
    costSoFar.put(start, 0.0);

    while (!frontier.isEmpty()) {
      Node current = frontier.poll().pairNode;

      if (current.equals(goal)) {
        break;
      }

      for (Node next : neighbors(current)) {
        Double newCost = costSoFar.get(current) + current.getCost(next);
        if (!cameFrom.containsKey(next) || newCost < costSoFar.get(next)) {
          Double priority = newCost;
          Pair nextPair = new Pair(next, priority);
          frontier.add(nextPair);
          costSoFar.put(next,newCost);
          cameFrom.put(next, current);
        }
      }
    }

    return makePath(cameFrom, goal);
  }


}
