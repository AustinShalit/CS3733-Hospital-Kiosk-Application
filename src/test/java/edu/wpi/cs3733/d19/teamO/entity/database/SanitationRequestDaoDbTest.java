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
  private static final int id1 = 123;
  private static final int id2 = 1234;

  private static final Node testNode1 = new Node(Integer.toString(id1), 0, 0,
      0, "Central", Node.NodeType.CONF, "LONGNAME", "SHORTNAME");

  private static final Node testNode2 = new Node(Integer.toString(id2), 1, 1,
      0, "Central", Node.NodeType.DEPT, "LONGNAME", "SHORTNAME");

  private static final SanitationRequest testSanitationRequest1 =
      new SanitationRequest(id1, LocalDateTime.now(), LocalDateTime.now(), testNode1,
          SanitationRequest.SanitationRequestType.BEDDING, "This is a description");

  private static final SanitationRequest testSanitationRequest2 =
      new SanitationRequest(id2, LocalDateTime.now(), LocalDateTime.now(), testNode2,
          SanitationRequest.SanitationRequestType.BEDDING, "This is a description");

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

    nodeDao.insert(testNode1);
    sanitationRequestDaoDb.insert(testSanitationRequest1);


    sanitationDao = new SanitationRequestDaoDb(dcf);
  }

  @Test
  void getTest() {
    sanitationDao.insert(testSanitationRequest1);

    assertTrue(sanitationDao.get(testSanitationRequest1.getId()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    sanitationDao.insert(testSanitationRequest1);

    assertNotSame(testSanitationRequest1,
        sanitationDao.get(testSanitationRequest1.getId()).orElseThrow(IllegalStateException::new));
  }

  @Test
  void checkEqual() {
    sanitationDao.insert(testSanitationRequest1);

    assertEquals(testSanitationRequest1, sanitationDao.get(testSanitationRequest1.getId()).get());
  }

  @Test
  void getNotExistingTest() {
    assertFalse(sanitationDao.get(12345).isPresent());
  }

  @Test
  void insertTest() {
    nodeDao.insert(testNode2);

    assertTrue(sanitationDao.insert(testSanitationRequest2));
  }

  @Test
  void insertTwiceTest() {
    sanitationDao.insert(testSanitationRequest1);
    assertFalse(sanitationDao.insert(testSanitationRequest1));
  }

  @Test
  void deleteTest() {
    sanitationDao.insert(testSanitationRequest1);
    assertTrue(sanitationDao.delete(testSanitationRequest1));
  }

  @Test
  void deleteNotExistingTest() {
    assertFalse(sanitationDao.delete(new SanitationRequest(123345667, null,
        null, null, null, null)));
  }

  @Test
  void updateTest() {
    sanitationDao.insert(testSanitationRequest1);

    SanitationRequest update = new SanitationRequest(testSanitationRequest1.getId(),
        LocalDateTime.now(), LocalDateTime.now(),
        testNode1,
        SanitationRequest.SanitationRequestType.OTHERS,
        "A different description");

    assertTrue(sanitationDao.update(update));
    assertNotEquals(testSanitationRequest1, sanitationDao.get(testSanitationRequest1.getId()));
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
    nodeDao.insert(testNode2);
    sanitationDao.insert(testSanitationRequest2);

    assertEquals(2, sanitationDao.getAll().size());
  }

  @Test
  void getAllResultSameTest() {
    sanitationDao.insert(testSanitationRequest1);

    assertEquals(testSanitationRequest1, sanitationDao.getAll().toArray()[0]);
  }

  @Test
  void getAllEmptyTest() {
    sanitationDao.delete(testSanitationRequest1);

    assertTrue(sanitationDao.getAll().isEmpty());
  }
}
