package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.google.common.graph.Graph;

class DepthFirstSearchAlgorithm<T> extends UninformedGraphSearchAlgorithm<T> {
	@Override
	public String getName() {
		return "Depth First Search";
	}

	@Override
	public List<T> getPath(final Graph<T> graph, final T start, final T goal) {
		final Stack<T> frontier = new Stack<>();
		final Map<T, T> cameFrom = new HashMap<>();
		frontier.push(start);
		cameFrom.put(start, null);

		while (!frontier.isEmpty()) {
			T current = frontier.pop();

			if (current.equals(goal)) {
				break;
			}

			for (T next : graph.successors(current)) {
				if (!cameFrom.containsKey(next)) {
					frontier.push(next);
					cameFrom.put(next, current);
				}
			}
		}
		return buildPath(cameFrom, goal);
	}
}
