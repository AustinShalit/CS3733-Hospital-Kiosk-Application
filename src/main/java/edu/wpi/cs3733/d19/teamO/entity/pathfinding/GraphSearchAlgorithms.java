package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.Objects;

import edu.wpi.cs3733.d19.teamO.entity.util.Registry;

public final class GraphSearchAlgorithms extends Registry<GraphSearchAlgorithm> {

  private static final GraphSearchAlgorithm BFS = new BreadthFirstSearchAlgorithm();
  private static final GraphSearchAlgorithm DFS = new DepthFirstSearchAlgorithm();

  public static final GraphSearchAlgorithm INITIAL = BFS;

  private static class InstanceHolder {
    static final GraphSearchAlgorithms INSTANCE = new GraphSearchAlgorithms(BFS, DFS);
  }

  public static GraphSearchAlgorithms getInstance() {
    return InstanceHolder.INSTANCE;
  }

  /**
   * Creates a new GraphSearchAlgorithm registry.
   *
   * @param initial the initial GraphSearchAlgorithms
   */
  private GraphSearchAlgorithms(GraphSearchAlgorithm... initial) {
    registerAll(initial);
  }

  /**
   * Gets the algorithm with the given name. If there is no algorithm with that name, returns
   * {@link #INITIAL} instead.
   *
   * @param name the name of the algorithm to get
   */
  public GraphSearchAlgorithm forName(final String name) {
    return getItems().stream()
        .filter(t -> t.getName().equals(name))
        .findFirst()
        .orElse(INITIAL);
  }

  @Override
  public void register(GraphSearchAlgorithm item) {
    Objects.requireNonNull(item);
    if (isRegistered(item)) {
      throw new IllegalArgumentException("GraphSearchAlgorithm " + item + " is already registered");
    }
    addItem(item);
  }

  @Override
  public void unregister(GraphSearchAlgorithm item) {
    removeItem(item);
  }
}
