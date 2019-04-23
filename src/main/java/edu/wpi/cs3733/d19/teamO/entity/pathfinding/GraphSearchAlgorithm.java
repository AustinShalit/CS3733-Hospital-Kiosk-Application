package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import com.google.common.base.MoreObjects;

import edu.wpi.cs3733.d19.teamO.entity.Node;

/**
 * A GraphSearchAlgorithm is a way of traversing a map.
 */
public class GraphSearchAlgorithm {

	private final String name;
	private final IGraphSearchAlgorithm<Node> algorithm;

	/**
	 * Creates a new GraphSearchAlgorithm with the given name.
	 *
	 * @param name      the name of the theme
	 * @param algorithm the algorithm oto use
	 */
	public GraphSearchAlgorithm(String name, IGraphSearchAlgorithm<Node> algorithm) {
		this.name = name;
		this.algorithm = algorithm;
	}

	/**
	 * Gets the name of this theme.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the style sheets in this theme.
	 */
	public IGraphSearchAlgorithm<Node> getAlgorithm() {
		return algorithm;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("name", name)
				.add("algorithm", algorithm)
				.toString();
	}
}
