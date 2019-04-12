package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.time.LocalDateTime;

import com.google.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SchedulingRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DatabaseExtension.class)
class SchedulingRequestDaoDbTest {

  private static final Node nodeA = new Node("A", 1, 2, "0", "B",
      Node.NodeType.CLASSROOM, "AL", "AS");
  private static final LocalDateTime aStart = LocalDateTime.of(2019, 4, 1,
      3, 30);
  private static final LocalDateTime aEnd = LocalDateTime.of(2019, 4, 1,
      4, 30);
  private static final LocalDateTime aRequest = LocalDateTime.now();
  private static final LocalDateTime aComplete = LocalDateTime.now();

  private static final SchedulingRequest schedulingRequest = new SchedulingRequest(12,
      aStart, aEnd, aRequest, aComplete, "Dr. Owlmann", nodeA);

  private static final SchedulingRequest schedulingRequest2 = new SchedulingRequest(12,
      LocalDateTime.now().minusHours(1),
      LocalDateTime.now().plusHours(1), aRequest, aComplete, "Dr. Owlmann", nodeA);

  private SchedulingRequestDao sanitationDao;
  private NodeDao nodeDao;

  @Inject
  private DatabaseConnectionFactory dcf;

  @BeforeEach
  void setup() throws SQLException {
    nodeDao = new NodeDaoDb(dcf);
    nodeDao.insert(nodeA);

    sanitationDao = new SchedulingRequestDaoDb(dcf);
  }

  @Test
  void getTest() {
    sanitationDao.insert(schedulingRequest);

    assertTrue(sanitationDao.get(schedulingRequest.getId()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    sanitationDao.insert(schedulingRequest);

    assertNotSame(schedulingRequest,
        sanitationDao.get(
            schedulingRequest.getId()).orElseThrow(IllegalStateException::new));
  }

  @Test
  void getNotExistingTest() {
    assertFalse(sanitationDao.get(schedulingRequest.getId()).isPresent());
  }

  @Test
  void insertTest() {
    assertTrue(sanitationDao.insert(schedulingRequest));
  }

  @Test
  void deleteTest() {
    sanitationDao.insert(schedulingRequest);
    assertTrue(sanitationDao.delete(schedulingRequest));
  }

  @Test
  void deleteNotExistingTest() {
    assertFalse(sanitationDao.delete(schedulingRequest));
  }

  @Test
  void updateTest() {
    sanitationDao.insert(schedulingRequest);
    SchedulingRequest update = new SchedulingRequest(schedulingRequest.getId(), aStart, aEnd,
        LocalDateTime.now(), LocalDateTime.now(), "D", nodeA);
    assertTrue(sanitationDao.update(update));
  }

  @Test
  void updateNotExistingTest() {
    assertFalse(sanitationDao.update(schedulingRequest));
  }

  @Test
  void getAllTest() {
    sanitationDao.insert(schedulingRequest);

    assertEquals(1, sanitationDao.getAll().size());
  }

  @Test
  void getAllResultSameTest() {
    sanitationDao.insert(schedulingRequest);
    assertTrue(sanitationDao.getAll().contains(schedulingRequest));
  }

  @Test
  void getAllEmptyTest() {
    assertTrue(sanitationDao.getAll().isEmpty());
  }

  @Test
  void wouldConflictTest() {
    sanitationDao.insert(schedulingRequest);

    Node nodeB = new Node("B", 4, 6, "1", "B",
        Node.NodeType.CLASSROOM, "BL", "BS");

    nodeDao.insert(nodeB);

    // Starts during schedulingRequest and ends after schedulingRequest
    SchedulingRequest conflictingSchedulingRequest1 = new SchedulingRequest(
        2,
        LocalDateTime.of(2019, 4, 1, 4, 0),
        LocalDateTime.of(2019, 4, 1, 5, 0),
        LocalDateTime.now(),
        LocalDateTime.now(),
        "Bob",
        nodeA);

    assertTrue(sanitationDao.wouldConflict(conflictingSchedulingRequest1));
    assertFalse(sanitationDao.insert(conflictingSchedulingRequest1));

    // Starts before schedulingRequest and ends after schedulingRequest
    SchedulingRequest conflictingSchedulingRequest2 = new SchedulingRequest(
        2,
        LocalDateTime.of(2019, 4, 1, 2, 0),
        LocalDateTime.of(2019, 4, 1, 6, 0),
        LocalDateTime.now(),
        LocalDateTime.now(),
        "Bob",
        nodeA);

    assertTrue(sanitationDao.wouldConflict(conflictingSchedulingRequest2));
    assertFalse(sanitationDao.insert(conflictingSchedulingRequest2));

    // Ends at the same time schedulingRequest starts
    SchedulingRequest nonConflictingSchedulingRequest1 = new SchedulingRequest(
        2,
        LocalDateTime.of(2019, 4, 1, 2, 0),
        LocalDateTime.of(2019, 4, 1, 3, 30),
        LocalDateTime.now(),
        LocalDateTime.now(),
        "Bob",
        nodeA);

    assertFalse(sanitationDao.wouldConflict(nonConflictingSchedulingRequest1));
    assertTrue(sanitationDao.insert(nonConflictingSchedulingRequest1));

    // Same time as schedulingRequest but in a different room
    SchedulingRequest nonConflictingSchedulingRequest2 = new SchedulingRequest(
        2,
        LocalDateTime.of(2019, 4, 1, 3, 30),
        LocalDateTime.of(2019, 4, 1, 4, 30),
        LocalDateTime.now(),
        LocalDateTime.now(),
        "Bob",
        nodeB);

    assertFalse(sanitationDao.wouldConflict(nonConflictingSchedulingRequest2));
    assertTrue(sanitationDao.insert(nonConflictingSchedulingRequest2));
  }

  @Test
  void isDuringTest() {
    assertFalse(schedulingRequest.isDuring(LocalDateTime.now()));
    assertTrue(schedulingRequest2.isDuring(LocalDateTime.now()));
    assertFalse(schedulingRequest2.isDuring(
        LocalDateTime.now().minusHours(4),
        LocalDateTime.now().minusHours(3))
    );
    assertTrue(schedulingRequest2.isDuring(
        LocalDateTime.now().minusHours(4),
        LocalDateTime.now().plusHours(3))
    );
  }

  @Test
  void getAllAvailableNodesTest() {
    sanitationDao.insert(schedulingRequest);
    assertTrue(sanitationDao.allAvailableNodes(LocalDateTime.now()).contains(nodeA));
    sanitationDao.insert(schedulingRequest2);
    assertFalse(sanitationDao.allAvailableNodes(LocalDateTime.now()).contains(nodeA));
  }

}
