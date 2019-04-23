package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.PriorityQueue;

class DijkstraAlgorithm<T extends ComparableCost<T>> extends InformedGraphSearchAlgorithm<T> {

  private final PriorityQueue<CostPair<T>> frontier = new PriorityQueue<>();

  @Override
  void clearFrontier() {
    frontier.clear();
  }

  @Override
  boolean isEmptyFrontier() {
    return frontier.isEmpty();
  }

  @Override
  void addToFrontier(T node, double cost) {
    frontier.add(new CostPair<>(node, cost));
  }

  @Override
  T getNextFrontier() {
    if (frontier.isEmpty()) {
      return null;
    }
    return frontier.poll().getItem();
  }

  @Override
  double getPriorityHeuristic(T next, T goal) {
    return 0; // Dijkstra's does not use a heuristic
  }
}
