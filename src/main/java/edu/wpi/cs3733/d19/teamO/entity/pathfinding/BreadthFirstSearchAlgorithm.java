package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.LinkedList;
import java.util.Queue;

class BreadthFirstSearchAlgorithm<T> extends UninformedGraphSearchAlgorithm<T> {

  private final Queue<T> frontier = new LinkedList<>();

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
