package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.function.Supplier;

public class GraphSearchAlgorithmSupplier {

  private final String name;
  private final Supplier<GraphSearchAlgorithm> supplier;

  GraphSearchAlgorithmSupplier(String name,
                               Supplier<GraphSearchAlgorithm> supplier) {
    this.name = name;
    this.supplier = supplier;
  }

  public String getName() {
    return name;
  }

  public Supplier<GraphSearchAlgorithm> getSupplier() {
    return supplier;
  }
}
