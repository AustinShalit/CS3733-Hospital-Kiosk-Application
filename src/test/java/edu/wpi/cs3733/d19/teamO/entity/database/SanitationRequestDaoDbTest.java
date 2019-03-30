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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SanitationRequestDaoDbTest {

  private static final Node TEST_NODE = new Node("TEST", 0, 0, 0, "Building", Node.NodeType.CONF,
      "Long Name", "Short Name");

  private static final SanitationRequest TEST_SANITATION_REQUEST = new SanitationRequest(12345, LocalDateTime.MIN, LocalDateTime.now(), TEST_NODE, SanitationRequest.SanitationRequestType.BEDDING, "This is a description");

  @Nested
  class Creation {
    @Test
    void createTableTest(TestInfo testInfo) {
      DatabaseConnectionFactory dcf
          = new DatabaseConnectionFactoryImpl(DatabaseConnectionFactoryImpl.MEMORY_PROTOCOL,
          testInfo.getDisplayName());

      assertDoesNotThrow(() -> new SanitationRequestDaoDb(dcf));
    }

    @Test
    void existingTableTest(TestInfo testInfo) {
      DatabaseConnectionFactory dcf
          = new DatabaseConnectionFactoryImpl(DatabaseConnectionFactoryImpl.MEMORY_PROTOCOL,
          testInfo.getDisplayName());
      assertDoesNotThrow(() -> new SanitationRequestDaoDb(dcf));
      assertDoesNotThrow(() -> new SanitationRequestDaoDb(dcf));
    }
  }

  private SanitationRequestDaoDb sdao;

  @BeforeEach
  void setup(TestInfo testInfo) throws SQLException {
    DatabaseConnectionFactory dcf
        = new DatabaseConnectionFactoryImpl(DatabaseConnectionFactoryImpl.MEMORY_PROTOCOL,
        testInfo.getDisplayName());
    sdao = new SanitationRequestDaoDb(dcf);
  }

  @Test
  void getTest() {
    sdao.insert(TEST_SANITATION_REQUEST);

    assertTrue(sdao.get(TEST_SANITATION_REQUEST.getId()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    sdao.insert(TEST_SANITATION_REQUEST);

    assertNotSame(TEST_SANITATION_REQUEST,
        sdao.get(TEST_SANITATION_REQUEST.getId()).orElseThrow(IllegalStateException::new));
  }

  @Test
  void getNotExistingTest() {
    assertFalse(sdao.get(TEST_SANITATION_REQUEST.getId()).isPresent());
  }

}
