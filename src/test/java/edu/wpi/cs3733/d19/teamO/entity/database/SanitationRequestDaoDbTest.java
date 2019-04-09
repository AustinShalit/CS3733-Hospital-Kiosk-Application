package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;

import com.google.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SanitationRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DatabaseExtension.class)
class SanitationRequestDaoDbTest {
  private static final int id1 = 123;
  private static final int id2 = 1234;

  private static final Node testNode1 = new Node(Integer.toString(id1), 0, 0,
      "FL2", "Central", Node.NodeType.CONF, "LONGNAME",
      "SHORTNAME");

  private static final Node testNode2 = new Node(Integer.toString(id2), 1, 1,
      "FL3", "Central", Node.NodeType.DEPT, "LONGNAME",
      "SHORTNAME");

  private static final SanitationRequest testSanitationRequest1 =
      new SanitationRequest(1, LocalDateTime.now(), LocalDateTime.now(), testNode1,
          "Bob", SanitationRequest.SanitationRequestType.BEDDING,
          "This is a description");

  private static final SanitationRequest testSanitationRequest2 =
      new SanitationRequest(2, LocalDateTime.now(), LocalDateTime.now(), testNode2,
          "Bill",
          SanitationRequest.SanitationRequestType.BEDDING, "This is a description");
  private SanitationRequestDaoDb sanitationDao;
  private NodeDao nodeDao;

  @Inject
  private DatabaseConnectionFactory dcf;

  @BeforeEach
  void setup() throws SQLException {
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
    assertFalse(sanitationDao.get(987).isPresent());
  }

  @Test
  void insertTest() {
    nodeDao.insert(testNode2);

    assertTrue(sanitationDao.insert(testSanitationRequest2));
  }

  @Test
  void deleteTest() {
    sanitationDao.insert(testSanitationRequest1);
    assertTrue(sanitationDao.delete(testSanitationRequest1));
  }

  @Test
  void deleteNotExistingTest() {
    assertFalse(sanitationDao.delete(new SanitationRequest(987, null,
        null, testNode1, null, null, null)));
  }

  @Test
  void updateTest() {
    sanitationDao.insert(testSanitationRequest1);

    SanitationRequest update = new SanitationRequest(testSanitationRequest1.getId(),
        LocalDateTime.now(), LocalDateTime.now(),
        testNode1, "Fred",
        SanitationRequest.SanitationRequestType.OTHERS,
        "A different description");

    assertTrue(sanitationDao.update(update));
    assertNotEquals(testSanitationRequest1, sanitationDao.get(testSanitationRequest1.getId()));
  }

  @Test
  void updateNotExistingTest() {
    Node newNode = new Node("9876", 0, 0, "2", "Fuller",
        Node.NodeType.DEPT, "longname", "shortname");
    assertFalse(sanitationDao.update(new SanitationRequest(987, LocalDateTime.now(),
        LocalDateTime.now(), newNode, "Jane",
        SanitationRequest.SanitationRequestType.SPILL, "This request doesnt exist")));
  }

  @Test
  void getAllTest() {
    nodeDao.insert(testNode2);
    sanitationDao.insert(testSanitationRequest2);

    assertEquals(2, sanitationDao.getAll().size());
  }

  @Test
  void getAllEmptyTest() {
    sanitationDao.delete(testSanitationRequest1);

    assertTrue(sanitationDao.getAll().isEmpty());
  }

  @Test
  void autoIncrementTest() {
    for (int i = 0; i < 10; i++) {
      sanitationDao.insert(testSanitationRequest1);
    }

    Set<SanitationRequest> sanitationRequestSet = sanitationDao.getAll();
    for (SanitationRequest sr : sanitationRequestSet) {
      // make sure the id is in the correct range
      assertTrue(sr.getId() < 11 || sr.getId() > 1);
    }
  }
}
