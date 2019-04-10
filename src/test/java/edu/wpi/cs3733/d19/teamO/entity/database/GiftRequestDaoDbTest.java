package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;

import com.google.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.wpi.cs3733.d19.teamO.entity.GiftRequest;
import edu.wpi.cs3733.d19.teamO.entity.Node;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DatabaseExtension.class)
class GiftRequestDaoDbTest {
  private static final int id1 = 123;
  private static final int id2 = 1234;

  private static final Node testNode1 = new Node(Integer.toString(id1), 0, 0,
      "FL2", "Central", Node.NodeType.CONF, "LONGNAME",
      "SHORTNAME");

  private static final Node testNode2 = new Node(Integer.toString(id2), 1, 1,
      "FL3", "Central", Node.NodeType.DEPT, "LONGNAME",
      "SHORTNAME");

  private static final GiftRequest testITRequest1 =
      new GiftRequest(1, LocalDateTime.now(), LocalDateTime.now(), testNode1,
          "Bob", GiftRequest.GiftRequestType.TEDDYBEAR,
          "This is a description", "Dev");

  private static final GiftRequest testITRequest2 =
      new GiftRequest(2, LocalDateTime.now(), LocalDateTime.now(), testNode2,
          "Bill", GiftRequest.GiftRequestType.BALLOONS,
          "This is a description", "Ken");
  private GiftRequestDaoDb giftRequestDaoDb;
  private NodeDao nodeDao;

  @Inject
  private DatabaseConnectionFactory dcf;

  @BeforeEach
  void setup() throws SQLException {
    nodeDao = new NodeDaoDb(dcf);

    nodeDao.insert(testNode1);
    giftRequestDaoDb = new GiftRequestDaoDb(dcf);
    giftRequestDaoDb.insert(testITRequest1);
  }

  @Test
  void getTest() {
    giftRequestDaoDb.insert(testITRequest1);

    assertTrue(giftRequestDaoDb.get(testITRequest1.getId()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    giftRequestDaoDb.insert(testITRequest1);

    assertNotSame(testITRequest1,
        giftRequestDaoDb.get(testITRequest1.getId()).orElseThrow(IllegalStateException::new));
  }

  @Test
  void checkEqual() {
    giftRequestDaoDb.insert(testITRequest1);

    assertEquals(testITRequest1, giftRequestDaoDb.get(testITRequest1.getId()).get());
  }

  @Test
  void getNotExistingTest() {
    assertFalse(giftRequestDaoDb.get(987).isPresent());
  }

  @Test
  void insertTest() {
    nodeDao.insert(testNode2);

    assertTrue(giftRequestDaoDb.insert(testITRequest2));
  }

  @Test
  void deleteTest() {
    giftRequestDaoDb.insert(testITRequest1);
    assertTrue(giftRequestDaoDb.delete(testITRequest1));
  }

  @Test
  void deleteNotExistingTest() {
    assertFalse(giftRequestDaoDb.delete(new GiftRequest(987, null,
        null, testNode1, null, null, null, null)));
  }

  @Test
  void updateTest() {
    giftRequestDaoDb.insert(testITRequest1);

    GiftRequest update = new GiftRequest(testITRequest1.getId(),
        LocalDateTime.now(), LocalDateTime.now(),
        testNode1, "Fred",
        GiftRequest.GiftRequestType.OTHERS,
        "A different description", "Austin");

    assertTrue(giftRequestDaoDb.update(update));
    assertNotEquals(testITRequest1, giftRequestDaoDb.get(testITRequest1.getId()));
  }

  @Test
  void updateNotExistingTest() {
    Node newNode = new Node("9876", 0, 0, "2", "Fuller",
        Node.NodeType.DEPT, "longname", "shortname");
    assertFalse(giftRequestDaoDb.update(new GiftRequest(987, LocalDateTime.now(),
        LocalDateTime.now(), newNode, "Jane",
        GiftRequest.GiftRequestType.TEDDYBEAR,
        "This request doesnt exist", "Jill")));
  }

  @Test
  void getAllTest() {
    nodeDao.insert(testNode2);
    giftRequestDaoDb.insert(testITRequest2);

    assertEquals(2, giftRequestDaoDb.getAll().size());
  }

  @Test
  void getAllEmptyTest() {
    giftRequestDaoDb.delete(testITRequest1);

    assertTrue(giftRequestDaoDb.getAll().isEmpty());
  }

  @Test
  void autoIncrementTest() {
    for (int i = 0; i < 10; i++) {
      giftRequestDaoDb.insert(testITRequest1);
    }

    Set<GiftRequest> internalTransportationRequestSet = giftRequestDaoDb.getAll();
    for (GiftRequest sr : internalTransportationRequestSet) {
      // make sure the id is in the correct range
      assertTrue(sr.getId() < 11 || sr.getId() > 1);
    }
  }

  @Nested
  class Creation {
    @Test
    void createTableTest(TestInfo testInfo) {
      DatabaseConnectionFactory dcf
          = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
          testInfo.getDisplayName());

      assertDoesNotThrow(() -> new NodeDaoDb(dcf));
      assertDoesNotThrow(() -> new GiftRequestDaoDb(dcf));
    }

    @Test
    void existingTableTest(TestInfo testInfo) {
      DatabaseConnectionFactory dcf
          = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
          testInfo.getDisplayName());

      assertDoesNotThrow(() -> new NodeDaoDb(dcf));

      assertDoesNotThrow(() -> new GiftRequestDaoDb(dcf));
      assertDoesNotThrow(() -> new GiftRequestDaoDb(dcf));
    }
  }

  @Test
  void categoryTest(){
    nodeDao.insert(testNode2);
    giftRequestDaoDb.insert(testITRequest1);
    giftRequestDaoDb.insert(testITRequest1);
    giftRequestDaoDb.insert(testITRequest2);
    assertEquals(3, giftRequestDaoDb.getNumberByCategory("TEDDYBEAR").size());
  }
}
