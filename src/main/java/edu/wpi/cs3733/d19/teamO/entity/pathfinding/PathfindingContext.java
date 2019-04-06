package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.List;

import com.google.common.graph.Graph;

public class PathfindingContext<T> {

  private GraphSearchAlgorithm<T> strategy;

  private PathfindingContext(GraphSearchAlgorithm<T> strategy) {
    this.strategy = strategy;
  }

  public PathfindingContext() {
    this(new BreadthFirstSearchAlgorithm<>());
  }

  public GraphSearchAlgorithm<T> getStrategy() {
    return strategy;
  }

  public void setStrategy(GraphSearchAlgorithm<T> strategy) {
    this.strategy = strategy;
  }

  public List<T> getPath(Graph<T> graph, T start, T goal) {
    return strategy.getPath(graph, start, goal);
  }
}
