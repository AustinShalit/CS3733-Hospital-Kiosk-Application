package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import edu.wpi.cs3733.d19.teamO.entity.Employee;
import edu.wpi.cs3733.d19.teamO.entity.Login;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginDaoDbTest {

  private static final Employee testEmployee1 = new Employee(123, "Dev",
      Employee.EmployeeType.ADMIN);
  private static final Login testLogin1 = new Login("dev", "hello",
      testEmployee1);

  @Nested
  class Creation {
    @Test
    void createTableTest(TestInfo testInfo) {
      DatabaseConnectionFactory dcf
          = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
          testInfo.getDisplayName());

      assertDoesNotThrow(() -> new LoginDaoDb(dcf));
    }

    @Test
    void existingTableTest(TestInfo testInfo) {
      DatabaseConnectionFactory dcf
          = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
          testInfo.getDisplayName());

      assertDoesNotThrow(() -> new LoginDaoDb(dcf));
    }
  }

  private LoginDao dao;

  @BeforeEach
  void setup(TestInfo testInfo) throws SQLException {
    DatabaseConnectionFactory dcf
        = new DatabaseConnectionFactoryEmbedded(DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
        testInfo.getTestClass().get().getName()
            + testInfo.getDisplayName());

    EmployeeDaoDb employeeDao = new EmployeeDaoDb(dcf);
    employeeDao.insert(testEmployee1);

    dao = new LoginDaoDb(dcf);
  }

  @Test
  void getTest() {
    dao.insert(testLogin1);

    assertTrue(dao.get(testLogin1.getUsername()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    dao.insert(testLogin1);

    assertNotSame(testLogin1,
        dao.get(testLogin1.getUsername()).orElseThrow(IllegalStateException::new));
  }

  @Test
  void getNotExistingTest() {
    assertFalse(dao.get(testLogin1.getUsername()).isPresent());
  }

  @Test
  void insertTest() {
    assertTrue(dao.insert(testLogin1));
  }

  @Test
  void insertTwiceTest() {
    dao.insert(testLogin1);
    assertFalse(dao.insert(testLogin1));
  }

  @Test
  void deleteTest() {
    dao.insert(testLogin1);
    assertTrue(dao.delete(testLogin1));
  }

  @Test
  void deleteNotExistingTest() {
    assertFalse(dao.delete(testLogin1));
  }

  @Test
  void updateTest() {
    dao.insert(testLogin1);

    Login update = new Login(testLogin1.getUsername(), "meow", testEmployee1);
    assertTrue(dao.update(update));
  }

  @Test
  void updateNotExistingTest() {
    assertFalse(dao.update(testLogin1));
  }

  @Test
  void getAllTest() {
    dao.insert(testLogin1);

    assertEquals(1, dao.getAll().size());
  }

  @Test
  void getAllResultSameTest() {
    dao.insert(testLogin1);

    System.out.println(dao.getAll().toArray()[0]);
    assertEquals(testLogin1, dao.getAll().toArray()[0]);
  }

  @Test
  void getAllEmptyTest() {
    dao.delete(testLogin1);

    assertTrue(dao.getAll().isEmpty());
  }
}
