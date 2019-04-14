package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.function.Supplier;

import edu.wpi.cs3733.d19.teamO.entity.Node;

public enum GraphSearchAlgorithm {
  BFS("Breadth First Search", BreadthFirstSearchAlgorithm::new),
  DFS("Depth First Search", DepthFirstSearchAlgorithm::new);

  private final String name;
  private final Supplier<IGraphSearchAlgorithm<Node>> algorithmSupplier;

  GraphSearchAlgorithm(final String name,
                       final Supplier<IGraphSearchAlgorithm<Node>> algorithmSupplier) {
    this.name = name;
    this.algorithmSupplier = algorithmSupplier;
  }

  public IGraphSearchAlgorithm<Node> getAlgorithm() {
    return algorithmSupplier.get();
  }

  @Override
  public String toString() {
    return name;
  }
}
