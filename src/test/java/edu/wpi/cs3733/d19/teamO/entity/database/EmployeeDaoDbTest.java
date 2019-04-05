package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import edu.wpi.cs3733.d19.teamO.entity.Employee;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeDaoDbTest {

  private static final Employee testEmployee = new Employee(123, "Test Employee",
      Employee.EmployeeType.DEFAULT);
  private static final Employee testAdmin = new Employee(2020, "Dr. Owlman",
      Employee.EmployeeType.ADMIN);

  @Nested
  class Creation {
    @Test
    void createTableTest(TestInfo testInfo) {
      DatabaseConnectionFactory dcf
          = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
          testInfo.getDisplayName());

      assertDoesNotThrow(() -> new EmployeeDaoDb(dcf));
    }

    @Test
    void existingTableTest(TestInfo testInfo) {
      DatabaseConnectionFactory dcf
          = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
          testInfo.getDisplayName());

      assertDoesNotThrow(() -> new EmployeeDaoDb(dcf));
    }
  }

  private EmployeeDao dao;

  @BeforeEach
  void setup(TestInfo testInfo) throws SQLException {
    DatabaseConnectionFactory dcf
        = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
        testInfo.getTestClass().get().getName()
            + testInfo.getDisplayName());

    dao = new EmployeeDaoDb(dcf);
  }

  @Test
  void getTest() {
    dao.insert(testEmployee);

    assertTrue(dao.get(testEmployee.getId()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    dao.insert(testEmployee);

    assertNotSame(testEmployee,
        dao.get(testEmployee.getId()).orElseThrow(IllegalStateException::new));
  }

  @Test
  void getNotExistingTest() {
    assertFalse(dao.get(testEmployee.getId()).isPresent());
  }

  @Test
  void insertTest() {
    assertTrue(dao.insert(testEmployee));
  }

  @Test
  void insertTwiceTest() {
    dao.insert(testEmployee);
    assertFalse(dao.insert(testEmployee));
  }

  @Test
  void deleteTest() {
    dao.insert(testEmployee);
    assertTrue(dao.delete(testEmployee));
  }

  @Test
  void deleteNotExistingTest() {
    assertFalse(dao.delete(testEmployee));
  }

  @Test
  void updateTest() {
    dao.insert(testEmployee);

    Employee update = new Employee(
        testEmployee.getId(), "meow", Employee.EmployeeType.SECURITY);
    assertTrue(dao.update(update));
  }

  @Test
  void updateNotExistingTest() {
    assertFalse(dao.update(testEmployee));
  }

  @Test
  void getAllTest() {
    dao.insert(testEmployee);
    dao.insert(testAdmin);

    assertEquals(2, dao.getAll().size());
  }

  @Test
  void getAllResultSameTest() {
    dao.insert(testEmployee);

    System.out.println(dao.getAll().toArray()[0]);
    assertEquals(testEmployee, dao.getAll().toArray()[0]);
  }

  @Test
  void getAllEmptyTest() {
    dao.delete(testEmployee);

    assertTrue(dao.getAll().isEmpty());
  }
}
