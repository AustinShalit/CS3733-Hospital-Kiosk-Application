package edu.wpi.cs3733.d19.teamO.entity.pathfinding;

import java.util.Arrays;
import java.util.List;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DepthFirstSearchAlgorithmTest {

  private static ImmutableGraph<String> graph;

  @BeforeAll
  static void beforeAll() {
    MutableGraph<String> graph = GraphBuilder.undirected().allowsSelfLoops(false).build();
    graph.addNode("A");
    graph.addNode("B");
    graph.addNode("C");
    graph.addNode("D");
    graph.addNode("E");
    graph.putEdge("A", "B");
    graph.putEdge("B", "C");
    graph.putEdge("C", "E");
    graph.putEdge("A", "D");
    graph.putEdge("D", "E");

    DepthFirstSearchAlgorithmTest.graph = ImmutableGraph.copyOf(graph);
  }

  @Test
  void pathStartIsFirstTest() {
    GraphSearchAlgorithm<String> algorithm = new DepthFirstSearchAlgorithm<>();

    List<String> path = algorithm.getPath(graph, "A", "E");

    assertEquals("A", path.get(0));
  }

  @Test
  void pathGoalIsLastTest() {
    GraphSearchAlgorithm<String> algorithm = new DepthFirstSearchAlgorithm<>();

    List<String> path = algorithm.getPath(graph, "A", "E");

    assertEquals("E", path.get(path.size() - 1));
  }

  @Test
  void getPathTest() {
    GraphSearchAlgorithm<String> algorithm = new DepthFirstSearchAlgorithm<>();

    List<String> path = algorithm.getPath(graph, "A", "E");

    assertEquals(Arrays.asList("A", "D", "E"), path);
  }
}
