package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class GraphSearchAlgorithms {

  public static final GraphSearchAlgorithm BFS = new BreadthFirstSearchAlgorithm<>();
  public static final GraphSearchAlgorithm DFS = new BreadthFirstSearchAlgorithm<>();

  public static final GraphSearchAlgorithm INITIAL = BFS;

  private static final ObservableList<GraphSearchAlgorithm> algorithms
      = FXCollections.unmodifiableObservableList(
      FXCollections.observableArrayList(BFS, DFS));

  /**
   * Gets the algorithm with the given name. If there is no algorithm with that name, returns
   * {@link #INITIAL} instead.
   *
   * @param name the name of the algorithm to get
   */
  public static GraphSearchAlgorithm forName(final String name) {
    return getItems().stream()
        .filter(t -> t.getClass().getName().equals(name))
        .findFirst()
        .orElse(INITIAL);
  }

  public static ObservableList<GraphSearchAlgorithm> getItems() {
    return algorithms;
  }
}
