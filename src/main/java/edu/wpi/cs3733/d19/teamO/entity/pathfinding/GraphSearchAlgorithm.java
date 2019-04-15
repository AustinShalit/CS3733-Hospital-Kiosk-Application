package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.function.Supplier;

import edu.wpi.cs3733.d19.teamO.entity.Node;

public enum GraphSearchAlgorithm {
  BFS(BreadthFirstSearchAlgorithm::new),
  DFS(DepthFirstSearchAlgorithm::new);

  //private final String name;
  private final Supplier<IGraphSearchAlgorithm<Node>> algorithmSupplier;

  GraphSearchAlgorithm(final Supplier<IGraphSearchAlgorithm<Node>> algorithmSupplier) {
    this.algorithmSupplier = algorithmSupplier;
  }

  public IGraphSearchAlgorithm<Node> getAlgorithm() {
    return algorithmSupplier.get();
  }
}
