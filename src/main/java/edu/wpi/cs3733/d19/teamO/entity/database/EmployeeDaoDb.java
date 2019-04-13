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

import edu.wpi.cs3733.d19.teamO.entity.Employee;

public class EmployeeDaoDb implements EmployeeDao {

  private static final Logger logger = Logger.getLogger(EmployeeDaoDb.class.getName());

  private static final String QUERY_FILE_NAME = "employee_queries.properties";

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

  EmployeeDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
    this.dcf = dcf;
    createTable();
  }

  EmployeeDaoDb() throws SQLException {
    this(new DatabaseConnectionFactoryEmbedded());
  }

  @Override
  public Optional<Employee> get(final Integer id) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("employee.select"));
      statement.setInt(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(extractEmployeeFromResultSet(resultSet));
        }
      }
    } catch (SQLException exception) {
      logger.log(Level.WARNING, "Failed to get Employee", exception);
    }
    return Optional.empty();
  }

  @Override
  public Set<Employee> getAll() {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement
          = connection.prepareStatement(queries.getProperty("employee.select_all"));
      try (ResultSet resultSet = statement.executeQuery()) {
        Set<Employee> employee = new HashSet<>();
        while (resultSet.next()) {
          employee.add(extractEmployeeFromResultSet(resultSet));
        }
        return employee;
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to get Employees", ex);
    }
    return Collections.emptySet();
  }

  private Employee extractEmployeeFromResultSet(final ResultSet resultSet) throws SQLException {
    return new Employee(
        resultSet.getInt("id"),
        resultSet.getString("username"),
        resultSet.getString("password"),
        resultSet.getString("name"),
        Employee.EmployeeType.get(resultSet.getString("type"))
    );
  }

  @Override
  public boolean insert(final Employee employee) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("employee.insert"));
      statement.setString(1, employee.getUsername());
      statement.setString(2, employee.getPassword());
      statement.setString(3, employee.getName());
      statement.setString(4, employee.getType().name());

      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to insert Employee", ex);
    }
    return false;
  }

  @Override
  public boolean update(final Employee employee) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("employee.update"));

      statement.setString(1, employee.getUsername());
      statement.setString(2, employee.getPassword());
      statement.setString(3, employee.getName());
      statement.setString(4, employee.getType().name());
      statement.setInt(5, employee.getId());

      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to update Employee", ex);
    }
    return false;
  }

  @Override
  public boolean delete(Employee employee) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("employee.delete"));
      statement.setInt(1, employee.getId());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to delete Employee");
    }
    return false;
  }

  private void createTable() throws SQLException {
    try (Connection connection = dcf.getConnection();
         ResultSet resultSet = connection.getMetaData().getTables(null, null,
             queries.getProperty("employee.table_name"), null)) {
      if (!resultSet.next()) {
        logger.info("Table " + queries.getProperty("employee.table_name") + " does not exist. "
            + "Creating");
        PreparedStatement statement = connection.prepareStatement(
            queries.getProperty("employee.create_table"));
        statement.executeUpdate();
        logger.info("Table " + queries.getProperty("employee.table_name") + " created");
      } else {
        logger.info("Table " + queries.getProperty("employee.table_name") + " exists");
      }
    } catch (SQLException exception) {
      logger.log(Level.WARNING, "Failed to create table", exception);
      throw exception;
    }
  }
}
