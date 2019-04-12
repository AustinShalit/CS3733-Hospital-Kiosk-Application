package edu.wpi.cs3733.d19.teamO.entity.csv;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;

import com.google.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import edu.wpi.cs3733.d19.teamO.entity.database.DatabaseExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DatabaseExtension.class)
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

  private static File TEST_DATA_FILE;

  static {
    try {
      TEST_DATA_FILE
          = new File(EdgeCsvReaderWriterTest.class.getResource("test_edges.csv").toURI());
    } catch (URISyntaxException ex) {
      ex.printStackTrace();
    }
  }

  @Inject
  private Database database;

  @BeforeEach
  void setup() {
    database.insertNode(NODE_A);
    database.insertNode(NODE_B);
    database.insertNode(NODE_C);
  }

  @Test
  void readEdgesTest() throws IOException {
    EdgeCsvReaderWriter edgeCsvReaderWriter = new EdgeCsvReaderWriter(database);
    List<Edge> edges
        = edgeCsvReaderWriter.readEdges(Files.newBufferedReader(TEST_DATA_FILE.toPath()));

    assertAll(
        () -> assertEquals(3, edges.size()),
        () -> assertTrue(edges.contains(EDGE_AB)),
        () -> assertTrue(edges.contains(EDGE_BC)),
        () -> assertTrue(edges.contains(EDGE_CA))
    );
  }
}
