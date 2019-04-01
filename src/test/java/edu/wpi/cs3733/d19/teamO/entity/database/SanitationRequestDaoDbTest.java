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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SanitationRequestDaoDbTest {
  int id = 123;
  int id2 =1234;

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
    sanitationRequestDaoDb.insert(TEST_SANITATION_REQUEST2);
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
    assertTrue(sanitationDao.get(TEST_SANITATION_REQUEST.getId()).isPresent());
    assertFalse(sanitationDao.delete(new SanitationRequest(123345667, null, null, null, null, null)));
    assertFalse(sanitationDao.delete(TEST_SANITATION_REQUEST));
  }

//  @Test
//  void updateTest() {
//    sanitationDao.insert(TEST_SANITATION_REQUEST);
//    SanitationRequest update = new SanitationRequest(TEST_SANITATION_REQUEST.getId(), NODE_A, NODE_C);
//    assertTrue(dao.update(update));
//  }
//
//  @Test
//  void updateNotExistingTest() {
//    assertFalse(dao.update(TEST_SANITATION_REQUEST));
//  }
//
//  @Test
//  void getAllTest() {
//    dao.insert(TEST_SANITATION_REQUEST);
//
//    assertEquals(1, dao.getAll().size());
//  }
//
//  @Test
//  void getAllResultSameTest() {
//    dao.insert(TEST_SANITATION_REQUEST);
//
//    assertEquals(TEST_SANITATION_REQUEST, dao.getAll().toArray()[0]);
//  }
//
//  @Test
//  void getAllEmptyTest() {
//    assertTrue(dao.getAll().isEmpty());
//  }
  
  






//  Database db;
//
//  @BeforeEach
//  void setup() {
//    assertDoesNotThrow(() -> db = new Database());
//
//
//  }
//
//  @Test
//  void insertTest() {
//    db.insertNode(TEST_NODE);
//    assertTrue(db.insertSanitationRequest(TEST_SANITATION_REQUEST));
//  }
//
//  @Test
//  void getTest() {
//    assertTrue(db.getSanitationRequest(id).isPresent());
//  }
//
//  @Test
//  void deleteTest() {
//    assertTrue(db.deleteSanitationRequest(TEST_SANITATION_REQUEST));
//    assertFalse(db.getSanitationRequest(id).isPresent());
//  }
//
//  @Test
//  void updateTest() {
////    assertTrue(db.updateSanitationRequest(Update_Sanitation_Request));
//  }
//
//  @Test
//  void getAllTest() {
//    db.insertSanitationRequest(TEST_SANITATION_REQUEST2);
//    assertEquals(2, db.sanitationRequestDaoDb.getAll().size());
  }


//  @Test
//  void getAndInsertTest() {
//    db.insertNode(TEST_NODE);
//    db.insertSanitationRequest(TEST_SANITATION_REQUEST);
//
//    assertTrue(db.getSanitationRequest(id).isPresent());
//    assertEquals(db.getSanitationRequest(id).get(), TEST_SANITATION_REQUEST);
//  }

