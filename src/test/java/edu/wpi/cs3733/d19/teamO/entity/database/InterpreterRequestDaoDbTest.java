package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;

import com.google.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.wpi.cs3733.d19.teamO.entity.InterpreterRequest;
import edu.wpi.cs3733.d19.teamO.entity.Node;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DatabaseExtension.class)
class InterpreterRequestDaoDbTest {
  private static final int id1 = 123;
  private static final int id2 = 1234;

  private static final Node testNode1 = new Node(Integer.toString(id1), 0, 0,
      "FL2", "Central", Node.NodeType.CONF, "LONGNAME",
      "SHORTNAME");

  private static final Node testNode2 = new Node(Integer.toString(id2), 1, 1,
      "FL3", "Central", Node.NodeType.DEPT, "LONGNAME",
      "SHORTNAME");

  private static final InterpreterRequest testInterpreterRequest1 =
      new InterpreterRequest(1, LocalDateTime.now(), LocalDateTime.now(),
          "Bob", "Translate", testNode1,
          InterpreterRequest.Language.FRA);

  private static final InterpreterRequest testInterpreterRequest2 =
      new InterpreterRequest(2, LocalDateTime.now(), LocalDateTime.now(),
          "Roberto", "More translating", testNode2,
          InterpreterRequest.Language.SPA);

  private InterpreterRequestDaoDb interpreterDao;
  private NodeDao nodeDao;

  @Inject
  private DatabaseConnectionFactory dcf;

  @BeforeEach
  void setup() throws SQLException {
    nodeDao = new NodeDaoDb(dcf);
    InterpreterRequestDaoDb interpreterRequestDaoDb = new InterpreterRequestDaoDb(dcf);

    nodeDao.insert(testNode1);
    interpreterRequestDaoDb.insert(testInterpreterRequest1);


    interpreterDao = new InterpreterRequestDaoDb(dcf);
  }

  @Test
  void getTest() {
    interpreterDao.insert(testInterpreterRequest1);

    assertTrue(interpreterDao.get(testInterpreterRequest1.getId()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    interpreterDao.insert(testInterpreterRequest1);

    assertNotSame(testInterpreterRequest1,
        interpreterDao.get(
            testInterpreterRequest1.getId()).orElseThrow(IllegalStateException::new));
  }

  @Test
  void checkEqual() {
    interpreterDao.insert(testInterpreterRequest1);

    assertEquals(testInterpreterRequest1, interpreterDao.get(
        testInterpreterRequest1.getId()).get());
  }

  @Test
  void getNotExistingTest() {
    assertFalse(interpreterDao.get(987).isPresent());
  }

  @Test
  void insertTest() {
    nodeDao.insert(testNode2);

    assertTrue(interpreterDao.insert(testInterpreterRequest2));
  }

  @Test
  void deleteTest() {
    interpreterDao.insert(testInterpreterRequest1);
    assertTrue(interpreterDao.delete(testInterpreterRequest1));
  }

  @Test
  void deleteNotExistingTest() {
    assertFalse(interpreterDao.delete(new InterpreterRequest(987, null,
        null, null, "", null, null)));
  }

  @Test
  void updateTest() {
    interpreterDao.insert(testInterpreterRequest1);

    InterpreterRequest update = new InterpreterRequest(testInterpreterRequest1.getId(),
        LocalDateTime.now(), LocalDateTime.now(),
        "Fred", "A different description", testNode1,
        InterpreterRequest.Language.SPA);

    assertTrue(interpreterDao.update(update));
    assertNotEquals(testInterpreterRequest1, interpreterDao.get(testInterpreterRequest1.getId()));
  }

  @Test
  void updateNotExistingTest() {
    Node newNode = new Node("9876", 0, 0, "2", "Fuller",
        Node.NodeType.DEPT, "longname", "shortname");
    assertFalse(interpreterDao.update(new InterpreterRequest(987, LocalDateTime.now(),
        LocalDateTime.now(), "Jane", "This request doesnt exist",
        newNode, InterpreterRequest.Language.ENG)));
  }

  @Test
  void getAllTest() {
    nodeDao.insert(testNode2);
    interpreterDao.insert(testInterpreterRequest2);

    assertEquals(2, interpreterDao.getAll().size());
  }

  @Test
  void getAllEmptyTest() {
    interpreterDao.delete(testInterpreterRequest1);

    assertTrue(interpreterDao.getAll().isEmpty());
  }

  @Test
  void autoIncrementTest() {
    for (int i = 0; i < 10; i++) {
      interpreterDao.insert(testInterpreterRequest1);
    }

    Set<InterpreterRequest> interpreterRequestSet = interpreterDao.getAll();
    for (InterpreterRequest ir : interpreterRequestSet) {
      // make sure the id is in the correct range
      assertTrue(ir.getId() < 11 || ir.getId() > 1);
    }
  }
}
