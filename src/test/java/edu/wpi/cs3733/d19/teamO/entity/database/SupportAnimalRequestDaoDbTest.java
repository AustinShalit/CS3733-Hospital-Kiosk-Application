package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;

import com.google.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SupportAnimalRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DatabaseExtension.class)
class SupportAnimalRequestDaoDbTest {
  private static final int id1 = 123;
  private static final int id2 = 1234;

  private static final Node NODE_1 = new Node(Integer.toString(id1), 0, 0,
      "FL2", "Central", Node.NodeType.CONF, "LONGNAME",
      "SHORTNAME");

  private static final Node NODE_2 = new Node(Integer.toString(id2), 1, 1,
      "FL3", "Central", Node.NodeType.DEPT, "LONGNAME",
      "SHORTNAME");

  private static final SupportAnimalRequest REQUEST_1 =
      new SupportAnimalRequest(1, LocalDateTime.now(), LocalDateTime.now(), NODE_1,
          "Bob", SupportAnimalRequest.SupportAnimalRequestType.DRAGON,
          "This is a description", "Dev");

  private static final SupportAnimalRequest REQUEST_2 =
      new SupportAnimalRequest(2, LocalDateTime.now(), LocalDateTime.now(), NODE_2,
          "Bill", SupportAnimalRequest.SupportAnimalRequestType.DOG,
          "This is a description", "Ken");


  private SupportAnimalRequestDao supportAnimalRequestDao;

  @Inject
  private DatabaseConnectionFactory dcf;

  @BeforeEach
  void setup() throws SQLException {
    NodeDao nodeDao = new NodeDaoDb(dcf);
    nodeDao.insert(NODE_1);
    nodeDao.insert(NODE_2);

    supportAnimalRequestDao = new SupportAnimalRequestDaoDb(dcf);
  }

  @Test
  void getTest() {
    supportAnimalRequestDao.insert(REQUEST_1);

    assertTrue(supportAnimalRequestDao.get(REQUEST_1.getId()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    supportAnimalRequestDao.insert(REQUEST_1);

    assertNotSame(REQUEST_1,
        supportAnimalRequestDao.get(REQUEST_1.getId()).orElseThrow(IllegalStateException::new));
  }

  @Test
  void checkEqual() {
    supportAnimalRequestDao.insert(REQUEST_1);

    assertEquals(REQUEST_1, supportAnimalRequestDao.get(REQUEST_1.getId()).get());
  }

  @Test
  void getNotExistingTest() {
    assertFalse(supportAnimalRequestDao.get(987).isPresent());
  }

  @Test
  void insertTest() {
    assertTrue(supportAnimalRequestDao.insert(REQUEST_2));
  }

  @Test
  void deleteTest() {
    supportAnimalRequestDao.insert(REQUEST_1);
    assertTrue(supportAnimalRequestDao.delete(REQUEST_1));
  }

  @Test
  void deleteNotExistingTest() {
    assertFalse(supportAnimalRequestDao.delete(REQUEST_1));
  }

  @Test
  void updateTest() {
    supportAnimalRequestDao.insert(REQUEST_1);

    SupportAnimalRequest update = new SupportAnimalRequest(REQUEST_1.getId(),
        LocalDateTime.now(), LocalDateTime.now(),
        NODE_1, "Fred",
        SupportAnimalRequest.SupportAnimalRequestType.OTHER,
        "A different description", "Austin");

    assertTrue(supportAnimalRequestDao.update(update));
    assertNotEquals(REQUEST_1, supportAnimalRequestDao.get(REQUEST_1.getId()));
  }

  @Test
  void updateNotExistingTest() {
    assertFalse(supportAnimalRequestDao.update(REQUEST_1));
  }

  @Test
  void getAllTest() {
    supportAnimalRequestDao.insert(REQUEST_1);
    supportAnimalRequestDao.insert(REQUEST_2);

    assertEquals(2, supportAnimalRequestDao.getAll().size());
  }

  @Test
  void getAllEmptyTest() {
    supportAnimalRequestDao.delete(REQUEST_1);

    assertTrue(supportAnimalRequestDao.getAll().isEmpty());
  }

  @Test
  void autoIncrementTest() {
    for (int i = 0; i < 10; i++) {
      supportAnimalRequestDao.insert(REQUEST_1);
    }

    Set<SupportAnimalRequest> requests = supportAnimalRequestDao.getAll();
    for (SupportAnimalRequest request : requests) {
      // make sure the id is in the correct range
      assertTrue(request.getId() < 11 || request.getId() > 1);
    }
  }
}
