package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SanitationRequestDaoDbTest {

  private static final Node TEST_NODE = new Node("123", 0, 0, 0,
      "Central", Node.NodeType.CONF, "LongName", "ShortName");
  private static final SanitationRequest TEST_Sanitation_Request = new SanitationRequest(123,
      LocalDateTime.now(), LocalDateTime.now(), TEST_NODE,
      SanitationRequest.SanitationRequestType.BEDDING, "This is a description");
  private static final SanitationRequest Update_Sanitation_Request = new SanitationRequest(123,
      LocalDateTime.now(), LocalDateTime.now(), TEST_NODE,
      SanitationRequest.SanitationRequestType.SPILL, "This is a description");
  private static final SanitationRequest TEST_Sanitation_Request2 = new SanitationRequest(456,
      LocalDateTime.now(), LocalDateTime.now(), TEST_NODE,
      SanitationRequest.SanitationRequestType.SPILL, "This is a description");

  Database db;

  @BeforeEach
  void setup(TestInfo testInfo) throws SQLException {
    assertDoesNotThrow(() -> db = new Database());
    db.insertNode(TEST_NODE);
    db.insertSanitationRequest(TEST_Sanitation_Request);
  }


  @Test
  void getTest() {
    assertTrue(db.getSanitationRequest(123).isPresent());
  }

  @Test
  void deleteTest() {
    assertTrue(db.deleteSanitationRequest(TEST_Sanitation_Request));
  }

  @Test
  void updateTest() {
    assertTrue(db.updateSanitationRequest(Update_Sanitation_Request));
  }

  @Test
  void getAllTest() {
    db.insertSanitationRequest(TEST_Sanitation_Request2);
    assertEquals(2, db.sanitationRequestDaoDb.getAll().size());
  }
}
