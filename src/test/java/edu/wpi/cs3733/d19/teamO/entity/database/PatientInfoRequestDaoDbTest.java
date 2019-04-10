package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.google.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.PatientInfoRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DatabaseExtension.class)
class PatientInfoRequestDaoDbTest {
  private static final int id1 = 123;
  private static final int id2 = 1234;

  private static final Node testNode1 = new Node(Integer.toString(id1), 0, 0,
      "FL2", "Central", Node.NodeType.CONF, "LONGNAME",
      "SHORTNAME");

  private static final Node testNode2 = new Node(Integer.toString(id2), 1, 1,
      "FL3", "Central", Node.NodeType.DEPT, "LONGNAME",
      "SHORTNAME");


  private static final PatientInfoRequest PIR1 = new PatientInfoRequest(
      1,
      LocalDateTime.now(),
      LocalDateTime.now(),
      testNode1,
      "Dr. Bob, PhD",
      "",
      "John Doe",
      LocalDate.of(1969, 4, 3),
      PatientInfoRequest.PatientInfoSex.MALE
  );


  private static final PatientInfoRequest PIR2 = new PatientInfoRequest(
      2,
      LocalDateTime.now(),
      LocalDateTime.now(),
      testNode2,
      "Jim",
      "",
      "John Doe III",
      LocalDate.of(1999, 4, 3),
      PatientInfoRequest.PatientInfoSex.MALE
  );

  private Database db;

  @Inject
  private DatabaseConnectionFactory dcf;

  @BeforeEach
  void setup() throws SQLException {
    db = new Database(dcf);
  }

  @Test
  void getTest() {
    db.insertNode(testNode1);
    db.insertPatientInfoRequest(PIR1);

    assertTrue(db.getPatientInfoRequest(PIR1.getId()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    db.insertNode(testNode1);
    db.insertPatientInfoRequest(PIR1);

    assertNotSame(PIR1, db.getPatientInfoRequest(PIR1.getId())
        .orElseThrow(IllegalStateException::new));
  }

  @Test
  void checkEqual() {
    db.insertNode(testNode1);
    db.insertPatientInfoRequest(PIR1);
    assertEquals(PIR1, db.getPatientInfoRequest(PIR1.getId()).get());
  }

  @Test
  void getNotExistingTest() {
    assertFalse(db.getPatientInfoRequest(254).isPresent());
  }

  @Test
  void insertTest() {
    db.insertNode(testNode2);
    assertTrue(db.insertPatientInfoRequest(PIR2));
  }

  @Test
  void deleteTest() {
    db.insertNode(testNode1);
    db.insertPatientInfoRequest(PIR1);
    assertTrue(db.deletePatientInfoRequest(PIR1));
  }

  @Test
  void deleteNotExistingTest() {
    assertFalse(db.deletePatientInfoRequest(PIR2));
  }

  @Test
  void updateTest() {
    db.insertNode(testNode1);
    db.insertPatientInfoRequest(PIR1);

    PatientInfoRequest update = new PatientInfoRequest(
        PIR1.getId(),
        LocalDateTime.now(),
        LocalDateTime.now(),
        testNode1,
        "Dr. Bob",
        "",
        "John Doe",
        LocalDate.of(1969, 4, 3),
        PatientInfoRequest.PatientInfoSex.MALE
    );

    assertTrue(db.updatePatientInfoRequest(update));
    assertNotEquals(PIR1, db.getPatientInfoRequest(PIR1.getId()));
  }

  @Test
  void updateNotExistingTest() {
    db.insertNode(testNode1);
    db.insertPatientInfoRequest(PIR1);

    PatientInfoRequest update = new PatientInfoRequest(
        125,
        LocalDateTime.now(),
        LocalDateTime.now(),
        testNode1,
        "Dr. Bob",
        "",
        "John Doe",
        LocalDate.of(1969, 4, 3),
        PatientInfoRequest.PatientInfoSex.MALE
    );

    assertFalse(db.updatePatientInfoRequest(update));
  }

  @Test
  void getAllTest() {
    db.insertNode(testNode1);
    db.insertNode(testNode2);
    db.insertPatientInfoRequest(PIR1);
    db.insertPatientInfoRequest(PIR2);

    assertEquals(2, db.getAllPatientInfoRequests().size());
  }

  @Test
  void getAllEmptyTest() {
    assertTrue(db.getAllPatientInfoRequests().isEmpty());
  }

  @Test
  void autoIncrementTest() {
    db.insertNode(testNode1);
    db.insertPatientInfoRequest(PIR1);

    for (int i = 0; i < 10; i++) {
      db.insertPatientInfoRequest(PIR1);
    }

    Set<PatientInfoRequest> patientInfoRequestSet = db.getAllPatientInfoRequests();
    for (PatientInfoRequest sr : patientInfoRequestSet) {
      // make sure the id is in the correct range
      assertTrue(sr.getId() < 11 || sr.getId() > 1);
    }
  }
}
