package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.Objects;

import edu.wpi.cs3733.d19.teamO.util.Registry;

/**
 * Keeps track of the GraphSearchAlgorithms available to the application.
 */
public final class GraphSearchAlgorithms extends Registry<GraphSearchAlgorithmSupplier> {

  private static GraphSearchAlgorithms defaultInstance;

  private static final GraphSearchAlgorithmSupplier A_STAR
      = new GraphSearchAlgorithmSupplier("A Star",
      AStarAlgorithm::new);
  private static final GraphSearchAlgorithmSupplier DIJKSTRA
      = new GraphSearchAlgorithmSupplier("Dijkstra",
      DijkstraAlgorithm::new);
  private static final GraphSearchAlgorithmSupplier BFS
      = new GraphSearchAlgorithmSupplier("Breadth First Search",
      BreadthFirstSearchAlgorithm::new);
  private static final GraphSearchAlgorithmSupplier DFS
      = new GraphSearchAlgorithmSupplier("Depth First Search",
      DepthFirstSearchAlgorithm::new);

  public static final GraphSearchAlgorithmSupplier INITIAL_ALGORITHM = A_STAR;

  /**
   * Gets the default algorithms instance.
   */
  public static GraphSearchAlgorithms getDefault() {
    synchronized (GraphSearchAlgorithms.class) {
      if (defaultInstance == null) {
        defaultInstance = new GraphSearchAlgorithms(A_STAR, DIJKSTRA, BFS, DFS);
      }
    }
    return defaultInstance;
  }

  /**
   * Creates a new GraphSearchAlgorithm registry.
   *
   * @param initial the initial GraphSearchAlgorithms
   */
  private GraphSearchAlgorithms(GraphSearchAlgorithmSupplier... initial) {
    registerAll(initial);
  }

  /**
   * Gets the algorithm with the given name. If there is no algorithm with that name, returns
   * {@link #INITIAL_ALGORITHM} instead.
   *
   * @param name the name of the algorithm to get
   */
  public GraphSearchAlgorithmSupplier forName(String name) {
    return getItems().stream()
        .filter(t -> t.getName().equals(name))
        .findFirst()
        .orElse(INITIAL_ALGORITHM);
  }

  @Override
  public void register(GraphSearchAlgorithmSupplier algorithm) {
    Objects.requireNonNull(algorithm);
    if (isRegistered(algorithm)) {
      throw new IllegalArgumentException("Algorithm " + algorithm + " is already registered");
    }
    addItem(algorithm);
  }

  @Override
  public void unregister(GraphSearchAlgorithmSupplier algorithm) {
    removeItem(algorithm);
  }
}
