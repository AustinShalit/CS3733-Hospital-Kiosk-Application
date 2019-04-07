package edu.wpi.cs3733.d19.teamO.entity.database;

import java.util.Set;

import edu.wpi.cs3733.d19.teamO.entity.Node;

/**
 * Database access object for {@link Node}s.
 */
interface NodeDao extends Dao<String, Node> {
  Set<Node> getAllRooms();
}
