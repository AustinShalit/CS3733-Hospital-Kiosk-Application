package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

final class PathBuilderUtility {

  private PathBuilderUtility() {
    throw new UnsupportedOperationException("Utility class!");
  }

  static <T> List<T> buildPath(final Map<T, T> cameFrom, final T goal) {
    LinkedList<T> path = new LinkedList<>(); // We want Queue interface
    T next = goal;
    while (cameFrom.get(next) != null) {
      path.push(next);
      next = cameFrom.get(next);
    }
    path.push(next);
    return path;
  }
}
