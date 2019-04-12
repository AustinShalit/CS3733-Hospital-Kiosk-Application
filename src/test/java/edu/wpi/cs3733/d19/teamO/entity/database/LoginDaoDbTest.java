package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;

import com.google.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.wpi.cs3733.d19.teamO.entity.Login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DatabaseExtension.class)
class LoginDaoDbTest {

  private static final Login testLogin1 = new Login(1, "dev", "hello", "Dev",
      Login.EmployeeType.ADMIN);

  private LoginDao dao;

  @Inject
  private DatabaseConnectionFactory dcf;

  @BeforeEach
  void setup() throws SQLException {
    dao = new LoginDaoDb(dcf);
  }

  @Test
  void getTest() {
    dao.insert(testLogin1);

    assertTrue(dao.get(testLogin1.getId()).isPresent());
  }

  @Test
  void getDifferentObjectTest() {
    dao.insert(testLogin1);

    assertNotSame(testLogin1,
        dao.get(testLogin1.getId()).orElseThrow(IllegalStateException::new));
  }

  @Test
  void getNotExistingTest() {
    assertFalse(dao.get(testLogin1.getId()).isPresent());
  }

  @Test
  void insertTest() {
    assertTrue(dao.insert(testLogin1));
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

    Login update = new Login(testLogin1.getId(), "dev", "meow", "Dev",
        Login.EmployeeType.ADMIN);
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
