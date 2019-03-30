package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

final class DatabaseConnectionFactoryImpl implements DatabaseConnectionFactory {

  private static final Logger kLogger
      = Logger.getLogger(DatabaseConnectionFactoryImpl.class.getName());

  static final String EMBEDDED_PROTOCOL = "jdbc:derby:";
  static final String MEMORY_PROTOCOL = EMBEDDED_PROTOCOL + "memory:";

  private final String protocol;
  private final String name;

  DatabaseConnectionFactoryImpl(final String protocol, final String name) {
    this.protocol = checkNotNull(protocol);
    this.name = checkNotNull(name);

    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
      kLogger.log(Level.SEVERE, "Derby embedded driver not found!", ex);
    }
  }

  DatabaseConnectionFactoryImpl() {
    this(EMBEDDED_PROTOCOL, "ProjectDb");
  }

  @Override
  public Connection getConnection() throws SQLException {
    try {
      return DriverManager.getConnection(protocol + name + ";create=true");
    } catch (SQLException ex) {
      kLogger.log(Level.SEVERE, "Exception getting connection to database", ex);
      throw ex;
    }
  }
}
