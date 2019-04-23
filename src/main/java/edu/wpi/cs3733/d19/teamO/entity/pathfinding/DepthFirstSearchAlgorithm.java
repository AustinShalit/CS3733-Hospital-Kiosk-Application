package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.Stack;

class DepthFirstSearchAlgorithm<T extends ComparableCost<T>>
    extends UninformedGraphSearchAlgorithm<T> {

  private final Stack<T> frontier = new Stack<>();

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
    frontier.push(node);
  }

  @Override
  T getNextFrontier() {
    return frontier.pop();
  }
}
