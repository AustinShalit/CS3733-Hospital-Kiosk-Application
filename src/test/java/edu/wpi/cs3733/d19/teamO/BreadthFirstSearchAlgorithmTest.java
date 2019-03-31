package edu.wpi.cs3733.d19.teamO;

import java.sql.SQLException;
import java.util.Stack;

import org.junit.jupiter.api.Test;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BreadthFirstSearchAlgorithmTest {

  private static final Node NODE_A = new Node("A", 1, 2, 0, "B", Node.NodeType.HALL,
      "AL", "AS");
  private static final Node NODE_B = new Node("B", 3, 4, 0, "B", Node.NodeType.HALL,
      "BL", "BS");
  private static final Node NODE_C = new Node("C", 5, 6, 0, "B", Node.NodeType.HALL,
      "CL", "CS");
  private static final Edge EDGE_AB = new Edge("AB", NODE_A, NODE_B);
  private static final Edge EDGE_BC = new Edge("BC", NODE_B, NODE_C);

  @Test
  void getPathTest() throws SQLException {
    Database database = new Database(BreadthFirstSearchAlgorithmTest.class.getName());
    database.insertNode(NODE_A);
    database.insertNode(NODE_B);
    database.insertNode(NODE_C);
    database.insertEdge(EDGE_AB);
    database.insertEdge(EDGE_BC);
    BreadthFirstSearchAlgorithm bfs = new BreadthFirstSearchAlgorithm(database);
    Stack<Node> path = bfs.getPath(NODE_A, NODE_C);

    assertEquals(3, path.size());


  }

}
