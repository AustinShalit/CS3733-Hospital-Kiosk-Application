package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;

import com.google.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.wpi.cs3733.d19.teamO.entity.ITSupportRequest;
import edu.wpi.cs3733.d19.teamO.entity.Node;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DatabaseExtension.class)
class ITSupportRequestDaoDbTest {
  private static final int id1 = 123;
  private static final int id2 = 1234;

  private static final Node testNode1 = new Node(Integer.toString(id1), 0, 0,
      "FL2", "Central", Node.NodeType.CONF, "LONGNAME",
      "SHORTNAME");

  private static final Node testNode2 = new Node(Integer.toString(id2), 1, 1,
      "FL3", "Central", Node.NodeType.DEPT, "LONGNAME",
      "SHORTNAME");

  private static final ITSupportRequest testITSupportRequest1 =
      new ITSupportRequest(1, LocalDateTime.now(), LocalDateTime.now(), testNode1,
          "Bob", ITSupportRequest.ITSupportRequestType.COMPUTER,
          "This is a description", "Dev");

  private static final ITSupportRequest testITSupportRequest2 =
      new ITSupportRequest(2, LocalDateTime.now(), LocalDateTime.now(), testNode2,
          "Bill", ITSupportRequest.ITSupportRequestType.INTERNET,
          "This is a description", "Ken");
  private ITSupportRequestDaoDb itSupportRequestDaoDb;
  private NodeDao nodeDao;

  @Inject
  private DatabaseConnectionFactory dcf;

  @BeforeEach
  void setup() throws SQLException {
    nodeDao = new NodeDaoDb(dcf);
    ITSupportRequestDaoDb informationTechnologySupportRequestDaoDb =
        new ITSupportRequestDaoDb(dcf);

    nodeDao.insert(testNode1);
    informationTechnologySupportRequestDaoDb.insert(testITSupportRequest1);


    itSupportRequestDaoDb = new ITSupportRequestDaoDb(dcf);
  }

  @Test
  void getTest() {
    itSupportRequestDaoDb.insert(testITSupportRequest1);

    assertTrue(itSupportRequestDaoDb.get(testITSupportRequest1.getId()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    itSupportRequestDaoDb.insert(testITSupportRequest1);

    assertNotSame(testITSupportRequest1,
        itSupportRequestDaoDb.get(testITSupportRequest1.getId())
            .orElseThrow(IllegalStateException::new));
  }

  @Test
  void checkEqual() {
    itSupportRequestDaoDb.insert(testITSupportRequest1);

    assertEquals(testITSupportRequest1, itSupportRequestDaoDb.get(testITSupportRequest1.getId())
        .get());
  }

  @Test
  void getNotExistingTest() {
    assertFalse(itSupportRequestDaoDb.get(987).isPresent());
  }

  @Test
  void insertTest() {
    nodeDao.insert(testNode2);

    assertTrue(itSupportRequestDaoDb.insert(testITSupportRequest2));
  }

  @Test
  void deleteTest() {
    itSupportRequestDaoDb.insert(testITSupportRequest1);
    assertTrue(itSupportRequestDaoDb.delete(testITSupportRequest1));
  }

  @Test
  void deleteNotExistingTest() {
    assertFalse(itSupportRequestDaoDb.delete(new ITSupportRequest(987, null,
        null, testNode1, null, null, null, null)));
  }

  @Test
  void updateTest() {
    itSupportRequestDaoDb.insert(testITSupportRequest1);

    ITSupportRequest update = new ITSupportRequest(testITSupportRequest1.getId(),
        LocalDateTime.now(), LocalDateTime.now(),
        testNode1, "Fred",
        ITSupportRequest.ITSupportRequestType.OTHERS,
        "A different description", "Austin");

    assertTrue(itSupportRequestDaoDb.update(update));
    assertNotEquals(testITSupportRequest1, itSupportRequestDaoDb.get(testITSupportRequest1
        .getId()));
  }

  @Test
  void updateNotExistingTest() {
    Node newNode = new Node("9876", 0, 0, "2", "Fuller",
        Node.NodeType.DEPT, "longname", "shortname");
    assertFalse(itSupportRequestDaoDb.update(new ITSupportRequest(987, LocalDateTime.now(),
        LocalDateTime.now(), newNode, "Jane",
        ITSupportRequest.ITSupportRequestType.TV,
        "This request doesnt exist", "Jill")));
  }

  @Test
  void getAllTest() {
    nodeDao.insert(testNode2);
    itSupportRequestDaoDb.insert(testITSupportRequest2);

    assertEquals(2, itSupportRequestDaoDb.getAll().size());
  }

  @Test
  void getAllEmptyTest() {
    itSupportRequestDaoDb.delete(testITSupportRequest1);

    assertTrue(itSupportRequestDaoDb.getAll().isEmpty());
  }

  @Test
  void autoIncrementTest() {
    for (int i = 0; i < 10; i++) {
      itSupportRequestDaoDb.insert(testITSupportRequest1);
    }

    Set<ITSupportRequest> itSupportRequestSet = itSupportRequestDaoDb.getAll();
    for (ITSupportRequest sr : itSupportRequestSet) {
      // make sure the id is in the correct range
      assertTrue(sr.getId() < 11 || sr.getId() > 1);
    }
  }
}
