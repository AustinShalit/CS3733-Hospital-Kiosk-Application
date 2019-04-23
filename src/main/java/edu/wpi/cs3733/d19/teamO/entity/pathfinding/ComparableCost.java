package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

public interface ComparableCost<T> {
  /**
   * Get the cost to another T.
   */
  double getCostTo(T other);
}
