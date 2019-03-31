package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SanitationDaoDbTest {

  Node TEST_NODE = new Node("123", 0, 0, 0, "Central", Node.NodeType.CONF, "LONGNAME", "SHORTNAME");
  SanitationRequest TEST_SANITATION_REQUEST = new SanitationRequest(123, LocalDateTime.now(), LocalDateTime.now(),
      TEST_NODE, SanitationRequest.SanitationRequestType.BEDDING, "This is a description");

  Database db;

  @BeforeEach
  void setup(TestInfo testInfo) throws SQLException {
    assertDoesNotThrow(() -> db = new Database());
  }

  @Test
  void getTest() {
    db.insertSanitationRequest(TEST_SANITATION_REQUEST);
    assertTrue(db.getSanitationRequest(123).isPresent());
  }
}