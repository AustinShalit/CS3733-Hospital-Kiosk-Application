package edu.wpi.cs3733.d19.teamO.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NodeTest {

  private static final Node TEST_NODE1 = new Node("TEST", 0, 0,
      "0", "Building", Node.NodeType.CONF,
      "Long Name", "Short Name");
  private static final Node TEST_NODE2 = new Node("TEST", 0, 0,
      "0", "Building", Node.NodeType.CONF,
      "Long Name", "Short Name");


  private static final String db_coords = "198.0 556.0;1032.0 558.0;1031.0 966.0;1144.0 1044.0;"
      + "1144.0 1266.0;928.0 1266.0;928.0 1332.0;786.0 1332.0;786.0 1486.0;198.0 1482.0";
  private static final Double[] list_coords = new Double[]{198.0, 556.0, 1032.0, 558.0, 1031.0,
      966.0, 1144.0, 1044.0, 1144.0, 1266.0, 928.0, 1266.0, 928.0, 1332.0, 786.0,
      1332.0, 786.0, 1486.0, 198.0, 1482.0};
  private static final Node TEST_NODE3 = new Node("TEST", 0, 0, "0",
      "Building", Node.NodeType.CONF,
      "Long Name", "Short Name", Node.parsePolygonFromString(db_coords));

  @Test
  void testEquals() {
    assertEquals(TEST_NODE1, TEST_NODE2);
  }

  @Test
  void testParsePolygonCoords() {
    assertArrayEquals(list_coords, TEST_NODE3.getPolygon().getPoints().toArray(new Double[20]));
  }

  @Test
  void testPolygonCoordsToString() {
    assertEquals(db_coords, TEST_NODE3.polygonCoordsToString());
  }
}
