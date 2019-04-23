package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

/**
 * Abstract class for uninformed graph search algorithms.
 */
abstract class UninformedGraphSearchAlgorithm<T extends ComparableCost<T>>
    extends GraphSearchAlgorithm<T> {

  abstract void addToFrontier(T node);

  @Override
  final void addToFrontier(T node, double priority) {
    addToFrontier(node);
  }

  @Override
  double getPriorityHeuristic(T next, T goal) {
    return 0;
  }
}
