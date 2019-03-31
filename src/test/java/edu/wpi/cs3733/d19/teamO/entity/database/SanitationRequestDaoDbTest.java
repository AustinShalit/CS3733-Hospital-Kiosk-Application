package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SanitationRequestDaoDbTest {
  int id = 123;

  Node TEST_NODE = new Node(id + "", 0, 0, 0, "Central", Node.NodeType.CONF, "LONGNAME", "SHORTNAME");
  SanitationRequest TEST_SANITATION_REQUEST = new SanitationRequest(id, LocalDateTime.now(), LocalDateTime.now(),
      TEST_NODE, SanitationRequest.SanitationRequestType.BEDDING, "This is a description");

  Database db;

  @BeforeEach
  void setup() {
    assertDoesNotThrow(() -> db = new Database());
  }


  @Test
  void getAndInsertTest() {
    db.insertNode(TEST_NODE);
    db.insertSanitationRequest(TEST_SANITATION_REQUEST);

    assertTrue(db.getSanitationRequest(id).isPresent());
    assertEquals(db.getSanitationRequest(id).get(), TEST_SANITATION_REQUEST);
  }
}