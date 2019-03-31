package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.Node;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EdgeDaoDbTest {

  private static final Node NODE_A = new Node("A", 1, 2, 0, "B", Node.NodeType.HALL,
      "AL", "AS");
  private static final Node NODE_B = new Node("B", 3, 4, 0, "B", Node.NodeType.HALL,
      "BL", "BS");
  private static final Node NODE_C = new Node("C", 5, 6, 0, "B", Node.NodeType.HALL,
      "CL", "CS");
  private static final Edge EDGE_AB = new Edge("AB", NODE_A, NODE_B);
  private static final Edge EDGE_BC = new Edge("BC", NODE_B, NODE_C);
  private static final Edge EDGE_CA = new Edge("CA", NODE_C, NODE_A);


  @Nested
  class Creation {
    @Test
    void createTableTest(TestInfo testInfo) {
      DatabaseConnectionFactory dcf
          = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
          testInfo.getDisplayName());

      assertDoesNotThrow(() -> new EdgeDaoDb(dcf));
    }

    @Test
    void existingTableTest(TestInfo testInfo) {
      DatabaseConnectionFactory dcf
          = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
          testInfo.getDisplayName());
      assertDoesNotThrow(() -> new EdgeDaoDb(dcf));
      assertDoesNotThrow(() -> new EdgeDaoDb(dcf));
    }
  }

  private EdgeDaoDb dao;

  @BeforeEach
  void setup(TestInfo testInfo) throws SQLException {
    DatabaseConnectionFactory dcf
        = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
        testInfo.getTestClass().get().getName()
            + testInfo.getDisplayName());
    NodeDaoDb nodeDaoDb = new NodeDaoDb(dcf);
    nodeDaoDb.insert(NODE_A);
    nodeDaoDb.insert(NODE_B);
    nodeDaoDb.insert(NODE_C);
    dao = new EdgeDaoDb(dcf);
  }

  @Test
  void getTest() {
    dao.insert(EDGE_AB);

    assertTrue(dao.get(EDGE_AB.getEdgeId()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    dao.insert(EDGE_AB);

    assertNotSame(EDGE_AB,
        dao.get(EDGE_AB.getEdgeId()).orElseThrow(IllegalStateException::new));
  }

  @Test
  void getNotExistingTest() {
    assertFalse(dao.get(EDGE_AB.getEdgeId()).isPresent());
  }

  @Test
  void insertTest() {
    assertTrue(dao.insert(EDGE_AB));
  }

  @Test
  void insertTwiceTest() {
    dao.insert(EDGE_AB);
    assertFalse(dao.insert(EDGE_AB));
  }

  @Test
  void deleteTest() {
    dao.insert(EDGE_AB);
    assertTrue(dao.delete(EDGE_AB));
  }

  @Test
  void deleteNotExistingTest() {
    assertFalse(dao.delete(EDGE_AB));
  }

  @Test
  void updateTest() {
    dao.insert(EDGE_AB);
    Edge update = new Edge(EDGE_AB.getEdgeId(), NODE_A, NODE_C);
    assertTrue(dao.update(update));
  }

  @Test
  void updateNotExistingTest() {
    assertFalse(dao.update(EDGE_AB));
  }

  @Test
  void getAllTest() {
    dao.insert(EDGE_AB);

    assertEquals(1, dao.getAll().size());
  }

  @Test
  void getAllResultSameTest() {
    dao.insert(EDGE_AB);

    assertEquals(EDGE_AB, dao.getAll().toArray()[0]);
  }

  @Test
  void getAllEmptyTest() {
    assertTrue(dao.getAll().isEmpty());
  }

  @Test
  void getForNodeTest() {
    dao.insert(EDGE_AB);
    dao.insert(EDGE_BC);
    dao.insert(EDGE_CA);

    Set<Edge> result = dao.getEdgesFor(NODE_A);
    assertAll(
        () -> assertTrue(result.contains(EDGE_AB)),
        () -> assertTrue(result.contains(EDGE_CA)),
        () -> assertEquals(2, result.size())
    );
  }
}
