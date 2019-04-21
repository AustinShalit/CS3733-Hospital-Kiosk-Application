package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import com.google.common.graph.Graph;
import edu.wpi.cs3733.d19.teamO.entity.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class DijkrasAlgorithm extends InformedGraphSearchAlgorithm {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<Node> getPath(Graph<Node> graph, Node start, Node goal) {
        PriorityQueue<Pair> frontier = new PriorityQueue<>();

        Map<Node, Node> cameFrom = new HashMap<>();
        cameFrom.put(start, null);
        Map<Node, Double> costSoFar = new HashMap<>();
        Pair newPair = new Pair(start, 0.0);
        frontier.add(newPair);
        costSoFar.put(start, 0.0);

        Pair nextPair = new Pair(start, 0.0);

        while (!frontier.isEmpty()) {
            Node current = frontier.poll().pairNode;

            if (current.equals(goal)) {
                break;
            }


            for (Node next : graph.successors(current)) {
                Double newCost = costSoFar.get(current) + euclideanDist(current, next);
                if (!cameFrom.containsKey(next) || newCost < costSoFar.get(next)) {
                    double priority = newCost;
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

    static final class Pair implements Comparable<Pair> {
        Node pairNode;
        double pairCost;

        Pair(Node pairNode, Double pairCost) {
            this.pairNode = pairNode;
            this.pairCost = pairCost;
        }

        public void setPairNode(Node pairNode) {
            this.pairNode = pairNode;
        }

        public void setPairCost(double pairCost) {
            this.pairCost = pairCost;
        }

        @Override
        public int compareTo(Pair pair) {
            return Double.compare(pairCost, pair.pairCost);
        }


    }


}
