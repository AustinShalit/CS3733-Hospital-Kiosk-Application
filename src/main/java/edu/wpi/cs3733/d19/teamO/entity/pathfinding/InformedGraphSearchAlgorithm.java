package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

/**
 * Abstract class for uninformed graph search algorithms.
 */
abstract class InformedGraphSearchAlgorithm<T extends ComparableCost<T>>
    extends GraphSearchAlgorithm<T> {

  protected static class CostPair<T> implements Comparable<CostPair<T>> {
    private final T item;
    private final double cost;

    CostPair(T item, double cost) {
      this.item = item;
      this.cost = cost;
    }

    public T getItem() {
      return item;
    }

    public double getCost() {
      return cost;
    }

    @Override
    public int compareTo(@NotNull CostPair<T> o) {
      return Double.compare(cost, o.cost);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      CostPair<?> costPair = (CostPair<?>) o;
      return Objects.equals(item, costPair.item);
    }

    @Override
    public int hashCode() {
      return Objects.hash(item);
    }
  }
}
