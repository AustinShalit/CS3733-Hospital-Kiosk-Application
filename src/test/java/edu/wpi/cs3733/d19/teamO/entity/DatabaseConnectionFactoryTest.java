package edu.wpi.cs3733.d19.teamO.entity;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DatabaseConnectionFactoryTest {

  @Test
  void getConnectionTest() throws SQLException {
    assertNotNull(new DatabaseConnectionFactoryImpl(DatabaseConnectionFactoryImpl.MEMORY_PROTOCOL,
        "Test").getConnection());
  }
}
