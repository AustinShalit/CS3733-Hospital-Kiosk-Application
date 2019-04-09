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
  private SecurityRequestDao dao;

  @BeforeEach
  void setup(TestInfo testInfo) throws SQLException {
    DatabaseConnectionFactory dcf
        = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
        testInfo.getDisplayName());

    NodeDaoDb nodeDaoDb = new NodeDaoDb(dcf);
    nodeDaoDb.insert(NODE_A);

    dao = new SecurityRequestDaoDb(dcf);
  }

  @Test
  void getTest() {
    dao.insert(SECURITY_REQUEST);

    assertTrue(dao.get(SECURITY_REQUEST.getId()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    dao.insert(SECURITY_REQUEST);

    assertNotSame(SECURITY_REQUEST,
        dao.get(SECURITY_REQUEST.getId()).orElseThrow(IllegalStateException::new));
  }

  @Test
  void getNotExistingTest() {
    assertFalse(dao.get(SECURITY_REQUEST.getId()).isPresent());
  }

  @Test
  void insertTest() {
    assertTrue(dao.insert(SECURITY_REQUEST));
  }

  @Test
  void deleteTest() {
    dao.insert(SECURITY_REQUEST);
    assertTrue(dao.delete(SECURITY_REQUEST));
  }

  @Test
  void deleteNotExistingTest() {
    assertFalse(dao.delete(SECURITY_REQUEST));
  }

  @Test
  void updateTest() {
    dao.insert(SECURITY_REQUEST);
    SecurityRequest update = new SecurityRequest(SECURITY_REQUEST.getId(),
        LocalDateTime.now(), LocalDateTime.now(), "A", "D", NODE_A);
    assertTrue(dao.update(update));
  }

  @Test
  void updateNotExistingTest() {
    assertFalse(dao.update(SECURITY_REQUEST));
  }

  @Test
  void getAllTest() {
    dao.insert(SECURITY_REQUEST);

    assertEquals(1, dao.getAll().size());
  }

  @Test
  void getAllResultSameTest() {
    dao.insert(SECURITY_REQUEST);

    assertEquals(SECURITY_REQUEST, dao.getAll().toArray()[0]);
  }

  @Test
  void getAllEmptyTest() {
    assertTrue(dao.getAll().isEmpty());
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
