package edu.wpi.cs3733.d19.teamO.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NodeTest {

  private static final Node TEST_NODE1 = new Node("TEST", 0, 0, "0", "Building", Node.NodeType.CONF,
      "Long Name", "Short Name");
  private static final Node TEST_NODE2 = new Node("TEST", 0, 0, "0", "Building", Node.NodeType.CONF,
      "Long Name", "Short Name");

  @Test
  void testEquals() {
    assertEquals(TEST_NODE1, TEST_NODE2);
  }
}
