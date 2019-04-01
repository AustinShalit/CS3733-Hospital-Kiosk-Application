package edu.wpi.cs3733.d19.teamO.entity.database;

import edu.wpi.cs3733.d19.teamO.entity.SchedulingRequest;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class SchedulingRequestDaoDb implements SchedulingRequestDao {

  private static final String QUERY_FILE_NAME = "scheduling_request_queries.properties";

  private static final Logger logger
      = Logger.getLogger(SchedulingRequestDaoDb.class.getName());
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
  private NodeDaoDb nodeDaoDb;

  SchedulingRequestDaoDb(final DatabaseConnectionFactory dcf) throws SQLException {
    this.dcf = dcf;
    nodeDaoDb = new NodeDaoDb(dcf);
    createTable();
  }

  SchedulingRequestDaoDb() throws SQLException {
    this(new DatabaseConnectionFactoryEmbedded());
  }

  @Override
  public Optional<SchedulingRequest> get(final Integer id) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement
          = connection.prepareStatement(queries.getProperty("scheduling_request.select"));
      statement.setInt(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(extractSchedulingRequestFromResultSet(resultSet));
        }
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to get Scheduling Request", ex);
    }
    return Optional.empty();
  }

  @Override
  public Set<SchedulingRequest> getAll() {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement
          = connection.prepareStatement(queries.getProperty("scheduling_request.select_all"));
      try (ResultSet resultSet = statement.executeQuery()) {
        Set<SchedulingRequest> schedulingRequests = new HashSet<>();
        while (resultSet.next()) {
          schedulingRequests.add(extractSchedulingRequestFromResultSet(resultSet));
        }
        return schedulingRequests;
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to get Scheduling Requests", ex);
    }
    return Collections.emptySet();
  }

  @Override
  public boolean insert(final SchedulingRequest schedulingRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("scheduling_request.insert"));
      statement.setInt(1, schedulingRequest.getID());
      statement.setTimestamp(2, Timestamp.valueOf(schedulingRequest.getStartTime()));
      statement.setTimestamp(3, Timestamp.valueOf(schedulingRequest.getEndTime()));
      statement.setTimestamp(4, Timestamp.valueOf(schedulingRequest.getTimeRequested()));
      statement.setTimestamp(5,Timestamp.valueOf(schedulingRequest.getTimeCompleted()));
      statement.setString(6, schedulingRequest.getWhoReserved());
      statement.setString(7, schedulingRequest.getRoom().getNodeId());

      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to insert Scheduling Request", ex);
    }
    return false;
  }

  @Override
  public boolean update(final SchedulingRequest schedulingRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(
          queries.getProperty("scheduling_request.update"));
      statement.setTimestamp(1, Timestamp.valueOf(schedulingRequest.getStartTime()));
      statement.setTimestamp(2, Timestamp.valueOf(schedulingRequest.getEndTime()));
      statement.setTimestamp(3, Timestamp.valueOf(schedulingRequest.getTimeRequested()));
      statement.setTimestamp(4, Timestamp.valueOf(schedulingRequest.getTimeCompleted()));
      statement.setString(5, schedulingRequest.getWhoReserved());
      statement.setString(6, schedulingRequest.getRoom().getNodeId());
      statement.setInt(7, schedulingRequest.getID());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to update Scheduling Request", ex);
    }
    return false;
  }

  @Override
  public boolean delete(final SchedulingRequest schedulingRequest) {
    try (Connection connection = dcf.getConnection()) {
      PreparedStatement statement
          = connection.prepareStatement(queries.getProperty("scheduling_request.delete"));
      statement.setInt(1, schedulingRequest.getID());
      return statement.executeUpdate() == 1;
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to delete Scheduling Request", ex);
    }
    return false;
  }

  private SchedulingRequest extractSchedulingRequestFromResultSet(final ResultSet resultSet)
      throws SQLException {
    return new SchedulingRequest(
        resultSet.getInt("id"),
        resultSet.getTimestamp("start_time").toLocalDateTime(),
        resultSet.getTimestamp("end_time").toLocalDateTime(),
        resultSet.getTimestamp("time_requested").toLocalDateTime(),
        resultSet.getTimestamp("time_completed").toLocalDateTime(),
        resultSet.getString("who_requested"),
        nodeDaoDb.get(resultSet.getString("location"))
            .orElseThrow(() -> new SQLException("Could not get node for scheduling request"))
    );
  }

  private void createTable() throws SQLException {
    try (Connection connection = dcf.getConnection();
         ResultSet resultSet = connection.getMetaData().getTables(null, null,
             queries.getProperty("scheduling_request.table_name"), null)) {
      if (!resultSet.next()) {
        logger.info("Table "
            + queries.getProperty("scheduling_request.table_name")
            + " does not exist. Creating");
        PreparedStatement statement
            = connection.prepareStatement(queries.getProperty("scheduling_request.create_table"));
        statement.executeUpdate();
        logger.info("Table " + queries.getProperty("scheduling_request.table_name")
            + " created");
      } else {
        logger.info("Table " + queries.getProperty("scheduling_request.table_name") + " exists");
      }
    } catch (SQLException ex) {
      logger.log(Level.WARNING, "Failed to create table", ex);
      throw ex;
    }
  }
}
