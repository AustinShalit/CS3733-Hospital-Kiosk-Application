package edu.wpi.cs3733.d19.teamO.entity.csv;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EdgeCsvReaderWriterTest {

  private static final Node NODE_A = new Node("A", 1, 2, "0", "B", Node.NodeType.HALL,
      "AL", "AS");
  private static final Node NODE_B = new Node("B", 3, 4, "0", "B", Node.NodeType.HALL,
      "BL", "BS");
  private static final Node NODE_C = new Node("C", 5, 6, "0", "B", Node.NodeType.HALL,
      "CL", "CS");
  private static final Edge EDGE_AB = new Edge("AB", NODE_A, NODE_B);
  private static final Edge EDGE_BC = new Edge("BC", NODE_B, NODE_C);
  private static final Edge EDGE_CA = new Edge("CA", NODE_C, NODE_A);

  private static Path TEST_DATA_FILE;

  static {
    try {
      TEST_DATA_FILE
          = Paths.get(EdgeCsvReaderWriterTest.class.getResource("test_edges.csv")
          .toURI().getPath());
    } catch (URISyntaxException ex) {
      ex.printStackTrace();
    }
  }

  private static Database database;

  @BeforeAll
  static void setup() throws SQLException {
    database = new Database(EdgeCsvReaderWriterTest.class.getName());
    database.insertNode(NODE_A);
    database.insertNode(NODE_B);
    database.insertNode(NODE_C);
  }

  @Test
  void readEdgesTest() throws IOException {
    EdgeCsvReaderWriter edgeCsvReaderWriter = new EdgeCsvReaderWriter(database);
    List<Edge> edges = edgeCsvReaderWriter.readEdges(TEST_DATA_FILE);

    assertAll(
        () -> assertEquals(3, edges.size()),
        () -> assertTrue(edges.contains(EDGE_AB)),
        () -> assertTrue(edges.contains(EDGE_BC)),
        () -> assertTrue(edges.contains(EDGE_CA))
    );
  }
}
