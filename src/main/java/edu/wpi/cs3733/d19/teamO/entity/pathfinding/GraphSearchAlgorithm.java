package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.List;

import com.google.common.graph.Graph;

/**
 * A search algorithm to find the path between two nodes in a graph.
 *
 * @param <T> The type of graph the search algorithm is searching
 */
interface GraphSearchAlgorithm<T> {
  List<T> getPath(Graph<T> graph, T start, T goal);
}
