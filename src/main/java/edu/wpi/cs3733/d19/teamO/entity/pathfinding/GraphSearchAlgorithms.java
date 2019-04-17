package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Stack;

import jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import edu.wpi.cs3733.d19.teamO.util.Registry;

/**
 * Keeps track of the GraphSearchAlgorithms available to the application.
 */
public final class GraphSearchAlgorithms extends Registry<GraphSearchAlgorithm> {

  // TODO replace with DI eg Guice
  private static GraphSearchAlgorithms defaultInstance;

  public static final GraphSearchAlgorithm BFS = new GraphSearchAlgorithm("Breadth First Search",
      new BreadthFirstSearchAlgorithm<>());
  public static final GraphSearchAlgorithm DFS = new GraphSearchAlgorithm("Depth First Search",
      new DepthFirstSearchAlgorithm<>());

  public static final GraphSearchAlgorithm INITIAL_ALGORITHM = BFS;

  /**
   * Gets the default algorithms instance.
   */
  public static GraphSearchAlgorithms getDefault() {
    synchronized (GraphSearchAlgorithms.class) {
      if (defaultInstance == null) {
        defaultInstance = new GraphSearchAlgorithms(BFS, DFS);
      }
    }
    return defaultInstance;
  }

  /**
   * Creates a new GraphSearchAlgorithm registry.
   *
   * @param initial the initial GraphSearchAlgorithms
   */
  public GraphSearchAlgorithms(GraphSearchAlgorithm... initial) {
    registerAll(initial);
  }

  /**
   * Gets the algorithm with the given name. If there is no algorithm with that name, returns
   * {@link #INITIAL_ALGORITHM} instead.
   *
   * @param name the name of the algorithm to get
   */
  public GraphSearchAlgorithm forName(String name) {
    return getItems().stream()
        .filter(t -> t.getName().equals(name))
        .findFirst()
        .orElse(INITIAL_ALGORITHM);
  }

  @Override
  public void register(GraphSearchAlgorithm algorithm) {
    Objects.requireNonNull(algorithm);
    if (isRegistered(algorithm)) {
      throw new IllegalArgumentException("Algorithm " + algorithm + " is already registered");
    }
    addItem(algorithm);
  }

  @Override
  public void unregister(GraphSearchAlgorithm algorithm) { removeItem(algorithm); }
}
