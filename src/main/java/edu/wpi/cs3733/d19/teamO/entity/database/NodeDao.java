package edu.wpi.cs3733.d19.teamO.entity.database;

import java.util.List;
import java.util.Set;

import edu.wpi.cs3733.d19.teamO.entity.Node;

/**
 * Database access object for {@link Node}s.
 */
interface NodeDao extends Dao<String, Node> {

	/**
	 * Get all the free rooms in the node database table.
	 *
	 * @param type The type of room to search for.
	 * @return A set of nodes with room as the given type.
	 */
	Set<Node> getAllRooms(String type);

	/**
	 * Get all nodes on the given floor.
	 *
	 * @param floor The floor used to search for nodes.
	 * @return A set of nodes on the given floor.
	 */
	Set<Node> getFloor(String floor);

	/**
	 * Get a String representing an unused id in the node database table.
	 */
	String getFreeNodeId();

	List<Node> getAllByLongname();
}
