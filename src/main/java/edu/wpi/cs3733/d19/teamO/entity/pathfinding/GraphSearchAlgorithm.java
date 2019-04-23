package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.graph.Graph;

import static edu.wpi.cs3733.d19.teamO.entity.pathfinding.PathBuilderUtility.buildPath;

/**
 * A GraphSearchAlgorithm is a way of traversing a map.
 */
public abstract class GraphSearchAlgorithm<T> {
  /**
   * Clear the frontier of all elements.
   */
  abstract void clearFrontier();

  /**
   * Checks to see if the frontier is empty.
   */
  abstract boolean isEmptyFrontier();

  /**
   * Adds the provided item to the frontier.
   */
  abstract void addToFrontier(T node);

  /**
   * Gets the next item from the frontier.
   */
  abstract T getNextFrontier();

  /**
   * Given a graph, get the path between the start and end nodes.
   */
  public final List<T> getPath(Graph<T> graph, T start, T goal) {
    clearFrontier();
    final Map<T, T> cameFrom = new HashMap<>();

    addToFrontier(start);
    cameFrom.put(start, null);

    while (!isEmptyFrontier()) {
      T current = getNextFrontier();

      if (current.equals(goal)) {
        break;
      }

      for (T next : graph.successors(current)) {
        if (!cameFrom.containsKey(next)) {
          addToFrontier(next);
          cameFrom.put(next, current);
        }
      }
    }
    return buildPath(cameFrom, goal);
  }
}
