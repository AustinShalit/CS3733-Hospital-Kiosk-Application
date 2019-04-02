package edu.wpi.cs3733.d19.teamO.entity.csv;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.wpi.cs3733.d19.teamO.entity.Node;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NodeCsvReaderWriterTest {

  private static final Node NODE_A = new Node("A", 1, 2, "0", "B", Node.NodeType.HALL,
      "AL", "AS");
  private static final Node NODE_B = new Node("B", 3, 4, "0", "B", Node.NodeType.HALL,
      "BL", "BS");
  private static final Node NODE_C = new Node("C", 5, 6, "0", "B", Node.NodeType.HALL,
      "CL", "CS");

  private static File TEST_DATA_FILE;

  static {
    try {
      TEST_DATA_FILE
          = new File(EdgeCsvReaderWriterTest.class.getResource("test_nodes.csv").toURI());
    } catch (URISyntaxException ex) {
      ex.printStackTrace();
    }
  }

  @Test
  void readNodesTest() throws IOException {
    NodeCsvReaderWriter nodeCsvReaderWriter = new NodeCsvReaderWriter();
    List<Node> nodes
        = nodeCsvReaderWriter.readNodes(Files.newBufferedReader(TEST_DATA_FILE.toPath()));

    assertAll(
        () -> assertEquals(3, nodes.size()),
        () -> assertTrue(nodes.contains(NODE_A)),
        () -> assertTrue(nodes.contains(NODE_B)),
        () -> assertTrue(nodes.contains(NODE_C))
    );
  }
}
