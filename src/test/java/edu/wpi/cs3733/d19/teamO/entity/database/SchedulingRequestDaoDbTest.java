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

  private static final SchedulingRequest schedulingRequest = new SchedulingRequest(12,
      aStart, aEnd, aRequest, aComplete, "Dr. Owlmann", nodeA);

  private Database database;

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

  @BeforeEach
  void setup(TestInfo testInfo) throws SQLException {
    DatabaseConnectionFactory dcf
        = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
        testInfo.getTestClass().get().getName()
            + testInfo.getDisplayName());

    database = new Database(dcf);
    database.insertNode(nodeA);
  }

  @Test
  void getTest() {
    database.insertSchedulingrequest(schedulingRequest);

    assertTrue(database.getSchedulingRequest(schedulingRequest.getId()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    database.insertSchedulingrequest(schedulingRequest);

    assertNotSame(schedulingRequest,
        database.getSchedulingRequest(
            schedulingRequest.getId()).orElseThrow(IllegalStateException::new));
  }

  @Test
  void getNotExistingTest() {
    assertFalse(database.getSchedulingRequest(schedulingRequest.getId()).isPresent());
  }

  @Test
  void insertTest() {
    assertTrue(database.insertSchedulingrequest(schedulingRequest));
  }

  @Test
  void deleteTest() {
    database.insertSchedulingrequest(schedulingRequest);
    assertTrue(database.deleteSchedulingRequest(schedulingRequest));
  }

  @Test
  void deleteNotExistingTest() {
    assertFalse(database.deleteSchedulingRequest(schedulingRequest));
  }

  @Test
  void updateTest() {
    database.insertSchedulingrequest(schedulingRequest);
    SchedulingRequest update = new SchedulingRequest(schedulingRequest.getId(), aStart, aEnd,
        LocalDateTime.now(), LocalDateTime.now(), "D", nodeA);
    assertTrue(database.updateScheduling(update));
  }

  @Test
  void updateNotExistingTest() {
    assertFalse(database.updateScheduling(schedulingRequest));
  }

  @Test
  void getAllTest() {
    database.insertSchedulingrequest(schedulingRequest);

    assertEquals(1, database.getAllSchedulingRequests().size());
  }

  @Test
  void getAllResultSameTest() {
    database.insertSchedulingrequest(schedulingRequest);
    assertTrue(database.getAllSchedulingRequests().contains(schedulingRequest));
  }

  @Test
  void getAllEmptyTest() {
    assertTrue(database.getAllSchedulingRequests().isEmpty());
  }

}
