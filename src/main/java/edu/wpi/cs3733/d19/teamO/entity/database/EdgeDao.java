package edu.wpi.cs3733.d19.teamO.entity.database;

import java.util.Set;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;

/**
 * Database access object for {@link Edge}s.
 */
interface EdgeDao extends Dao<Edge> {
  /**
   * Get all the edges that a provided node is a part of.
   *
   * @param node The node
   * @return A Set containing the all Edges the node is a part of
   */
  Set<Edge> getEdgesFor(Node node);
}
