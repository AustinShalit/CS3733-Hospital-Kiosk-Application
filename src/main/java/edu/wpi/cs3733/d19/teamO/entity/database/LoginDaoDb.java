package edu.wpi.cs3733.d19.teamO.entity.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.cs3733.d19.teamO.entity.Login;

public class LoginDaoDb implements LoginDao {

  private static final Logger logger = Logger.getLogger(LoginDaoDb.class.getName());

  private static final String QUERY_FILE_NAME = "login_queries.properties";

  private static final Properties queries;

  static {
    queries = new Properties();
    try (InputStream is = SchedulingRequestDaoDb.class.getResourceAsStream(QUERY_FILE_NAME)) {
      queries.load(is);
    } catch (IOException ex) {
      logger.log(Level.SEVERE, "Unable to load property file: " + QUERY_FILE_NAME, ex);
    }
  }

  private DatabaseConnectionFactory dcf;
  private EmployeeDaoDb employeeDaoDb;

  LoginDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
    this.dcf = dcf;
    employeeDaoDb = new EmployeeDaoDb(dcf);
    createTable();
  }

  LoginDaoDb() throws SQLException {
    this(new DatabaseConnectionFactoryEmbedded());
  }

  @Override
  public Optional<Login> get(final String username) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("login.select"));
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
          = connection.prepareStatement(queries.getProperty("login.select_all"));
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
        resultSet.getString("username"),
        resultSet.getString("password"),
        employeeDaoDb.get(resultSet.getInt("emp_id"))
            .orElseThrow(() -> new SQLException("Could not get associated employee"))
    );
  }

  @Override
  public boolean insert(final Login login) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("login.insert"));
      statement.setString(1, login.getUsername());
      statement.setString(2, login.getPassword());
      statement.setInt(3, login.getEmployee().getId());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to insert Login", ex);
    }
    return false;
  }

  @Override
  public boolean update(final Login login) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("login.update"));

      statement.setString(1, login.getUsername());
      statement.setString(2, login.getPassword());
      statement.setInt(3, login.getEmployee().getId());

      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to update Login", ex);
    }
    return false;
  }

  @Override
  public boolean delete(Login login) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("login.delete"));
      statement.setString(1, login.getUsername());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "FAILED to delete SanitationRequest");
    }
    return false;
  }

  private void createTable() throws SQLException {
    try (Connection connection = dcf.getConnection();
         ResultSet resultSet = connection.getMetaData().getTables(null, null,
             queries.getProperty("login.table_name"), null)) {
      if (!resultSet.next()) {
        logger.info("Table " + queries.getProperty("login.table_name") + " does not exist. "
            + "Creating");
        PreparedStatement statement = connection.prepareStatement(
            queries.getProperty("login.create_table"));
        statement.executeUpdate();
        logger.info("Table " + queries.getProperty("login.table_name") + " created");
      } else {
        logger.info("Table " + queries.getProperty("login.table_name") + " exists");
      }
    } catch (SQLException exception) {
      logger.log(Level.WARNING, "Failed to create table", exception);
      throw exception;
    }
  }
}
