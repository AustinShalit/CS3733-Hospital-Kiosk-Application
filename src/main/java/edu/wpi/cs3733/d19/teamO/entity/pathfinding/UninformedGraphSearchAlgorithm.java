package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Abstract class for uninformed graph search algorithms.
 */
abstract class UninformedGraphSearchAlgorithm<T> implements IGraphSearchAlgorithm<T> {

	/**
	 * Given a map of nodes and the goal, construct a path of nodes to follow.
	 *
	 * @param cameFrom A map of nodes
	 * @param goal     The goal
	 * @return The path
	 */
	protected List<T> buildPath(final Map<T, T> cameFrom, final T goal) {
		LinkedList<T> path = new LinkedList<>(); // We want Queue interface
		T next = goal;
		while (cameFrom.get(next) != null) {
			path.push(next);
			next = cameFrom.get(next);
		}
		path.push(next);
		return path;
	}
}
