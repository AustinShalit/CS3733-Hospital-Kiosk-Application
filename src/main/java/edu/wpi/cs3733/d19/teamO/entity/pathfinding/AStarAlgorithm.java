package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.PriorityQueue;

class AStarAlgorithm<T> extends InformedGraphSearchAlgorithm<T> {

  private final PriorityQueue<T> frontier = new PriorityQueue<>();

  @Override
  void clearFrontier() {
    frontier.clear();
  }

  @Override
  boolean isEmptyFrontier() {
    return frontier.isEmpty();
  }

  @Override
  void addToFrontier(T node) {
    frontier.add(node);
  }

  @Override
  T getNextFrontier() {
    return frontier.poll();
  }
}
