package edu.wpi.cs3733.d19.teamO.entity;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import javafx.scene.shape.Polygon;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.cs3733.d19.teamO.entity.Node;

class NodeTest {

  private static final Node TEST_NODE1 = new Node("TEST", 0, 0, "0", "Building", Node.NodeType.CONF,
      "Long Name", "Short Name");
  private static final Node TEST_NODE2 = new Node("TEST", 0, 0, "0", "Building", Node.NodeType.CONF,
      "Long Name", "Short Name");
  private static final String coords = "198 556;1032 558;1031 966;1144 1044;1144 1266;928 1266;928 1332;786 1332;786 1486;198 1482";
  @Test
  void testEquals() {
    assertEquals(TEST_NODE1, TEST_NODE2);
  }

  @Test
  void testParsePolygonCoords() {
    Polygon p = Node.parsePolygonFromString(coords);
    Double[] expected = new Double[]{198.0, 556.0, 1032.0, 558.0, 1031.0, 966.0, 1144.0, 1044.0, 1144.0, 1266.0, 928.0, 1266.0, 928.0, 1332.0, 786.0, 1332.0, 786.0, 1486.0, 198.0, 1482.0};
    Double[] actual = p.getPoints().toArray(new Double[20]);

    assertArrayEquals(expected, actual);
  }
}
