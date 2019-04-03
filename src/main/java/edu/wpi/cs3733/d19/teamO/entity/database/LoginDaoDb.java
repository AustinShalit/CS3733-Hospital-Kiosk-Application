package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.cs3733.d19.teamO.entity.Login;

public class LoginDaoDb implements LoginDao {

  private static final Logger logger = Logger.getLogger(LoginDaoDb.class.getName());

  private static final String TABLE_NAME = "LOGIN";
  private final DatabaseConnectionFactory dcf;

  LoginDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
    this.dcf = dcf;
    createTable();
  }

  LoginDaoDb() throws SQLException {
    this(new DatabaseConnectionFactoryEmbedded());
  }

  @Override
  public Optional<Login> get(final String username) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME
          + " WHERE lg_username=?");
      statement.setString(1, username);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(extractLoginFromResultSet(resultSet));
        }
      }
    } catch (SQLException exception) {
      logger.log(Level.WARNING, "Failed to get Login", exception);
    }
    return Optional.empty();
  }

  @Override
  public Set<Login> getAll() {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement
          = connection.prepareStatement("SELECT * FROM " + TABLE_NAME);
      try (ResultSet resultSet = statement.executeQuery()) {
        Set<Login> login = new HashSet<>();
        while (resultSet.next()) {
          login.add(extractLoginFromResultSet(resultSet));
        }
        return login;
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to get Logins", ex);
    }
    return Collections.emptySet();
  }

  private Login extractLoginFromResultSet(final ResultSet resultSet) throws SQLException {
    return new Login(
        resultSet.getString("lg_username"),
        resultSet.getString("password")
    );
  }

  @Override
  public boolean insert(final Login login) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement("INSERT INTO " + TABLE_NAME
          + " VALUES (?, ?)");
      statement.setString(1, login.getUsername());
      statement.setString(2, login.getPassword());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to insert Login", ex);
    }
    return false;
  }

  @Override
  public boolean update(final Login login) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement("UPDATE " + TABLE_NAME
          + " SET lg_username=?,"
          + "password=?"
          + " WHERE lg_username=?");
      statement.setString(1, login.getUsername());
      statement.setString(2, login.getPassword());
      statement.setString(3, login.getUsername());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to update Login", ex);
    }
    return false;
  }

  @Override
  public boolean delete(Login login) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM " + TABLE_NAME
          + " WHERE lg_username=?");
      statement.setString(1, login.getUsername());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "FAILED to delete SanitationRequest");
    }
    return false;
  }

  private void createTable() throws SQLException {
    try (Connection connection = dcf.getConnection();
         ResultSet resultSet = connection.getMetaData().getTables(null, null, TABLE_NAME, null)) {
      if (!resultSet.next()) {
        logger.info("Table " + TABLE_NAME + " does not exist. Creating");
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE " + TABLE_NAME
            + "(lg_username VARCHAR(255) PRIMARY KEY, "
            + "PASSWORD VARCHAR(255))");
        statement.executeUpdate();
        logger.info("Table " + TABLE_NAME + " created");
      } else {
        logger.info("Table " + TABLE_NAME + " exists");
      }
    } catch (SQLException exception) {
      logger.log(Level.WARNING, "Failed to create table", exception);
      throw exception;
    }
  }
}