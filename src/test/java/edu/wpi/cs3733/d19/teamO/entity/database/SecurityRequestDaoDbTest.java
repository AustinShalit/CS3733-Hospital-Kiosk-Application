package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SecurityRequest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityRequestDaoDbTest {

  private static final Node NODE_A = new Node("A", 1, 2, "0", "B", Node.NodeType.HALL,
      "AL", "AS");

  private static final SecurityRequest SECURITY_REQUEST
      = new SecurityRequest(1, LocalDateTime.now(),
      LocalDateTime.now(), "A", "D", NODE_A);
  private Database dao;

  @BeforeEach
  void setup(TestInfo testInfo) throws SQLException {
    DatabaseConnectionFactory dcf
        = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
        testInfo.getTestClass().get().getName()
            + testInfo.getDisplayName());

    dao = new Database(dcf);
    dao.insertNode(NODE_A);
  }

  @Test
  void getTest() {
    dao.insertSecurityRequest(SECURITY_REQUEST);

    assertTrue(dao.getSecurityRequest(SECURITY_REQUEST.getId()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    dao.insertSecurityRequest(SECURITY_REQUEST);

    assertNotSame(SECURITY_REQUEST,
        dao.getSecurityRequest(SECURITY_REQUEST.getId()).orElseThrow(IllegalStateException::new));
  }

  @Test
  void getNotExistingTest() {
    assertFalse(dao.getSecurityRequest(SECURITY_REQUEST.getId()).isPresent());
  }

  @Test
  void insertTest() {
    assertTrue(dao.insertSecurityRequest(SECURITY_REQUEST));
  }

  @Test
  void deleteTest() {
    dao.insertSecurityRequest(SECURITY_REQUEST);
    assertTrue(dao.deleteSecurityRequest(SECURITY_REQUEST));
  }

  @Test
  void deleteNotExistingTest() {
    assertFalse(dao.deleteSecurityRequest(SECURITY_REQUEST));
  }

  @Test
  void updateTest() {
    dao.insertSecurityRequest(SECURITY_REQUEST);
    SecurityRequest update = new SecurityRequest(SECURITY_REQUEST.getId(),
        LocalDateTime.now(), LocalDateTime.now(), "A", "D", NODE_A);
    assertTrue(dao.updateSecurity(update));
  }

  @Test
  void updateNotExistingTest() {
    assertFalse(dao.updateSecurity(SECURITY_REQUEST));
  }

  @Test
  void getAllTest() {
    dao.insertSecurityRequest(SECURITY_REQUEST);

    assertEquals(1, dao.getAllSecurityRequests().size());
  }

  @Test
  void getAllResultSameTest() {
    dao.insertSecurityRequest(SECURITY_REQUEST);

    assertEquals(SECURITY_REQUEST, dao.getAllSecurityRequests().toArray()[0]);
  }

  @Test
  void getAllEmptyTest() {
    assertTrue(dao.getAllSecurityRequests().isEmpty());
  }

  @Nested
  class Creation {
    @Test
    void createTableTest(TestInfo testInfo) {
      DatabaseConnectionFactory dcf
          = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
          testInfo.getDisplayName());

      assertDoesNotThrow(() -> new SecurityRequestDaoDb(dcf));
    }

    @Test
    void existingTableTest(TestInfo testInfo) {
      DatabaseConnectionFactory dcf
          = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
          testInfo.getDisplayName());
      assertDoesNotThrow(() -> new SecurityRequestDaoDb(dcf));
      assertDoesNotThrow(() -> new SecurityRequestDaoDb(dcf));
    }
  }

}
