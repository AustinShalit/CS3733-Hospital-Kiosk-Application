package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SchedulingRequest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SchedulingRequestDaoDbTest {

  private static final Node nodeA = new Node("A", 1, 2, "0", "B",
      Node.NodeType.HALL, "AL", "AS");
  private static final LocalDateTime aStart = LocalDateTime.of(2019, 4, 1,
      3, 30);
  private static final LocalDateTime aEnd = LocalDateTime.of(2019, 4, 1,
      4, 30);
  private static final LocalDateTime aRequest = LocalDateTime.now();
  private static final LocalDateTime aComplete = LocalDateTime.now();

  private static final SchedulingRequest schedulingRequest = new SchedulingRequest(12, aStart, aEnd,
      aRequest, aComplete, "Dr. Owlman", nodeA);

  @Nested
  class Creation {
    @Test
    void createTableTest(TestInfo testInfo) {
      DatabaseConnectionFactory dcf
          = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
          testInfo.getDisplayName());

      assertDoesNotThrow(() -> new SchedulingRequestDaoDb(dcf));
    }

    @Test
    void existingTableTest(TestInfo testInfo) {
      DatabaseConnectionFactory dcf
          = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
          testInfo.getDisplayName());
      assertDoesNotThrow(() -> new SchedulingRequestDaoDb(dcf));
      assertDoesNotThrow(() -> new SchedulingRequestDaoDb(dcf));
    }
  }

  private SchedulingRequestDaoDb schedulingDao;

  @BeforeEach
  void setup(TestInfo testInfo) throws SQLException {
    DatabaseConnectionFactory dcf
        = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
        testInfo.getTestClass().get().getName()
            + testInfo.getDisplayName());

    NodeDao nodeDao = new NodeDaoDb(dcf);

    nodeDao.insert(nodeA);

    schedulingDao = new SchedulingRequestDaoDb(dcf);
  }

  @Test
  void getTest() {
    schedulingDao.insert(schedulingRequest);

    assertTrue(schedulingDao.get(schedulingRequest.getId()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    schedulingDao.insert(schedulingRequest);

    assertNotSame(schedulingRequest,
        schedulingDao.get(schedulingRequest.getId()).orElseThrow(IllegalStateException::new));
  }

  @Test
  void getNotExistingTest() {
    assertFalse(schedulingDao.get(schedulingRequest.getId()).isPresent());
  }

  @Test
  void insertTest() {
    assertTrue(schedulingDao.insert(schedulingRequest));
  }

  @Test
  void insertTwiceTest() {
    schedulingDao.insert(schedulingRequest);
    assertFalse(schedulingDao.insert(schedulingRequest));
  }

  @Test
  void deleteTest() {
    schedulingDao.insert(schedulingRequest);
    assertTrue(schedulingDao.delete(schedulingRequest));
  }

  @Test
  void deleteNotExistingTest() {
    assertFalse(schedulingDao.delete(schedulingRequest));
  }

  @Test
  void updateTest() {
    schedulingDao.insert(schedulingRequest);

    SchedulingRequest update = new SchedulingRequest(12, aStart, aEnd,
        LocalDateTime.now(), LocalDateTime.now(), "Dr. Owlman", nodeA);
    assertTrue(schedulingDao.update(update));
  }

  @Test
  void updateNotExistingTest() {
    assertFalse(schedulingDao.update(schedulingRequest));
  }

  @Test
  void getAllTest() {
    schedulingDao.insert(schedulingRequest);

    assertEquals(1, schedulingDao.getAll().size());
  }

  @Test
  void getAllResultSameTest() {
    schedulingDao.insert(schedulingRequest);
    
    assertEquals(schedulingRequest, schedulingDao.getAll().toArray()[0]);
  }

  @Test
  void getAllEmptyTest() {
    assertTrue(schedulingDao.getAll().isEmpty());
  }

}
