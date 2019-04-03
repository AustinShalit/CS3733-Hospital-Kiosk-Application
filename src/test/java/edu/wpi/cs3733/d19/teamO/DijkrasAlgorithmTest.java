package edu.wpi.cs3733.d19.teamO;

import java.sql.SQLException;
import java.util.Stack;

import org.assertj.core.api.Fail;
import org.junit.jupiter.api.Test;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

import static org.junit.jupiter.api.Assertions.*;

class DijkrasAlgorithmTest {


  private static final Node NODE_A = new Node("A", 0, 0, 0, "B", Node.NodeType.HALL,
      "AL", "AS");
  private static final Node NODE_B = new Node("B", 6, 0, 0, "B", Node.NodeType.HALL,
      "BL", "BS");
  private static final Node NODE_C = new Node("C", 10, 7, 0, "B", Node.NodeType.HALL,
      "CL", "CS");
  private static final Node NODE_D = new Node("D", 9, -2, 0, "B", Node.NodeType.HALL,
      "CL", "CS");
  private static final Node NODE_E = new Node("E", 11, 0, 0, "B", Node.NodeType.HALL,
      "CL", "CS");
  private static final Edge EDGE_AB = new Edge("AB", NODE_A, NODE_B);
  private static final Edge EDGE_BC = new Edge("BC", NODE_B, NODE_C);
  private static final Edge EDGE_CE = new Edge("CE", NODE_C, NODE_E);
  private static final Edge EDGE_BD = new Edge("BD", NODE_B, NODE_D);
  private static final Edge EDGE_DE = new Edge("DE", NODE_D, NODE_E);

  @Test
  void getPath() throws SQLException {

    Database database = new Database(BreadthFirstSearchAlgorithmTest.class.getName());
    database.insertNode(NODE_A);
    database.insertNode(NODE_B);
    database.insertNode(NODE_C);
    database.insertNode(NODE_D);
    database.insertNode(NODE_E);
    database.insertEdge(EDGE_AB);
    database.insertEdge(EDGE_BC);
    database.insertEdge(EDGE_BD);
    database.insertEdge(EDGE_CE);
    database.insertEdge(EDGE_DE);

    DijkrasAlgorithm dij = new DijkrasAlgorithm(database);

    Stack<Node> path = dij.getPath(NODE_A, NODE_E);

    Stack<Node> correctPath = new Stack<Node>();

    correctPath.add(NODE_E);
    correctPath.add(NODE_D);
    correctPath.add(NODE_B);
    correctPath.add(NODE_A);

    assertEquals(path, correctPath);

  }
}