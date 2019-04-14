package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;

import com.google.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.wpi.cs3733.d19.teamO.entity.Employee;
import edu.wpi.cs3733.d19.teamO.entity.EmployeeAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DatabaseExtension.class)
class EmployeeDaoDbTest {

  private static final Employee TEST_EMPLOYEE_1 = new Employee(
      1,
      "dev",
      "hello",
      "Dev",
      new EmployeeAttributes(Employee.EmployeeType.ADMIN)
  );

  private EmployeeDao dao;

  @Inject
  private DatabaseConnectionFactory dcf;

  @BeforeEach
  void setup() throws SQLException {
    dao = new EmployeeDaoDb(dcf);
  }

  @Test
  void getTest() {
    dao.insert(TEST_EMPLOYEE_1);

    assertTrue(dao.get(TEST_EMPLOYEE_1.getId()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    dao.insert(TEST_EMPLOYEE_1);

    assertNotSame(TEST_EMPLOYEE_1,
        dao.get(TEST_EMPLOYEE_1.getId()).orElseThrow(IllegalStateException::new));
  }

  @Test
  void getNotExistingTest() {
    assertFalse(dao.get(TEST_EMPLOYEE_1.getId()).isPresent());
  }

  @Test
  void insertTest() {
    assertTrue(dao.insert(TEST_EMPLOYEE_1));
  }

  @Test
  void deleteTest() {
    dao.insert(TEST_EMPLOYEE_1);
    assertTrue(dao.delete(TEST_EMPLOYEE_1));
  }

  @Test
  void deleteNotExistingTest() {
    assertFalse(dao.delete(TEST_EMPLOYEE_1));
  }

  @Test
  void updateTest() {
    dao.insert(TEST_EMPLOYEE_1);

    Employee update = new Employee(
        TEST_EMPLOYEE_1.getId(),
        "dev",
        "meow",
        "Dev",
        new EmployeeAttributes(Employee.EmployeeType.ADMIN)
    );
    assertTrue(dao.update(update));
  }

  @Test
  void updateNotExistingTest() {
    assertFalse(dao.update(TEST_EMPLOYEE_1));
  }

  @Test
  void getAllTest() {
    dao.insert(TEST_EMPLOYEE_1);

    assertEquals(1, dao.getAll().size());
  }

  @Test
  void getAllResultSameTest() {
    dao.insert(TEST_EMPLOYEE_1);

    System.out.println(dao.getAll().toArray()[0]);
    assertEquals(TEST_EMPLOYEE_1, dao.getAll().toArray()[0]);
  }

  @Test
  void getAllEmptyTest() {
    dao.delete(TEST_EMPLOYEE_1);

    assertTrue(dao.getAll().isEmpty());
  }
}
