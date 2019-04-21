package edu.wpi.cs3733.d19.teamO.entity.database;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;

import java.util.Set;

/**
 * Database access object for {@link Edge}s.
 */
interface EdgeDao extends Dao<String, Edge> {
    /**
     * Get all the edges that a provided node is a part of.
     *
     * @param node The node
     * @return A Set containing the all Edges the node is a part of
     */
    Set<Edge> getEdgesFor(Node node);

    /**
     * Get all edges that are in specific floor.
     *
     * @param floor The string of floor
     * @return the set of edges that contain nodes in specific floor
     */
    Set<Edge> getFloor(String floor);

    /**
     * Get a String representing an unused id in the edge database table.
     */
    String getFreeEdgeId();
}
