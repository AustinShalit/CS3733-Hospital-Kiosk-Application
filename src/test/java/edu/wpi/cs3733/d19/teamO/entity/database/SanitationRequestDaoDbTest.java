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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SanitationRequestDaoDbTest {
  int id = 123;
  int id2 = 1234;

  Node TEST_NODE = new Node(id + "", 0, 0, 0, "Central", Node.NodeType.CONF, "LONGNAME", "SHORTNAME");
  Node TEST_NODE2 = new Node(id2 + "", 1, 1, 0, "Central", Node.NodeType.DEPT, "LONGNAME", "SHORTNAME");

  SanitationRequest TEST_SANITATION_REQUEST = new SanitationRequest(id, LocalDateTime.now(), LocalDateTime.now(),
      TEST_NODE, SanitationRequest.SanitationRequestType.BEDDING, "This is a description");

  SanitationRequest TEST_SANITATION_REQUEST2 = new SanitationRequest(id2, LocalDateTime.now(), LocalDateTime.now(),
      TEST_NODE2, SanitationRequest.SanitationRequestType.BEDDING, "This is a description");

  @Nested
  class Creation {
    @Test
    void createTableTest(TestInfo testInfo) {
      DatabaseConnectionFactory dcf
          = new DatabaseConnectionFactoryImpl(DatabaseConnectionFactoryImpl.MEMORY_PROTOCOL,
          testInfo.getDisplayName());

      assertDoesNotThrow(() -> new NodeDaoDb(dcf));
      assertDoesNotThrow(() -> new SanitationRequestDaoDb(dcf));
    }

    @Test
    void existingTableTest(TestInfo testInfo) {
      DatabaseConnectionFactory dcf
          = new DatabaseConnectionFactoryImpl(DatabaseConnectionFactoryImpl.MEMORY_PROTOCOL,
          testInfo.getDisplayName());

      assertDoesNotThrow(() -> new NodeDaoDb(dcf));

      assertDoesNotThrow(() -> new SanitationRequestDaoDb(dcf));
      assertDoesNotThrow(() -> new SanitationRequestDaoDb(dcf));
    }
  }

  private SanitationRequestDaoDb sanitationDao;
  private NodeDao nodeDao;

  @BeforeEach
  void setup(TestInfo testInfo) throws SQLException {
    DatabaseConnectionFactory dcf
        = new DatabaseConnectionFactoryImpl(DatabaseConnectionFactoryImpl.MEMORY_PROTOCOL,
        testInfo.getTestClass().get().getName()
            + testInfo.getDisplayName());

    nodeDao = new NodeDaoDb(dcf);
    SanitationRequestDaoDb sanitationRequestDaoDb = new SanitationRequestDaoDb(dcf);

    nodeDao.insert(TEST_NODE);
    sanitationRequestDaoDb.insert(TEST_SANITATION_REQUEST);


    sanitationDao = new SanitationRequestDaoDb(dcf);
  }

  @Test
  void getTest() {
    sanitationDao.insert(TEST_SANITATION_REQUEST);

    assertTrue(sanitationDao.get(TEST_SANITATION_REQUEST.getId()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    sanitationDao.insert(TEST_SANITATION_REQUEST);

    assertNotSame(TEST_SANITATION_REQUEST,
        sanitationDao.get(TEST_SANITATION_REQUEST.getId()).orElseThrow(IllegalStateException::new));
  }

  @Test
  void assertEquals() {
    sanitationDao.insert(TEST_SANITATION_REQUEST);

    assertTrue(TEST_SANITATION_REQUEST.equals(sanitationDao.get(TEST_SANITATION_REQUEST.getId()).get()));
  }

  @Test
  void getNotExistingTest() {
    assertFalse(sanitationDao.get(12345).isPresent());
  }

  @Test
  void insertTest() {
    nodeDao.insert(TEST_NODE2);

    assertTrue(sanitationDao.insert(TEST_SANITATION_REQUEST2));
  }

  @Test
  void insertTwiceTest() {
    sanitationDao.insert(TEST_SANITATION_REQUEST);
    assertFalse(sanitationDao.insert(TEST_SANITATION_REQUEST));
  }

  @Test
  void deleteTest() {
    sanitationDao.insert(TEST_SANITATION_REQUEST);
    assertTrue(sanitationDao.delete(TEST_SANITATION_REQUEST));
  }

  @Test
  void deleteNotExistingTest() {
    assertFalse(sanitationDao.delete(new SanitationRequest(123345667, null,
        null, null, null, null)));
  }

  @Test
  void updateTest() {
    sanitationDao.insert(TEST_SANITATION_REQUEST);

    SanitationRequest update = new SanitationRequest(TEST_SANITATION_REQUEST.getId(),
        LocalDateTime.now(), LocalDateTime.now(),
        TEST_NODE,
        SanitationRequest.SanitationRequestType.OTHERS,
        "A different description");

    assertTrue(sanitationDao.update(update));
    assertFalse(TEST_SANITATION_REQUEST.equals(sanitationDao.get(TEST_SANITATION_REQUEST.getId())));
  }

  @Test
  void updateNotExistingTest() {
    Node newNode = new Node("9876", 0, 0, 0, "",
        Node.NodeType.DEPT, "longname", "shortname");
    assertFalse(sanitationDao.update(new SanitationRequest(9876, LocalDateTime.now(),
        LocalDateTime.now(), newNode, SanitationRequest.SanitationRequestType.SPILL,
        "This request doesnt exist")));
  }

  @Test
  void getAllTest() {
    nodeDao.insert(TEST_NODE2);
    sanitationDao.insert(TEST_SANITATION_REQUEST2);

    assertTrue(2 == sanitationDao.getAll().size());
  }

  @Test
  void getAllResultSameTest() {
    sanitationDao.insert(TEST_SANITATION_REQUEST);

    assertTrue(TEST_SANITATION_REQUEST.equals(sanitationDao.getAll().toArray()[0]));
  }

  @Test
  void getAllEmptyTest() {
    sanitationDao.delete(TEST_SANITATION_REQUEST);

    assertTrue(sanitationDao.getAll().isEmpty());
  }
}