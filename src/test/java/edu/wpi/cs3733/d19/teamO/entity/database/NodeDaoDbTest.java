package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;

import com.google.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.wpi.cs3733.d19.teamO.entity.Node;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DatabaseExtension.class)
class NodeDaoDbTest {

  private static final Node TEST_NODE = new Node("TEST", 0, 0, "0", "B", Node.NodeType.CONF,
      "LN", "SN");
  private static final Node TEST_NODE2 = new Node("123", 0, 0, "L2", "B", Node.NodeType.CONF,
      "LN", "SN");
  private static final Node TEST_NODE3 = new Node("456", 0, 0, "L2", "B", Node.NodeType.HALL,
      "LN", "SN");

  private NodeDao dao;

  @Inject
  private DatabaseConnectionFactory dcf;

  @BeforeEach
  void setup() throws SQLException {
    dao = new NodeDaoDb(dcf);
  }

  @Test
  void getTest() {
    dao.insert(TEST_NODE);

    assertTrue(dao.get(TEST_NODE.getNodeId()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    dao.insert(TEST_NODE);

    assertNotSame(TEST_NODE,
        dao.get(TEST_NODE.getNodeId()).orElseThrow(IllegalStateException::new));
  }

  @Test
  void getNotExistingTest() {
    assertFalse(dao.get(TEST_NODE.getNodeId()).isPresent());
  }

  @Test
  void insertTest() {
    assertTrue(dao.insert(TEST_NODE));
  }

  @Test
  void insertTwiceTest() {
    dao.insert(TEST_NODE);
    assertFalse(dao.insert(TEST_NODE));
  }

  @Test
  void deleteTest() {
    dao.insert(TEST_NODE);
    assertTrue(dao.delete(TEST_NODE));
  }

  @Test
  void deleteNotExistingTest() {
    assertFalse(dao.delete(TEST_NODE));
  }

  @Test
  void updateTest() {
    dao.insert(TEST_NODE);
    Node update = new Node(TEST_NODE.getNodeId(), 1, 2, "3", "Shack", Node.NodeType.HALL,
        "Looong Name", "s.name");
    assertTrue(dao.update(update));
  }

  @Test
  void updateNotExistingTest() {
    assertFalse(dao.update(TEST_NODE));
  }

  @Test
  void getAllTest() {
    dao.insert(TEST_NODE);

    assertEquals(1, dao.getAll().size());
  }

  @Test
  void getAllResultSameTest() {
    dao.insert(TEST_NODE);

    assertEquals(TEST_NODE, dao.getAll().toArray()[0]);
  }

  @Test
  void getAllEmptyTest() {
    assertTrue(dao.getAll().isEmpty());
  }

  @Test
  void getAllRoomTest() {
    dao.insert(TEST_NODE);
    dao.insert(TEST_NODE2);
    dao.insert(TEST_NODE3);
    assertEquals(2, dao.getAllRooms("CONF").size());
  }


  @Test
  void getAllRoomTest2() {
    dao.insert(TEST_NODE);
    dao.insert(TEST_NODE2);
    dao.insert(TEST_NODE3);
    dao.update(new Node("456", 0, 0, "0", "B", Node.NodeType.BATH,
        "LN", "SN"));
    assertEquals(1, dao.getAllRooms("BATH").size());
  }

  @Test
  void getFloor() {
    dao.insert(TEST_NODE);
    dao.insert(TEST_NODE2);
    dao.insert(TEST_NODE3);
    assertEquals(2, dao.getFloor("L2").size());
  }
}
