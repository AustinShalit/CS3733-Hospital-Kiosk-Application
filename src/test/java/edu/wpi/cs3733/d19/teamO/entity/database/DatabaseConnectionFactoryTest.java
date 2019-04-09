package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DatabaseConnectionFactoryTest {

  @Test
  void getConnectionTest() throws SQLException {
    try (Connection con = new DatabaseConnectionFactoryEmbedded(
        DatabaseConnectionFactoryEmbedded.MEMORY_PROTOCOL,
        "Test").getConnection()) {
      assertNotNull(con);
    }
  }
}
